/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package pax.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOUtil;
import pax.netty.data.*;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;
import pax.util.AsyncConnector;
import tool.dao.BizObject;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Decodes the binary representation of a {@link BigInteger} prepended
 * with a magic number ('F' or 0x46) and a 32-bit integer length prefix into a
 * {@link BigInteger} instance.  For example, { 'F', 0, 0, 0, 1, 42 } will be
 * decoded into new BigInteger("42").
 */
public class MessageDecoder extends ByteToMessageDecoder {
    static Logger logger = Logger.getLogger(MessageDecoder.class);

    public final static AttributeKey<Request> CUR_REQUEST_KEY = AttributeKey.newInstance("cur_request");
    public final static AttributeKey<AsyncConnector> HTTP_CLIENT_KEY = AttributeKey.newInstance("http_client");
    public final static AttributeKey<List<BizObject>> TOTAL_FILES_LIST = AttributeKey.newInstance("files_list");

    public final static AttributeKey<BizObject> CUR_SN_KEY = AttributeKey.newInstance("cur_sn");
    /**
     * 暂时支持的交易种类
     * 7001
     * 7004
     * 7005
     */
    public final static List<String> SERVERLIST = Arrays.asList(
            TransCode.DOWNLOAD.getCode(),
            TransCode.INIT.getCode(),
            TransCode.CHANGE.getCode()
    );

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        logger.info("---MessageDecoder in decoder---");
        logger.info(String.format("[MessageDecoder decode]ByteBuf size is ：%s ,ByteBuf is : %s", in.readableBytes(), ByteBufUtil.hexDump(in)));

        if (in.readableBytes() < 14) {
            logger.info(" < 14 " + in.readableBytes());
            return;
        }
        //开头两个字节为 0x00 0x00
//        byte[] heartByte = new byte[]{0x00, 0x00};
//        byte[] heart = new byte[2];
//        while (true) {
//            in.markReaderIndex();
//            in.readBytes(heart);
//            logger.info("is heart!!! heart is : " + ISOUtil.hexString(heartByte) + ",the data is : " + ISOUtil.hexString(heart));
//            if (!heartByte.equals(heart)) {
//                in.resetReaderIndex();
//                logger.info("is not heart!!! heart is : " + ISOUtil.hexString(heartByte) + ",the data is : " + ISOUtil.hexString(heart));
//                break;
//            }
//        }
//        logger.info(String.format("[MessageDecoder decode]After Remove Heart!!! ByteBuf size is ：%s ,ByteBuf is : %s", in.readableBytes(), ByteBufUtil.hexDump(in)));

        in.markReaderIndex();

        //将相关信息保存到请求中，类型session
        Attribute<BizObject> cur_sn_session = ctx.channel().attr(CUR_SN_KEY);

        //读取总报文长度
        byte[] len = new byte[2];
        in.readBytes(len);
        int len_int = ByteUtils.btLen2IntLen(len);
        logger.info(String.format("readableBytes is : %s ,len is : %s", in.readableBytes(), len_int));
        //验证长度
        if (in.readableBytes() < len_int) {
            in.resetReaderIndex();
            return;
        }


        byte[] tpdu = new byte[5];

        in.readBytes(tpdu);

        String tpdu_str = ISOUtil.hexString(tpdu);
        //将目的地址和源地址交换位置
        byte[] reverseTPDU = reverseTPDU(tpdu);
        String re_tpdu_str = ISOUtil.hexString(reverseTPDU);

        logger.info(String.format("tpdu is : %s ,reverseTPDU is : %s,len_int is : %s", tpdu_str, re_tpdu_str, len_int));

        //已经访问过了
        if (cur_sn_session != null && cur_sn_session.get() != null
                && cur_sn_session.get().getString("tpdu").equals(re_tpdu_str)
                && SERVERLIST.contains(cur_sn_session.get().getString("code"))) {

            //记录子报文
            String dllMsg = ByteBufUtil.hexDump(in);
            //去掉外层的etx 和 lrc
            dllMsg = dllMsg.substring(0, dllMsg.length() - 4);

            //读子报文开头 0x02
            int magicNumber = in.readUnsignedByte();
            if (magicNumber != 2) {
                in.resetReaderIndex();
                return;
            }

            //读子报文长度 0x00,0x3F
            byte dlen[] = new byte[2];
            in.readBytes(dlen);
            int dlen_int = ByteUtils.btLen2IntLen(dlen);

//            //数据没读满，继续等待
//            if (in.readableBytes() < dlen_int + 2) {
//                in.resetReaderIndex();
//                return;
//            }
            //把中间数据读完
            byte[] to_read = new byte[in.readableBytes() - 2];
            in.readBytes(to_read);


            //读取外层的 0x03 0x76
            byte etx = in.readByte();
            byte lrc = in.readByte();

            //TODO 验证etx和lrc
            if (etx != Message.ETX) {
                logger.info("wrong etx " + etx);
            }


            CmdMsg cmdMsg = new CmdMsg(tpdu, dllMsg);
            out.add(cmdMsg);

        } else {
            if (cur_sn_session == null) {
                logger.info("[MessageDecoder decode]no session,this session is null ");
            } else {
                logger.info("[MessageDecoder decode]no session,this session hashcode is : " + cur_sn_session.hashCode());
            }
            byte[] ver = new byte[2];
            byte[] code = new byte[4];

            in.readBytes(ver);
            in.readBytes(code);

            logger.info(String.format("code is ：%s", ByteUtils.byte2String(code)));
            if (ByteUtils.byte2String(code).equals(TransCode.INIT.getCode()) || ByteUtils.byte2String(code).equals(TransCode.CHANGE.getCode())) {
                TerMsg terMsg = new TerMsg(in, reverseTPDU, ver, code);
                out.add(terMsg);
            } else if (ByteUtils.byte2String(code).equals(TransCode.CONFIRM.getCode())) {
                ConfirmMsg confirmMsg = new ConfirmMsg(in, reverseTPDU, ver, code);
                out.add(confirmMsg);

            } else if (ByteUtils.byte2String(code).equals(TransCode.ISSUE_APPINFO.getCode())) {
                IssuAppMsg issuAppMsg = new IssuAppMsg(in, reverseTPDU, ver, code);
                out.add(issuAppMsg);
            } else if (ByteUtils.byte2String(code).equals(TransCode.DOWNLOAD.getCode())) {
                DownloadMsg downloadMsg = new DownloadMsg(in, reverseTPDU, ver, code);
                out.add(downloadMsg);
            }
//            else if (ByteUtils.byte2String(code).equals(TransCode.SCRIPT.getCode())) {
//                ScriptMsg scriptMsg = new ScriptMsg(in, reverseTPDU, ver, code);
//                out.add(scriptMsg);
//            }

            else {
                logger.info(String.format("no server for the code ：%s", ByteUtils.byte2String(code)));
                //读完buffer
                byte[] bytes = new byte[in.readableBytes()];
                in.readBytes(bytes);
            }


        }
        logger.info("---MessageDecoder  out decoder---");
    }

    public byte[] reverseTPDU(byte[] tpdu) {
        byte[] h = tpdu;
        if (h != null) {
            if (tpdu.length == 5) {
                byte[] tmp = new byte[2];
                System.arraycopy(h, 1, tmp, 0, 2);
                System.arraycopy(h, 3, h, 1, 2);
                System.arraycopy(tmp, 0, h, 3, 2);
            }
        }
        return h;
    }
}
