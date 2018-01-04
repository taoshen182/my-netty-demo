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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import pax.netty.coder.TaskDecoder;
import pax.netty.coder.TaskEncoder;

import java.util.List;

/**
 * Creates a newly configured {@link ChannelPipeline} for a client-side channel.
 */
public class TmsClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    private List<byte[]> data;

    public TmsClientInitializer(SslContext sslCtx, List<byte[]> data) {
        this.sslCtx = sslCtx;
        this.data = data;

    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), TmsClient.HOST, TmsClient.PORT));
        }

        // Enable stream compression (you can remove these two if unnecessary)
        //  pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        // pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        // Add the number codec first,
        pipeline.addLast(new TaskDecoder());
        pipeline.addLast(new TaskEncoder());

        // and then business logic.
        pipeline.addLast(new TmsClientHandler(data));
    }
}
