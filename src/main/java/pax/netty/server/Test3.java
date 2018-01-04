package pax.netty.server;

import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.util.encoders.Hex;
import org.jpos.iso.ISOUtil;
import org.junit.Test;
import pax.netty.util.ByteUtils;
import pax.util.AsyncConnector;
import pax.utils.TcpUtils;
import sand.utils.SSLService;
import tool.crypto.Crypto;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by want on 2017/5/9.
 */
public class Test3 {
    @org.junit.Test
    public void as() {

        byte[] nm = {0x60, 0x00, 0x36, 0x00, 0x00, 0x30, 0x31, 0x50, 0x41, 0x58, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x50,
                0x41, 0x58, 0x20, 0x53, 0x35, 0x30, 0x30, 0x20, 0x45, 0x46, 0x54, 0x2D, 0x50, 0x4F,
                0x53, 0x20, 0x20, 0x20, 0x20, 0x36, 0x35, 0x30, 0x30, 0x30, 0x35, 0x36, 0x32, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x30, 0x32, 0x30, 0x30, 0x03};


        List<byte[]> bl = new ArrayList<>();

        bl.add(0, nm);


        byte b = ByteUtils.calLRC(bl);
        System.out.println(b);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String date = dateFormat.format(new Date());
        String[] date_str = date.split(" ");
        System.out.println("date_str0 = " + date_str[0]);
        System.out.println("date_str1 = " + date_str[1]);


    }

