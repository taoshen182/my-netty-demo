/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package pax.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sends a sequence of integers to a {@link } to calculate
 * the factorial of the specified integer.
 */
public final class TmsClient {
    static Logger logger = Logger.getLogger(TmsClient.class);

    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "113.204.232.6");
    static final int PORT = Integer.parseInt(System.getProperty("port", "30049"));
//    static final int COUNT = Integer.parseInt(System.getProperty("count", "1000"));


    public static void main(String[] args) throws Exception {
        List<byte[]> dataList = new ArrayList<>();
        byte[] data1 = {0x00, 0x31, 0x66, 0x00, 0x30, 0x00, 0x60, 0x30, 0x30, 0x37, 0x30, 0x30
                , 0x31, 0x7C, 0x43, 0x50, 0x4F, 0x53, 0x7C, 0x74, 0x65, 0x73, 0x74, 0x31, 0x32, 0x33, 0x34
                , 0x35, 0x36, 0x37, 0x38, 0x39, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C,
                0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C};
        byte[] data2 = {0x00, 0x31, 0x65, 0x00, 0x30, 0x00, 0x60, 0x30, 0x30, 0x37, 0x30, 0x30
                , 0x31, 0x7C, 0x43, 0x50, 0x4F, 0x53, 0x7C, 0x74, 0x65, 0x73, 0x74, 0x31, 0x32, 0x33, 0x34
                , 0x35, 0x36, 0x37, 0x38, 0x39, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C,
                0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C};
        byte[] data3 = {0x00, 0x31, 0x62, 0x00, 0x30, 0x00, 0x60, 0x30, 0x30, 0x37, 0x30, 0x30
                , 0x31, 0x7C, 0x43, 0x50, 0x4F, 0x53, 0x7C, 0x74, 0x65, 0x73, 0x74, 0x31, 0x32, 0x33, 0x34
                , 0x35, 0x36, 0x37, 0x38, 0x39, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C,
                0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C};
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        startClient("113.204.232.6", 30049, dataList);

    }

    public static void startClient(String host, int port, List<byte[]> dataList) throws SSLException, InterruptedException {
        logger.info("[TmsClient]start a client to connect " + host + ":" + port + " ... ");
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new TmsClientInitializer(sslCtx, dataList));

            // Make a new connection.
            ChannelFuture f = b.connect(HOST, PORT).sync();
            Thread.sleep(1000);
//            f.channel().writeAndFlush(b);
            // Get the handler instance to retrieve the answer.
//            TmsClientHandler handler =
//                (TmsClientHandler) f.channel().pipeline().last();

            // Print out the answer.
            //  System.err.format("Factorial of %,d is: %,d", COUNT, handler.getFactorial());
        } finally {
            System.out.println("[TmsClient]shutdown client ");
            group.shutdownGracefully();
        }
    }
}
