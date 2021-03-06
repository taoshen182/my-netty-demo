package pax.netty.data;

import io.netty.buffer.ByteBuf;
import org.jpos.iso.ISOUtil;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;
import tool.dao.BizObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/6/30.
 */
public class ConfirmMsg extends Message {
    byte[] tpdu = new byte[5];
    byte[] ver = new byte[2];
    byte[] code = new byte[4];


    byte[] ter_sn = new byte[50]; //设备序列号	ans50

    byte[] mer_no = new byte[15];//商户编号	 Ans15
    byte[] ter_no = new byte[8];//终端编号	Ans8
    byte[] confirm_code = new byte[2];//标识交易类型：7003：确认通知


    BizObject ter;

    byte[] respcode = new byte[2];

    @Override
    public List<byte[]> getDataList() {
        List<byte[]> list = new ArrayList<>();
        list.add(tpdu);
        list.add(ver);
        list.add(code);
        list.add(ter_sn);
        list.add(confirm_code);
        return list;
    }

    @Override
    public byte[] getRespcode() {
        return this.respcode;
    }

    public ConfirmMsg() {
    }

//    public TerMsg(ByteBuf buf, BizObject biz) {
//        super(buf);
//        buf.readBytes(ter_sn);
//        System.out.println("ter_sn : " + ByteUtils.byte2String(ter_sn));
//        ter = biz;
//        ter.set("ter_sn", ByteUtils.byte2String(ter_sn).trim());
//        ter.set("ter_sn_bt", ter_sn);
//    }

    public ConfirmMsg(ByteBuf buf, byte[] tpdu, byte[] ver, byte[] code) {
        buf.readBytes(ter_sn);
        buf.readBytes(mer_no);
        buf.readBytes(ter_no);
        buf.readBytes(confirm_code);

        this.tpdu = tpdu;
        this.ver = ver;
        this.code = code;

    }

    public byte[] getTerSN() {
        return ter_sn;
    }

//    public BizObject getTer() {
//        return ter;
//    }

    @Override
    public void fill(ByteBuf buf) {

    }

    public void copyInfoTo(ConfirmMsg confirmMsg) {
        confirmMsg.tpdu = this.tpdu;
        confirmMsg.ver = this.ver;
        confirmMsg.code = this.code;
        confirmMsg.ter_sn = this.ter_sn;
        confirmMsg.confirm_code = this.confirm_code;

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
        return ter;
    }
}
