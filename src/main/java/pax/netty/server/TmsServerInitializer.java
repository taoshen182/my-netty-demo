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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.util.Attribute;
import org.apache.log4j.Logger;
import pax.netty.coder.MessageDecoder;
import pax.netty.coder.MessageEncoder;
import tool.dao.BizObject;

import static pax.netty.coder.MessageDecoder.CUR_SN_KEY;

/**
 * Creates a newly configured {@link ChannelPipeline} for a server-side channel.
 */
public class TmsServerInitializer extends ChannelInitializer<SocketChannel> {
    static Logger logger = Logger.getLogger(TmsServerInitializer.class);
    private final SslContext sslCtx;

    public TmsServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        // Enable stream compression (you can remove these two if unnecessary)
        //  pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        // pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));

        // Add the number codec first,
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new MessageEncoder());

        // and then business logic.
        // Please note we create a handler for every new channel
        // because it has stateful properties.
        pipeline.addLast(new TmsServerHandler());
    }

    public static void main(String[] args) {
        byte[] b = {0x30, 0x32
        };

        String s = new String(b);
        System.out.println(s);

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        logger.info("有个客户端断开了连接？？？");
        Attribute<BizObject> cur_sn_session = ctx.channel().attr(CUR_SN_KEY);
        cur_sn_session.set(null);
    }


}
