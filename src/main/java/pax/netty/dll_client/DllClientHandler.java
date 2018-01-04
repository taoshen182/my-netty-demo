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
package pax.netty.dll_client;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.apache.tomcat.util.buf.HexUtils;
import org.jpos.iso.ISOUtil;
import pax.netty.coder.BizMsgDecoder;
import pax.netty.coder.BizMsgEncoder;
import pax.netty.coder.MessageDecoder;
import pax.netty.coder.MessageEncoder;
import pax.netty.data.*;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;
import tool.dao.BizObject;

import static org.jboss.netty.channel.Channels.pipeline;
import static pax.netty.coder.MessageDecoder.CUR_REQUEST_KEY;

/**
 * Handler for a client-side channel.  This handler maintains stateful
 * information which is specific to a certain channel using member variables.
 * Therefore, an instance of this handler can cover only one channel.  You have
 * to create a new handler instance whenever you create a new channel and insert
 * this handler to avoid a race condition.
 */
public class DllClientHandler extends SimpleChannelInboundHandler<BizObject> {

    private ChannelHandlerContext ctx;
    private int receivedMessages;
    private int next = 1;

    private Channel tms_channel;

    public DllClientHandler() {
    }

    public DllClientHandler(Channel tms_channel) {
        this.tms_channel = tms_channel;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {

//        System.out.println("active ......");

        this.ctx = ctx;
//        ctx.writeAndFlush(b);
//        byte[] tpdu = new byte[]{0x60, 0x00, 0x36, 0x00, 0x00};
//
//        A4Msg a4Msg = new A4Msg(tpdu);
//
//        ctx.write(a4Msg);
//        ctx.flush();
//        BizObject test = new BizObject("test");
//        test.set("ttt", "www");
//        ctx.writeAndFlush(test);

        //ctx.flush();
//        System.out.println("end  ");
        //sendRemoteDownloadRequest();
        //sendNumbers();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, final BizObject msg) {

        System.out.println(" DllClientHandler receive msg " + msg);
//        Attribute<Request> cur_request = ctx.channel().attr(cur_request_key);
//
//        Request currequest = cur_request.get();//.set(request);

//        //可以 从dll客户端向dll服务端发送消息
//        byte[] tpdu = new byte[]{0x60, 0x66, 0x36, 0x00, 0x06};
//        A4Msg a4Msg = new A4Msg(tpdu);
//
//        ctx.write(a4Msg);
//        ctx.flush();

        //TODO 从dll客户端向POS发送消息
//        tms_channel.write(a4Msg);
//        tms_channel.flush();

        //TODO 将msg 转换成 pos报文
        String cmd = msg.getString("cmd");
        String tpdu = msg.getString("tpdu");
        String fid = msg.getString("fid");
        String mid = msg.getString("mid");
        String sn = msg.getString("sn");
        String ver = msg.getString("ver");
        String trans_code = msg.getString("trans_code");
        String version = msg.getString("version");
        System.out.println("trans_code = " + trans_code);
        if (cmd.equals("A0")) {
            byte[] bt_tpdu = ISOUtil.hex2byte(tpdu);
            byte[] bt_fid = ISOUtil.hex2byte(fid);
            byte[] bt_mid = ISOUtil.hex2byte(mid);
            byte[] bt_sn = ISOUtil.hex2byte(sn);
            byte[] bt_ver = ISOUtil.hex2byte(ver);
            byte[] bt_version = ISOUtil.hex2byte(version);

//            InitMsg initMsg = new InitMsg(bt_tpdu, bt_ver, bt_fid, bt_mid, bt_sn, bt_version);
//            initMsg.setCode(TransCode.getCodeByte(TransCode.SUCCESS));
//            tms_channel.write(initMsg);
//            tms_channel.flush();

        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
