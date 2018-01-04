package pax.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;
import pax.netty.data.Message;

/**
 * Created by xieke on 2017/4/19.
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    static Logger logger = Logger.getLogger(MessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        msg.write(out);
//        logger.info("[MessageEncoder encode]ByteBuf size ：" + out.readableBytes() + ",ByteBuf:" + ByteBufUtil.hexDump(out));
    }
}