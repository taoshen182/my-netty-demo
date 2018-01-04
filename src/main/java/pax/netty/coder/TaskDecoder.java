package pax.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOUtil;
import pax.netty.util.ByteUtils;
import pax.service.plantask.PlanTaskContext;
import tool.dao.BizObject;

import java.util.List;

/**
 * Created by want on 2017/5/25.
 */
public class TaskDecoder extends ByteToMessageDecoder {
    static Logger logger = Logger.getLogger(TaskDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logger.info("[TaskDecoder decode]input byteBuf is : " + ByteBufUtil.hexDump(in));

        int mixLen = 13;
        if (in.readableBytes() < mixLen) {
            logger.info(String.format(" readable bytes is short : %d < 13", in.readableBytes()));
            return;
        }
        in.markReaderIndex();
        //读取长度
        byte[] len = new byte[2];
        in.readBytes(len);
        int len_int = ByteUtils.byte2IntLen(len);
        //验证长度
        logger.info("len = " + ISOUtil.hexString(len) + ", len is : " + ByteUtils.byte2IntLen(len));
        if (in.readableBytes() < len_int) {
            in.resetReaderIndex();
            return;
        }

        byte[] tpdu = new byte[5];
        in.readBytes(tpdu);
        logger.info("tpdu = " + ISOUtil.hexString(tpdu));
        //读取剩下部分的数据
        byte[] req = new byte[len_int - tpdu.length];
        in.readBytes(req);
        String body = new String(req, "gbk");
        logger.info("body = " + body);
        String[] split = body.split("\\|");
        BizObject ret = new BizObject();

        //封装数据
        // 交易码	Ans6	007008
        ret.set(PlanTaskContext.TradeResponseField.TRADE_CODE, split[0]);
        // 渠道id	Ans4	CPOS
        ret.set(PlanTaskContext.TradeResponseField.CHANNEL_ID, split[1]);
        // 商户号	Ans15
        ret.set(PlanTaskContext.TradeResponseField.MERCHANT_NO, split[2]);
        // 终端号	Ans8
        ret.set(PlanTaskContext.TradeResponseField.TER_NO, split[3]);
        // 任务内容ID	Ans20
        ret.set(PlanTaskContext.TradeResponseField.TASK_ID, split[4]);
        // 响应码	Ans2	00表示成功
        ret.set(PlanTaskContext.TradeResponseField.RESP_CODE, split[5]);

        //输出数据
        out.add(ret);
        logger.info("[TaskDecoder decode]output BizObject is : " + ret.toString());
    }
}
