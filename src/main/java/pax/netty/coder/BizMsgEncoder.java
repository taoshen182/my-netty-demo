package pax.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import tool.dao.BizObject;

/**
 * Created by want on 2017/6/2.
 */
public class BizMsgEncoder extends MessageToByteEncoder<BizObject> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BizObject msg, ByteBuf out) throws Exception {
        System.out.println("---BizMsgEncoder in encode---");
        String s = msg.toString();
        System.out.println("s = " + s);
        out.writeBytes(s.getBytes());
        System.out.println("---BizMsgEncoder out encode---" + ByteBufUtil.hexDump(out));
    }
}
