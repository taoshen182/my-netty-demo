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

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import pax.netty.coder.BizMsgEncoder;
import pax.netty.coder.BizMsgDecoder;

/**
 * Creates a newly configured {@link ChannelPipeline} for a client-side channel.
 */
public class DllClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    //TMS 和 POS连接的通道
    private Channel tms_channel;

    public DllClientInitializer(SslContext sslCtx, Channel channel) {
        this.sslCtx = sslCtx;
        tms_channel = channel;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), DllClient.HOST, DllClient.PORT));
        }

        // Enable stream compression (you can remove these two if unnecessary)
        //  pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        // pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        // Add the number codec first,
        pipeline.addLast("biz_dec", new BizMsgDecoder());
        pipeline.addLast("biz_enc", new BizMsgEncoder());


        // and then business logic.
        pipeline.addLast(new DllClientHandler(tms_channel));
    }
}
