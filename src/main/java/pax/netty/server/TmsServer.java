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
package pax.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.apache.log4j.Logger;
import sand.depot.tool.system.SystemKit;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;


public final class TmsServer {
    static Logger logger = Logger.getLogger(TmsServer.class);

    static final boolean SSL = System.getProperty("ssl") != null;
    //    static final int PORT = Integer.parseInt(System.getProperty("port", "8322"));
//    public static String PORT = "8087";

    public static void start() throws CertificateException, SSLException {
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new TmsServerInitializer(sslCtx));

            b.bind(Integer.parseInt(SystemKit.getCacheParamById("tms_server", "tms_port"))).sync().channel().closeFuture();
//            bossGroup.schedule() //定时任务

//            Thread.sleep(2000);
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
//        TmsServer.start();
//        System.out.println("===is waiting !!! = ");

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        TmsServer.start(bossGroup, workerGroup);
        boolean shutdown = bossGroup.isShutdown();
        boolean shutdown1 = workerGroup.isShutdown();
        System.out.println("shutdown1 = " + shutdown1);
        System.out.println("shutdown = " + shutdown);

        Thread.sleep(1000);


        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        System.out.println("isTerminated = " + bossGroup.isTerminated());
        System.out.println("isShuttingDown = " + bossGroup.isShuttingDown());
        Thread.sleep(5000);
        System.out.println("isShutdown = " + bossGroup.isShutdown());
        System.out.println("isTerminated = " + bossGroup.isTerminated());

        if (bossGroup.isShutdown() || workerGroup.isShutdown()) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            TmsServer.start(bossGroup, workerGroup);
        }

    }

    public static void start(EventLoopGroup bossGroup, EventLoopGroup workerGroup) throws CertificateException, SSLException {
        logger.info("[TmsServer start]PORT=" + SystemKit.getCacheParamById("tms_server", "tms_port"));
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        if (bossGroup == null) bossGroup = new NioEventLoopGroup(1);
        if (workerGroup == null) workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new TmsServerInitializer(sslCtx));

            b.bind(Integer.parseInt(SystemKit.getCacheParamById("tms_server", "tms_port"))).sync().channel().closeFuture();


        } catch (InterruptedException e) {
            e.printStackTrace();
            if (bossGroup != null) bossGroup.shutdownGracefully();
            if (workerGroup != null) workerGroup.shutdownGracefully();
        }

    }
}
