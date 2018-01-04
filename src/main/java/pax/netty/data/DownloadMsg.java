package pax.netty.data;

import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOUtil;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;
import tool.dao.BizObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : wangtao
 * @Description :
 * @Date : 2017/10/25 17:01
 */
public class DownloadMsg extends Message {
    static Logger logger = Logger.getLogger(DownloadMsg.class);

    byte[] tpdu = new byte[5];
    byte[] ver = new byte[2];
    byte[] code = new byte[4];

    byte[] ter_sn = new byte[50];//设备序列号	ans50	不足右补0x20
    byte[] mer_no = new byte[15];//  商户编号	Ans15

    byte[] ter_no = new byte[8];//终端编号	Ans8

    byte[] dowmload_mark = new byte[2];//    下载内容标志	An2	从联机平台获取，区分银联卡应用与非银联卡应用  00：银联卡应用  01：非银联卡应用


    byte[] magic_code; //    应用版本号	N6/ANS..50(LLVAR)	从联机平台获取当下载内容标志为00时，格式为N6 当下载内容标志为01时，格式为ANS..50(LLVAR)

    byte[] dowmload_check_code = new byte[32];//下载任务校验码	ans32	从联机平台获取


    byte[] respcode = new byte[2];// 响应码	ans2	参见附录C


    public DownloadMsg() {

    }

    public DownloadMsg(ByteBuf buf, byte[] tpdu, byte[] ver, byte[] code) {
        buf.readBytes(ter_sn);
        buf.readBytes(mer_no);


        buf.readBytes(ter_no);
        buf.readBytes(dowmload_mark);
        logger.info("dowmload_mark is ：" + ISOUtil.hexString(dowmload_mark));
        //读取剩下的部分
        int re = buf.readableBytes();
        logger.info("剩下可读长度 ：" + re);
        magic_code = new byte[re - dowmload_check_code.length];
        buf.readBytes(magic_code);
        buf.readBytes(dowmload_check_code);

        this.tpdu = tpdu;
        this.ver = ver;
        this.code = code;
    }

    @Override
    public List<byte[]> getDataList() {
        List<byte[]> list = new ArrayList<>();
        list.add(tpdu);
        list.add(ver);
        list.add(code);
        list.add(ter_sn);
        return list;
    }

    @Override
    public void fill(ByteBuf buf) {

    }

    public void copyInfoTo(DownloadMsg downloadMsg) {
        downloadMsg.tpdu = this.tpdu;
        downloadMsg.ver = this.ver;
        downloadMsg.code = this.code;
        downloadMsg.ter_sn = this.ter_sn;

    }

    @Override
    public byte[] getRespcode() {
        return this.respcode;
    }

    public void setRespcode(TransCode resp) {
        this.respcode = TransCode.getCodeByte(resp);
    }

    public BizObject toBiz() {
        BizObject ter = new BizObject();
        ter.set("tpdu", ISOUtil.hexString(tpdu));
        ter.set("ver", ByteUtils.byte2String(ver));
        ter.set("code", ByteUtils.byte2String(code));
        ter.set("ter_sn", ByteUtils.byte2String(ter_sn));
        ter.set("mer_no", ByteUtils.byte2String(mer_no));
        ter.set("ter_no", ByteUtils.byte2String(ter_no));
        ter.set("dowmload_mark", ByteUtils.byte2String(dowmload_mark));
        ter.set("magic_code", ByteUtils.byte2String(magic_code));
        ter.set("dowmload_check_code", ByteUtils.byte2String(dowmload_check_code));
        return ter;
    }
}
