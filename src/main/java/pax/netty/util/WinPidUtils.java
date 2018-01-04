package pax.netty.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by want on 2017/5/18.
 */
public class WinPidUtils {
    public static int getPidByPort() throws IOException {
        int pid = -1;
        InputStream ins = null;
        String[] cmd2 = new String[]{"cmd.exe", "/C", "C:\\Users\\want\\Desktop\\BAT\\testPid.bat"};  // 命令
        try {
            Process process = Runtime.getRuntime().exec(cmd2);
            ins = process.getInputStream();  // 获取执行cmd命令后的信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // 输出
                pid = Integer.parseInt(line);
                break;
            }
            int exitValue = process.waitFor();
            System.out.println("返回值：" + exitValue);
            process.getOutputStream().close();  // 不要忘记了一定要关

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return pid;
    }
}
