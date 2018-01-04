package pax.netty.data;

import io.netty.buffer.ByteBuf;
import pax.netty.util.TransCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/7/20.
 */
public class IssuAppMsg extends Message {

    byte[] tpdu = new byte[5];
    byte[] ver = new byte[2];
    byte[] code = new byte[4];

    byte[] ter_sn = new byte[50];//设备序列号	ans50	不足右补0x20
    byte[] app_ver = new byte[50];//    应用版本号	ans50	首次请求可为全0x20，不足右补0x20


    /*应答数据*/
    byte[] respcode = new byte[2];// 响应码	ans2	参见附录C
    byte[] app_param = new byte[512];// 应用参数	Ans…512(LLLVAR)	1、应用版本号：ans..50(LLVAR),长度（2B）＋应用版本号（厂商自定义，最大50B） 2、应用参数：ans…457(LLLVAR),长度（3B）＋应用参数信息（最大457B）
    byte[] p_mark = new byte[1];// 后续应用参数标识	N1	若存在后续应用参数待下载，则为1，否则为0。若为1，终端发起应用信息下发请求报文
    byte[] next_ver = new byte[50];// 下一应用版本号	ans50	若后续应用参数标识为1时出现，不足右补0x20。


    @Override
    public List<byte[]> getDataList() {
        List<byte[]> list = new ArrayList<>();
        list.add(tpdu);
        list.add(ver);
        list.add(code);
        list.add(ter_sn);
//        list.add(rsp_code);
//        list.add(app_param);
//        list.add(p_mark);
//        list.add(next_ver);
        return list;
    }

    @Override
    public void fill(ByteBuf buf) {

    }

    public IssuAppMsg(byte[] rsp_code, byte[] app_param, byte[] p_mark, byte[] next_ver) {
//        this.rsp_code = rsp_code;
        this.app_param = app_param;
        this.p_mark = p_mark;
        this.next_ver = next_ver;
    }

    public IssuAppMsg(byte[] rsp_code, byte[] app_param, byte[] p_mark) {
//        this.rsp_code = rsp_code;
        this.app_param = app_param;
        this.p_mark = p_mark;
    }

    public IssuAppMsg() {
    }

    public void copyInfoTo(IssuAppMsg issuAppMsg) {
        issuAppMsg.tpdu = this.tpdu;
        issuAppMsg.ver = this.ver;
        issuAppMsg.code = this.code;
        issuAppMsg.ter_sn = this.ter_sn;

    }

    @Override
    public byte[] getRespcode() {
        return this.respcode;
    }

    public void setRespcode(TransCode resp) {
        this.respcode = TransCode.getCodeByte(resp);
    }

    public IssuAppMsg(ByteBuf buf, byte[] tpdu, byte[] ver, byte[] code) {
        buf.readBytes(ter_sn);
        buf.readBytes(app_ver);

        this.tpdu = tpdu;
        this.ver = ver;
        this.code = code;
    }
}
