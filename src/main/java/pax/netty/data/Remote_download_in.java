package pax.netty.data;

import io.netty.buffer.ByteBuf;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xieke on 2017/4/17.
 */
public class Remote_download_in extends  Message {


    public byte sac_1[]= new byte[8];
    byte phase[]= new byte[4];
    public byte[] return_code = new byte[2];

    @Override
    public List<byte[]> getDataList() {
        List<byte[]> list = new ArrayList<>();
        list.add(sac_1);
        list.add(phase);
        list.add(return_code);

        return list;

    }

    public Remote_download_in(ByteBuf buf){
        super(buf);
        fill(buf);
    }
    public void setCode(byte[] t_code){
        return_code= t_code;
//        code = TransCode.getTransCode(new String(return_code));

    }
    public Remote_download_in(byte[] t_code){
        return_code = t_code;
       // T_DATA = ByteUtils.createTSDATA();

        this.setCode(t_code);

//        try {
//            //DMK=DESCoder.initKey();
//            DWK= Base64.decodeBase64(DESCoder.initKey());
//            System.out.println("dwk len is "+DWK.length);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

//    public Remote_download_in(byte sac1[]){
//        sac_1=sac1;
//
//
//    }


    public void fill(ByteBuf buf){
        buf.readBytes(sac_1);
        buf.readBytes(phase);
        buf.readBytes(return_code);


    }


    @Override
    public String toString() {
        return "Remote_download_in{" +
                "sac_1=" + Arrays.toString(sac_1) +
                ", phase=" + Arrays.toString(phase) +
                ", return_code=" + Arrays.toString(return_code) +
                '}';
    }
}
