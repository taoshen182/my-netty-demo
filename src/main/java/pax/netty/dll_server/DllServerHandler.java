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
package pax.netty.dll_server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;

import pax.netty.data.Request;
import pax.netty.util.TransCode;
import tool.dao.BizObject;

import java.math.BigInteger;

import static pax.netty.coder.MessageDecoder.CUR_REQUEST_KEY;

/**
 * Handler for a server-side channel.  This handler maintains stateful
 * information which is specific to a certain channel using member variables.
 * Therefore, an instance of this handler can cover only one channel.  You have
 * to create a new handler instance whenever you create a new channel and insert
 * this handler  to avoid a race condition.
 */
public class DllServerHandler extends SimpleChannelInboundHandler<BizObject> {

    private BigInteger lastMultiplier = new BigInteger("1");
    private BigInteger factorial = new BigInteger("1");

    private ChannelHandlerContext ctx;

    private boolean flag = true;

    /**
     * 发送远程下载请求
     */
    private void sendRemoteDownloadRequest() {

        System.out.println("send remote download request");
        // Do not send more than 4096 numbers.
        Request r = new Request("send my file".getBytes());

        Attribute<Request> cur_request = ctx.channel().attr(CUR_REQUEST_KEY);

        cur_request.set(r);

        ctx.write(r);
        ctx.flush();
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        this.ctx = ctx;
        //sendRemoteDownloadRequest();
        //sendNumbers();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, BizObject msg) throws Exception {
        // Calculate the cumulative factorial and send it to the client.
//        lastMultiplier = msg;
//        factorial = factorial.multiply(msg);
        System.out.println("is the msg = " + msg);

        if (msg.getString("cmd").equals("A0")) {
            BizObject biz = new BizObject();

            biz.set("trans_code", TransCode.SUCCESS.getCode());
            biz.set("cmd", msg.get("cmd"));
            biz.set("tpdu", msg.get("tpdu"));
            biz.set("ver", msg.get("ver"));
            biz.set("fid", msg.get("fid"));
            biz.set("mid", msg.get("mid"));
            biz.set("sn", msg.get("sn"));
            biz.set("version", msg.get("version"));
            System.out.println("biz = " + biz);
            ctx.writeAndFlush(biz);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.printf("inactive  Factorial of %,d is: %,d%n", lastMultiplier, factorial);
        System.out.println("Dllserver close a client channel ....isOpen = " + ctx.channel().isOpen());
//        ChannelFuture future = ctx.channel().closeFuture();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
