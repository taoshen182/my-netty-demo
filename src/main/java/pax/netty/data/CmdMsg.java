package pax.netty.data;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.log4j.Logger;
import org.junit.Test;
import pax.netty.coder.MessageDecoder;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;
import tool.dao.BizObject;

import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.formula.functions.TextFunction.MID;
import static pax.netty.util.ByteUtils.getDataLen;

/**
 * Created by want on 2017/5/17.
 */
public class CmdMsg extends Message {
    byte[] tpdu = new byte[5];

    String dllMsg;

    byte[] respcode = new byte[2];

    byte[] ret_data;

    public void setRespcode(TransCode respCode) {
        this.respcode = TransCode.getCodeByte(respCode);
    }

    @Override
    public byte[] getRespcode() {
        return this.respcode;
    }


    public void setRetData(byte[] ret_data) {
        this.ret_data = ret_data;
    }


    @Override
    public List<byte[]> getDataList() {
        List<byte[]> list = new ArrayList<>();
        list.add(tpdu);
        list.add(ret_data);
        return list;
    }

    @Override
    public void fill(ByteBuf buf) {

    }

    public CmdMsg() {
    }


    public CmdMsg(byte[] tpdu, String dllMsg) {
        this.tpdu = tpdu;
        this.dllMsg = dllMsg;
    }


    public String getDllMsg() {
        return dllMsg;
    }


}
