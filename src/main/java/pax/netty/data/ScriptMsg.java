package pax.netty.data;

import io.netty.buffer.ByteBuf;
import pax.netty.util.TransCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/7/21.
 */
public class ScriptMsg extends Message {

    byte[] tpdu = new byte[5];
    byte[] ver = new byte[2];
    byte[] code = new byte[4];


    byte[] ter_sn = new byte[50]; //设备序列号	ans50

    byte[] mer_no = new byte[15];//商户编号	 Ans15
    byte[] ter_no = new byte[8];//终端编号	Ans8

    byte[] sp_itpt_ver = new byte[2];   //    脚本解析器版本号	An2
    byte[] sp_index = new byte[10];   //    脚本下载索引	Ans10
    byte[] dl_app_mark = new byte[5];   //    下载应用标识号	Ans5
    byte[] hd_mark = new byte[8];  //    硬件标识信息	ANS8


    byte[] respcode = new byte[2];
    byte[] sp_data = new byte[1024];//    脚本参数

    @Override
    public List<byte[]> getDataList() {
        List<byte[]> list = new ArrayList<>();
        list.add(tpdu);
        list.add(ver);
        list.add(code);
        list.add(ter_sn);


        list.add(respcode);
        list.add(sp_index);
        list.add(dl_app_mark);



        list.add(sp_data);



        return list;
    }

    @Override
    public void fill(ByteBuf buf) {

    }

    public void copyInfoTo(ScriptMsg scriptMsg) {
        scriptMsg.tpdu = this.tpdu;
        scriptMsg.ver = this.ver;
        scriptMsg.code = this.code;
        scriptMsg.ter_sn = this.ter_sn;

    }

    public ScriptMsg(ByteBuf buf, byte[] tpdu, byte[] ver, byte[] code) {
        buf.readBytes(ter_sn);
        buf.readBytes(mer_no);
        buf.readBytes(ter_no);
        buf.readBytes(sp_itpt_ver);
        buf.readBytes(sp_index);
        buf.readBytes(dl_app_mark);
        buf.readBytes(hd_mark);

        this.tpdu = tpdu;
        this.ver = ver;
        this.code = code;

    }

    public void setRespcode(TransCode resp) {
        this.respcode = TransCode.getCodeByte(resp);
    }


    public ScriptMsg() {
    }

    public ScriptMsg(ByteBuf buf) {
        super(buf);
    }
}
