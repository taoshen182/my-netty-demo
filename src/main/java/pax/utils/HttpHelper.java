package pax.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jpos.iso.ISOUtil;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author : wangtao
 * @Description :
 * @Date : 2017/10/17 14:30
 */
public class HttpHelper {
    CloseableHttpClient m_HttpClient;

    public HttpHelper() {
        m_HttpClient = HttpClients.createDefault();
    }

    // send bytes and recv bytes
    public byte[] post(String url, byte[] bytes, String contentType) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new ByteArrayEntity(bytes));
        if (contentType != null)
            httpPost.setHeader("Content-type", contentType);
        CloseableHttpResponse httpResponse = m_HttpClient.execute(httpPost);
        try {
            HttpEntity entityResponse = httpResponse.getEntity();
            int contentLength = (int) entityResponse.getContentLength();
            if (contentLength <= 0)
                throw new IOException("No response");
            byte[] respBuffer = new byte[contentLength];
            if (entityResponse.getContent().read(respBuffer) != respBuffer.length)
                throw new IOException("Read response buffer error");
            return respBuffer;
        } finally {
            httpResponse.close();
        }
    }

    public byte[] post(String url, byte[] bytes) throws IOException {
        return post(url, bytes, null);
    }

    public String postXml(String url, String str) throws IOException {
        byte[] reqBuffer = str.getBytes(Charset.forName("UTF-8"));
        byte[] respBuffer = post(url, reqBuffer, "application/xml; charset=UTF-8");
        String resp = new String(respBuffer, Charset.forName("UTF-8"));
        return resp;
    }

    public static void main(String[] args) throws IOException {
        HttpHelper httpHelper = new HttpHelper();
        String url = "http://113.204.232.6:30049";
        byte[] aa = {0x00, 0x31
                , 0x60, 0x00, 0x30, 0x00, 0x03
                , 0x30, 0x30, 0x37, 0x30, 0x30
                , 0x31, 0x7C, 0x43, 0x50, 0x4F
                , 0x53, 0x7C, 0x74, 0x65, 0x73
                , 0x74, 0x31, 0x32, 0x33, 0x34
                , 0x35, 0x36, 0x37, 0x38, 0x39
                , 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C};
        byte[] post = httpHelper.post(url, aa);
        String s = ISOUtil.hexString(post);
        System.out.println("s = " + s);
    }
}