    @Test
    public void sd() throws UnknownHostException {
        Map<String, Object> ipMacInfo = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface
                        .getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    String name = networkInterface.getDisplayName();
                    System.out.println("name = " + name);
                    if (inetAddress.isSiteLocalAddress() && !(name.contains("Adapter")
                            || name.contains("Virtual") || name.contains("VMnet") || name.contains("#"))) {
                        String hostAddress = inetAddress.getHostAddress();
                        System.out.println("hostAddress = " + hostAddress);
//                    ipMacInfo = pickInetAddress(inetAddress, networkInterface);
//                    if (ipMacInfo != null) {
//                        System.out.println("ipMacInfo = " + ipMacInfo);
//                        Object ip = ipMacInfo.get("ip");
//                        System.out.println("ip = " + ip);
//                    }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sf() {
        String s = ISOUtil.byte2hex(new byte[]{0x01});
        System.out.println("s = " + s);
        String port = System.getProperty("port");
        System.out.println("port = " + port);
    }

    private static Map<String, Object> pickInetAddress(InetAddress inetAddress,
                                                       NetworkInterface ni) {
        try {
            String name = ni.getDisplayName();
            if (name.contains("Adapter")
                    || name.contains("Virtual") || name.contains("VMnet") || name.contains("#")) {
                return null;
            }
            if (ni.isVirtual() || !ni.isUp() || !ni.supportsMulticast()) {
                return null;
            }

            if (inetAddress.isSiteLocalAddress()) {
                Formatter formatter = new Formatter();

                formatter.close();
                Map<String, Object> ipMacInfo = new HashMap<String, Object>();
                ipMacInfo.put("hostname", inetAddress.getHostName()); //系统当前hostname
                ipMacInfo.put("ip", inetAddress.getHostAddress()); //ip地址
                ipMacInfo.put("ipnet", inetAddressTypeName(inetAddress)); //网络类型
                ipMacInfo.put("os", System.getProperty("os.name")); //系统名称

                ipMacInfo.put("cpu-arch", System.getProperty("os.arch")); //cpu架构
                ipMacInfo.put("network-arch", ni.getDisplayName()); //网卡名称
                return ipMacInfo;
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String inetAddressTypeName(InetAddress inetAddress) {
        return (inetAddress instanceof Inet4Address) ? "ipv4" : "ipv6";
    }

    @Test
    public void ff() {
//        String realPath = "C:\\project\\tms\\src\\main\\webapp\\";
//        String parent = new File(realPath).getParentFile().getParentFile().getParent();
//        System.out.println("parent = " + parent);
//
//        String dll_path = "/home/tms/app/libLandiTMS_ylV1.0.so";
//        String substring = dll_path.substring(dll_path.lastIndexOf("/") + 1, dll_path.lastIndexOf(".so"));
//        System.out.println("substring = " + substring);

        String a = "PAX S500 EFT-POS     65000562";
        String[] split = a.split("  ");
        for (int i = 0; i < split.length; i++) {
            System.out.println("split = " + i + " --> " + split[i] + "=" + split[i].length());
        }
        System.out.println("split = " + split.length);

        System.out.println("  ".length());

        String b = "PAX-S500-EFT-POS-----65000562";
        String[] splitb = b.split("--");
        for (int i = 0; i < splitb.length; i++) {
            System.out.println("split = " + i + " --> " + splitb[i] + "=" + splitb[i].length());
        }
    }

    @Test
    public void ffss() throws IOException, ClassNotFoundException {


        //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
        File f = new File("C:\\Users\\want\\Desktop\\TMS资料\\PaxRule.jar");
        URL url1 = f.toURI().toURL();
        URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());


        //通过jarFile和JarEntry得到所有的类
        JarFile jar = new JarFile(f);
        //返回zip文件条目的枚举
        Enumeration<JarEntry> enumFiles = jar.entries();
        JarEntry entry;
        //测试此枚举是否包含更多的元素
        while (enumFiles.hasMoreElements()) {
            entry = (JarEntry) enumFiles.nextElement();
            if (entry.getName().indexOf("META-INF") < 0) {
                String classFullName = entry.getName();
                if (!classFullName.endsWith(".class")) {
                    classFullName = classFullName.substring(0, classFullName.length() - 1);
                } else {
                    //去掉后缀.class
                    String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
                    Class<?> myclass = myClassLoader.loadClass(className);
                    //打印类名
                    System.out.println("*****************************");
                    System.out.println("全类名:" + className);

                    //得到类中包含的属性
                    Method[] methods = myclass.getMethods();
                    for (Method method : methods) {
                        String methodName = method.getName();
                        System.out.println("方法名称:" + methodName);
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        for (Class<?> clas : parameterTypes) {
                            // String parameterName = clas.getName();
                            String parameterName = clas.getSimpleName();
                            System.out.println("参数类型:" + parameterName);
                        }
                        System.out.println("==========================");
                    }
                }
            }
        }
    }

    @Test
    public void fgss() {
        Crypto cy = new Crypto();
        String tms = cy.des("tms728");
        System.out.println("tms = " + tms);

        String s = "020001";
        byte[] decode = Hex.decode(s);
//        ByteUtils.printHexString(decode);
        String s1 = HexUtils.toHexString(decode);
        System.out.println("s1 = " + s1);
    }


    @Test
    public void aassa() throws UnsupportedEncodingException {
        byte[] aa = {0x16, 0x03, 0x03, 0x00, 0x35, 0x01, 0x00, 0x00, 0x31, 0x03, 0x03
                , 0x00, 0x00, (byte) 0xe2, 0x4a, (byte) 0xbc, 0x0a, 0x21, 0x0e, (byte) 0xc2, 0x26, 0x5f
                , 0x34, (byte) 0x9b, (byte) 0xdd, (byte) 0x88, 0x04, (byte) 0xc7, (byte) 0xfc, (byte) 0x95, (byte) 0xcf, 0x37, 0x63
                , (byte) 0xfa, (byte) 0xb7, (byte) 0xcd, (byte) 0xea, 0x36, (byte) 0xda, (byte) 0xd7, 0x0a, (byte) 0xe8, (byte) 0xba, 0x00
                , 0x00, 0x0a, 0x00, 0x3d, 0x00, 0x3c, 0x00, 0x0a, 0x00, 0x2f, 0x00
                , 0x05, 0x01, 0x00, 0x15, 0x03, 0x03, 0x00, 0x02, 0x01, 0x00};
        String gbk = new String(aa, "gbk");
        int i = gbk.hashCode();
        System.out.println("i = " + i);

        System.out.println("gbk = " + gbk);
    }

    @Test
    public void asaaa() {

        String aa = "aseq";
        byte[] bytes = aa.getBytes();
        String s = ISOUtil.hexString(bytes);
        System.out.println("s = " + s);
        byte[] bytes1 = ISOUtil.hex2byte(s);
        String ss = new String(bytes1);
        System.out.println("ss = " + ss);
    }

    @Test
    public void test16Baowen() throws IOException, InterruptedException {
        String trade_server_url = "http://113.204.232.6:30049";
        AsyncConnector connector = AsyncConnector.getInstance();
        StringBuffer content = new StringBuffer();
        byte[] aa = {0x00, 0x31
                , 0x60, 0x00, 0x30, 0x00, 0x00
                , 0x30, 0x30, 0x37, 0x30, 0x30
                , 0x31, 0x7C, 0x43, 0x50, 0x4F
                , 0x53, 0x7C, 0x74, 0x65, 0x73
                , 0x74, 0x31, 0x32, 0x33, 0x34
                , 0x35, 0x36, 0x37, 0x38, 0x39
                , 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C};
        String gbk = new String(aa, "gbk");
//        SSLService sslService = new SSLService();
//        Object post = sslService.sendRequest(trade_server_url, gbk);
//        System.out.println("post = " + post);
//        System.out.println("Trade Server result is : " + post);

        URL url = new URL("http://113.204.232.6:30049");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("encoding", "gbk");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);

        bw.write(gbk);
        bw.flush();

        Thread.sleep(10000);

        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        //关闭资源
        System.out.println(builder);
    }

    //服务器地址
    public static final String IP_ADDR = "113.204.232.6";
    //服务器端口号
    public static final int PORT = 30049;

    @Test
    public void asasa() {
        System.out.println("客户端启动...");
        System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");
        while (true) {
            Socket socket = null;
            try {
                //创建一个流套接字并将其连接到指定主机上的指定端口号
                socket = new Socket(IP_ADDR, PORT);

                //读取服务器端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                //向服务器端发送数据
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                System.out.print("请输入: \t");
//                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
                byte[] aa = {0x00, 0x31
                        , 0x60, 0x00, 0x30, 0x00, 0x00
                        , 0x30, 0x30, 0x37, 0x30, 0x30
                        , 0x31, 0x7C, 0x43, 0x50, 0x4F
                        , 0x53, 0x7C, 0x74, 0x65, 0x73
                        , 0x74, 0x31, 0x32, 0x33, 0x34
                        , 0x35, 0x36, 0x37, 0x38, 0x39
                        , 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C};


                out.write(aa);

                String ret = input.readUTF();
                System.out.println("ret.length() = " + ret.length());
                System.out.println("服务器端返回过来的是: " + ret);
                // 如接收到 "OK" 则断开连接
                if (ret.endsWith("00|")) {
                    System.out.println("客户端将关闭连接");
                    Thread.sleep(500);
                    break;
                }

                out.close();
                input.close();
            } catch (Exception e) {
                System.out.println("客户端异常:" + e.getMessage());
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
    }

    @Test
    public void test() {
        byte[] bt_len = {0x00, (byte) 0x96};
        int i = 150;
        byte[] len = ByteUtils.intToBytes(i);
        String s = ISOUtil.hexString(len);
        System.out.println("s = " + s);
    }

    @Test
    public void testTime() {
        String closeddate = "2017-08-09 16:37:11.0";
        closeddate = closeddate.substring(0, closeddate.indexOf("."));
        System.out.println("closeddate = " + closeddate);
        String r_closeddate = closeddate.replaceAll("-| |:|", "");
        System.out.println("r_closeddate = " + r_closeddate);
        String substring = r_closeddate.substring(0, 8);
        System.out.println("substring = " + substring);


    }

    /**
     * 将字符串的tpdu转换成byte数组
     */
    @Test
    public void dsda() {
        String tpdu = "6000300033";
        byte[] bytes = ByteUtils.hex2Bytes(tpdu);
        System.out.println("bytes = " + ISOUtil.hexString(bytes));
    }
}


