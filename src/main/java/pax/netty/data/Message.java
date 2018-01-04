package pax.netty.data;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOUtil;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.poi.ss.formula.functions.TextFunction.MID;
import static pax.netty.util.ByteUtils.bcd2Byte;

/**
 * Created by xieke on 2017/4/14.
 */
public abstract class Message {
    static Logger logger = Logger.getLogger(Message.class);

    public static final byte STX = 0x02;
    public static final byte ETX = 0x03;

    public Message() {

    }

    abstract public List<byte[]> getDataList();

    //有需要就重写
    public byte[] getRespcode() {
        return null;
    }

    ;

    public Message(ByteBuf buf) {

    }

    abstract public void fill(ByteBuf buf);

    public void write(ByteBuf buf) {
        logger.info("send start!!!");
        List<byte[]> bl = this.getDataList();
        logger.info("bl = " + bl.size());
        short s = ByteUtils.getDataLen(bl);
        //获取返回码
        byte[] respcode = getRespcode();
        if (respcode != null) {
            s += respcode.length;
        }
        logger.info("send len is " + s);
        byte len[] = ByteUtils.intToBytes(s);
        bl.add(0, len);
        for (byte[] bs : bl) {
            buf.writeBytes(bs);
        }
        if (respcode != null) {
            buf.writeBytes(respcode);
        }
        logger.info(String.format("[Message write]send successful!!! len is : %s ,buf is : %s ", buf.readableBytes(), ByteBufUtil.hexDump(buf)));
    }
}
