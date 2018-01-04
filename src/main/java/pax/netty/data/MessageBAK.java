package pax.netty.data;

import io.netty.buffer.ByteBuf;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xieke on 2017/4/14.
 */
public abstract class MessageBAK {

    public static final byte STX = 0x02;
    public static final byte ETX = 0x03;

    public TransCode code;

    int len;
    byte[] TPDU = new byte[5];
    byte[] VER = new byte[2];
    byte[] FID = new byte[20];
    byte[] MID = new byte[20];
    byte[] SN = new byte[38];
    byte[] version = new byte[2];  // len 2
    //byte[] DATA=new byte[44];

    //int trans_code; // len 4
//    byte[] return_code=new byte[2];
//   // byte[] RESERVE;
//
//    public void setCode(byte[] t_code){
//        return_code = t_code;
//        code = TransCode.getTransCode(new String(return_code));
//
//    }

    public void copyInfoTo(MessageBAK m) {
        m.TPDU = this.TPDU;
        m.VER = this.VER;
        m.FID = this.FID;
        m.MID = this.MID;
        m.SN = this.SN;
        m.version = this.version;

    }

    public MessageBAK() {

    }


    abstract public List<byte[]> getDataList();

    public MessageBAK(ByteBuf buf) {


        buf.readBytes(TPDU);
        buf.readBytes(VER);
        buf.readBytes(FID);
        buf.readBytes(MID);
        buf.readBytes(SN);
        buf.readBytes(version);

        // len = datalen-80;
        // fill(buf);


    }

    abstract public void fill(ByteBuf buf);

    public  void write(ByteBuf buf) {

        System.out.println("send start!!!");
        buf.writeByte(STX);
        List<byte[]> bl = this.getDataList();

        byte[] etx = {0x03};
        bl.add(0, version);
        bl.add(0, SN);
        bl.add(0, MID);
        bl.add(0, FID);
        bl.add(0, VER);
        bl.add(0, TPDU);


        short s = ByteUtils.getDataLen(bl);

        System.out.println("send len is " + s);
        byte len[] = ByteUtils.bcd2Byte(s);
        //buf.writeShort(s);
        bl.add(0, len);
        bl.add(etx);
        //buf.writeBytes(len);

        //bl.add(0, len);
        //bl.add(0,etx);
        for (byte[] bs : bl) {
            buf.writeBytes(bs);
        }
        byte lrc = ByteUtils.calLRC(bl);
        System.out.println("lrc = " + lrc);

       // buf.writeByte(ETX);
        buf.writeByte(lrc);

        System.out.println("send successful!!! len is " + buf.readableBytes());


    }

    @Override
    public String toString() {
        return "Message{" +
                "code=" + code +
                ", len=" + len +
                ", TPDU=" + Arrays.toString(TPDU) +
                ", VER=" + Arrays.toString(VER) +
                ", FID=" + Arrays.toString(FID) +
                ", MID=" + Arrays.toString(MID) +
                ", SN=" + Arrays.toString(SN) +
                ", version=" + Arrays.toString(version) +
                '}';
    }


}
