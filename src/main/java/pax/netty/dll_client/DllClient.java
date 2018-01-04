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
package pax.netty.dll_client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

import javax.net.ssl.SSLException;


/**
 * Sends a sequence of integers to a {@link } to calculate
 * the factorial of the specified integer.
 */
public final class DllClient {

    static final boolean SSL = System.getProperty("ssl") != null;
    public static final String HOST = System.getProperty("host", "127.0.0.1");
    public static final int PORT = Integer.parseInt(System.getProperty("port", "8321"));
    static final int COUNT = Integer.parseInt(System.getProperty("count", "1000"));


    public static void main(String[] args) throws Exception {
        DllClient dllClient = new DllClient();

        Channel run = dllClient.run(HOST, PORT, null);

        BizObject biz = new BizObject();
        biz.set("name", "dllclient");
        biz.set("msg", "test the main method");

//        JDO jdo = new JDO();
//        jdo.e
        BizObject ter_type = QueryFactory.getInstance("ter_type").getByID("48d3567eea1f4378956a9eab4d7402as");
        System.out.println("ter_type = " + ter_type);
        run.writeAndFlush(biz);

    }

    public Channel run(String host, int port, Channel channel) throws SSLException, InterruptedException {
        System.out.println("start dll client !!! server host:" + host + ",port:" + port);
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
                    .handler(new DllClientInitializer(sslCtx, channel));

            // Make a new connection.
            ChannelFuture f = b.connect(host, port).sync();


            return f.channel();
//            f.channel().writeAndFlush(b);
            // Get the handler instance to retrieve the answer.
//            TmsClientHandler handler =
//                (TmsClientHandler) f.channel().pipeline().last();

            // Print out the answer.
            //  System.err.format("Factorial of %,d is: %,d", COUNT, handler.getFactorial());
        } finally {
//            System.out.println("shutdown client ");
            // group.shutdownGracefully();
        }
    }
}
