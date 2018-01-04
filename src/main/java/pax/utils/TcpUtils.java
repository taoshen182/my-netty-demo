package pax.utils;

import sand.depot.tool.system.ControllableException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by want on 2017/10/16.
 */
public class TcpUtils {
    //服务器地址
    public static final String IP_ADDR = "113.204.232.6";
    //服务器端口号
    public static final int PORT = 30049;

    public static void main(String[] args) {

        byte[] aa = {0x00, 0x31
                , 0x60, 0x00, 0x30, 0x00, 0x03
                , 0x30, 0x30, 0x37, 0x30, 0x30
                , 0x31, 0x7C, 0x43, 0x50, 0x4F
                , 0x53, 0x7C, 0x74, 0x65, 0x73
                , 0x74, 0x31, 0x32, 0x33, 0x34
                , 0x35, 0x36, 0x37, 0x38, 0x39
                , 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C};

        String request = request(aa, 6);
        System.out.println("request = " + request);
    }

    public static String request(byte[] data, int endMark) {
        if (endMark < 1) {
            throw new ControllableException("结束标志\"|\"的个数必须大于零");
        }
        System.out.println("客户端启动...");
        String ret = "no data";
        while (true) {
            Socket socket = null;
            try {
                //创建一个流套接字并将其连接到指定主机上的指定端口号
                socket = new Socket(IP_ADDR, PORT);

                //读取服务器端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                //向服务器端发送数据
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                out.write(data);
                Thread.sleep(500);
                ret = input.readUTF();
                System.out.println("服务器端返回过来的是: " + ret);
                // 如接收完毕
                String[] split = ret.split("\\|");
                System.out.println(" split = " + split.length + ", endMark = " + endMark);
                if (endMark == split.length) {
                    System.out.println("数据接收完毕，客户端将关闭连接");
                    break;
                }
                out.close();
                input.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "error is : " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "error is : " + e.getMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "error is : " + e.getMessage();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        socket = null;
                        System.out.println("客户端 finally 异常:" + e.getMessage());
                    }
                }
            }
        }
        return ret;
    }
}
