package pax.netty.data;

import io.netty.buffer.ByteBuf;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.formula.functions.T;
import org.jpos.iso.ISOUtil;
import pax.netty.util.ANSIMacUtils;
import pax.netty.util.ByteUtils;
import pax.netty.util.DESCoder;
import pax.netty.util.TransCode;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xieke on 2017/4/17.
 */
public class Request extends  Message {



    byte T_DATA[] = new byte[10];
    byte reserve[]=new byte[34];
    public byte trans_code[]=new byte[4];
    byte end_reserve[] =new byte[20];



    /************下面属于session变量**************/
    public byte[] DWK; //工作秘钥
    public byte DWK_1[]; //工作秘钥
    public String DMK="Q5ihmKS1GZ0="; //主秘钥
    public byte TAC_1[];
    public byte TAC_0[];
    public byte SAC_0[];
    public byte SAC_1[];
    //code = TransCode.get

    @Override
    public List<byte[]> getDataList() {

        List<byte[]> list = new ArrayList<>();
        list.add(T_DATA);
        list.add(reserve);
        list.add(trans_code);
        list.add(end_reserve);
        return list;
    }


    public void setCode(byte[] t_code){
        trans_code = t_code;
        String scode=new String(trans_code);
        System.out.println("code is "+scode);
//        code = TransCode.getTransCode(scode);

    }
    public Request(byte[] t_code){
        T_DATA = ByteUtils.createTSDATA();
        this.setCode(t_code);

//        try {
//            //DMK=DESCoder.initKey();
//            DWK= Base64.decodeBase64(DESCoder.initKey());
//            System.out.println("dwk len is "+DWK.length);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public Request(ByteBuf buf){

        super(buf);
        this.fill(buf);
        //System.out.println("t data is "+T_DATA);
        try {
            //DMK=DESCoder.initKey();
            DWK= Base64.decodeBase64(DESCoder.initKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fill(ByteBuf buf){
//        T_DATA= new byte[10];
//         T_DATA = new byte[10];
//         reserve=new byte[34];
//          trans_code=new byte[4];
         //end_reserve =new byte[20];

        System.out.println("in request fill ,readable is "+buf.readableBytes()+"   "+T_DATA);
       // buf.read
        buf.readBytes(T_DATA);
        buf.readBytes(reserve);
        buf.readBytes(trans_code);
        this.setCode(trans_code);


       // len = len -54;
      //  logger.info


        end_reserve = new byte[buf.readableBytes()-2];
        System.out.println("end reserve len is "+end_reserve.length);
        buf.readBytes(end_reserve);

    }


    public void calSAC0(byte[] sdata){
        try {

            SAC_0=ANSIMacUtils.calculateANSIX9_9MAC(DWK,sdata);
            //TAC_1 = DESCoder.encrypt(T_DATA,DWK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void calSAC1(byte[] sdata){
        try {

            SAC_1=ANSIMacUtils.calculateANSIX9_9MAC(DWK,sdata);
            //TAC_1 = DESCoder.encrypt(T_DATA,DWK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void calDWK(byte dwk1[]){
//        byte dwk_1[] = new byte[8];
//
//        for(int i=0;i<8;i++){
//            dwk_1[i]=dwk1[i];
//        }

        try {
            System.out.println(ISOUtil.hexString(dwk1));
            //System.out.println(Integer.toh);
            DWK=DESCoder.decrypt(dwk1,DMK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void calTAC0(){
        try {

            System.out.println("dwk "+DWK+"  T_data "+ T_DATA);

            TAC_0=ANSIMacUtils.calculateANSIX9_9MAC(DWK,T_DATA);

            //TAC_1 = DESCoder.encrypt(T_DATA,DWK);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void calTAC1(){
        try {

            TAC_1=ANSIMacUtils.calculateANSIX9_9MAC(DWK,T_DATA);
            //TAC_1 = DESCoder.encrypt(T_DATA,DWK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void calDWK1(){
        try {


            //8位
            //这里按理说是加密后 dwk_1是 8位，但这里是16位，可能和padding模式有关系
            DWK_1 = DESCoder.encrypt(DWK,DMK);

            System.out.println("dwk 1 is ");
            ByteUtils.printHexString(DWK_1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  void main(String args[]){

        try {
            byte [] DWK= Base64.decodeBase64(DESCoder.initKey());
            Request r = new Request("2011".getBytes());

            r.calDWK1();
            ByteUtils.printHexString(r.DWK);
            ByteUtils.printHexString(r.DWK_1);


            r.calDWK(r.DWK_1);
            ByteUtils.printHexString(r.DWK);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Request{" +
                "T_DATA=" + Arrays.toString(T_DATA) +
                ", reserve=" + Arrays.toString(reserve) +
                ", trans_code=" + Arrays.toString(trans_code) +
                ", end_reserve=" + Arrays.toString(end_reserve) +
                ", DWK=" + Arrays.toString(DWK) +
                ", DWK_1=" + Arrays.toString(DWK_1) +
                ", DMK='" + DMK + '\'' +
                ", TAC_1=" + Arrays.toString(TAC_1) +
                ", TAC_0=" + Arrays.toString(TAC_0) +
                ", SAC_0=" + Arrays.toString(SAC_0) +
                ", SAC_1=" + Arrays.toString(SAC_1) +
                '}';
    }
}
