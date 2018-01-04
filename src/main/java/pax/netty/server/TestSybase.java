package pax.netty.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Author : wangtao
 * @Description :
 * @Date : 2017/10/26 18:22
 */
public class TestSybase {
    public static void main(String[] args) {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String url = "jdbc:jtds:sybase://192.168.1.253:5000/tms";
            Properties props = System.getProperties();
            props.put("user", "sa");
            props.put("password", "suse123456");
            conn = DriverManager.getConnection(url, props);
            statement = conn.createStatement();
            rs = statement.executeQuery("SELECT * FROM dbo.app");
            while (rs.next()) {
                System.out.println(rs.getString("app_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
