package pax.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOUtil;

/**
 * @Author : wangtao
 * @Description :
 * @Date : 2017/10/17 15:03
 */
public class TaskEncoder extends MessageToByteEncoder<byte[]> {

    static Logger logger = Logger.getLogger(TaskEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
        logger.info("[TaskDecoder encode]start send msg ....");
        out.writeBytes(msg);
        logger.info("[TaskDecoder encode]send msg is : " + ISOUtil.hexString(msg));
    }
}
