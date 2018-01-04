package pax.netty.data;

import io.netty.buffer.ByteBuf;
import pax.netty.util.ByteUtils;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xieke on 2017/4/17.
 */
public class Remote_download_out extends  Message {


    byte encrypt_type[]={0x00};  //表示 des加密方式，也可能是 0x20
    public byte dwk_1[]=new byte[16];
    public byte tac_1[] = new byte[8];
    public byte s_data[]=new byte[10];

    byte phase[]= {0x00,0x08,0x00,0x00};
    byte return_code[] = {0x00,0x00};

    public Remote_download_out(ByteBuf buf){
        super(buf);
        fill(buf);
    }

    public Remote_download_out(byte TAC_1[], byte DWK_1[]){

        System.out.println("tac1 len "+TAC_1.length+"  dwk1 len "+DWK_1.length);
        this.tac_1 = TAC_1;

//        //取前八个字节
//        for(int i=0;i<8;i++){
//            dwk_1[i]=DWK_1[i];
//        }

        this.dwk_1 = DWK_1;
        s_data = ByteUtils.createTSDATA();
    }


    public Remote_download_out() {

    }

    @Override
    public List<byte[]> getDataList() {

//        encrypt_type=new byte[1];;  //表示 des加密方式，也可能是 0x20
//        dwk_1=new byte[16];
//        tac_1 = new byte[8];
//        s_data=new byte[10];
//
//        phase= new byte[4];
//        return_code = new byte[2];


        List<byte[]> list = new ArrayList<>();
        list.add(encrypt_type);
        list.add(dwk_1);
        list.add(tac_1);
        list.add(s_data);
        list.add(phase);
        list.add(return_code);
        return list;
    }


    public void fill(ByteBuf buf){


        buf.readBytes(encrypt_type);
        buf.readBytes(dwk_1);
        buf.readBytes(tac_1);
        buf.readBytes(s_data);
        buf.readBytes(phase);
        buf.readBytes(return_code);


    }

    @Override
    public String toString() {
        return "Remote_download_out{" +
                "encrypt_type=" + Arrays.toString(encrypt_type) +
                ", dwk_1=" + Arrays.toString(dwk_1) +
                ", tac_1=" + Arrays.toString(tac_1) +
                ", s_data=" + Arrays.toString(s_data) +
                ", phase=" + Arrays.toString(phase) +
                ", return_code=" + Arrays.toString(return_code) +
                '}';
    }
}
