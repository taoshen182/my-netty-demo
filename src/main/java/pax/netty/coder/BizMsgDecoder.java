package pax.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import pax.util.JsonUtils;
import sand.utils.JsonTool;
import tool.dao.BizObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by want on 2017/6/2.
 */
public class BizMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(" BizMsgDecoder ByteBuf in  " + ByteBufUtil.hexDump(in));

        byte[] req = new byte[in.readableBytes()];
        in.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("body = " + body);

        BizObject bizObject = JsonTool.toBiz(body);
        out.add(bizObject);
    }
}
