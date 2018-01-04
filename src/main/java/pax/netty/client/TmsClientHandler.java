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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOUtil;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.netty.data.Request;
import pax.service.appversion.AppVersionService;
import pax.service.inst.InstService;
import pax.service.plantask.PlanTaskContext;
import pax.service.plantask.PlanTaskService;
import pax.service.terminals.TerminalsService;
import pax.service.tertype.TerTypeService;
import sand.actionhandler.system.ActionHandler;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.List;

import static pax.netty.coder.MessageDecoder.CUR_REQUEST_KEY;
import static pax.service.plantask.PlanTaskContext.RESP_CODE_SUCCESS;

/**
 * Handler for a client-side channel.  This handler maintains stateful
 * information which is specific to a certain channel using member variables.
 * Therefore, an instance of this handler can cover only one channel.  You have
 * to create a new handler instance whenever you create a new channel and insert
 * this handler to avoid a race condition.
 */
public class TmsClientHandler extends SimpleChannelInboundHandler {
    static Logger logger = Logger.getLogger(TmsClientHandler.class);

    private ChannelHandlerContext ctx;
    private List<byte[]> dataList;

    public TmsClientHandler() {

    }

    public TmsClientHandler(List<byte[]> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                logger.info("[TmsClientHandler]channel active ......" + ISOUtil.hexString(dataList.get(i)));
                this.ctx = ctx;
                ctx.writeAndFlush(dataList.get(i));
                logger.info("[TmsClientHandler]end " + i);
            }
        }

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, final Object bt) throws SQLException {
        //同步到交易系统返回结果处理
        if (BizObject.class.isInstance(bt)) {
            logger.info("[TmsClientHandler]obj is : " + bt.toString());
            BizObject ret = (BizObject) bt;
            //请求成功
            if (PlanTaskContext.RESP_CODE_SUCCESS.equals(ret.getString(PlanTaskContext.TradeResponseField.RESP_CODE))) {
                //007001
                if (PlanTaskContext.TRADE_CODE_TASK.equals(ret.getString(PlanTaskContext.TradeResponseField.TRADE_CODE))) {
                    //更改任务的状态  同步成功
                    String task_id = ret.getString(PlanTaskContext.TradeResponseField.TASK_ID);
                    StringBuffer sql = new StringBuffer();
                    sql.append(" update ").append(PlanTaskService.TABLE_FULL_NAME).append(" set is_task_update='")
                            .append(PlanTaskContext.TASK_UPDATE_YES).append("' where id='").append(task_id).append("'");
                    logger.info("[TmsClientHandler 007001]task sql is : " + sql);
                    int i = QueryFactory.executeUpdateSQL(sql.toString());
                    logger.info("[TmsClientHandler 007001]task result is : " + i);
                }
                //007008
                if (PlanTaskContext.TRADE_CODE_TASK_FINISH.equals(ret.getString(PlanTaskContext.TradeResponseField.TRADE_CODE))) {
                    //更改任务的状态  同步成功
                    String task_id = ret.getString(PlanTaskContext.TradeResponseField.TASK_ID);
                    StringBuffer sql = new StringBuffer();
                    sql.append(" update ").append(PlanTaskService.TABLE_FULL_NAME).append(" set is_task_finish='")
                            .append(PlanTaskContext.TASK_UPDATE_YES).append("' where id='").append(task_id).append("'");
                    logger.info("[TmsClientHandler 007008]task finish sql is : " + sql);
                    int i = QueryFactory.executeUpdateSQL(sql.toString());
                    logger.info("[TmsClientHandler 007008]task finish result is : " + i);
                }

            }
            //请求异常
            else {
                logger.info("sync to TradeServer wrong," + ret.toString());
            }
        } else {
            logger.info("no right msg : " + bt.toString());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 发送远程下载请求
     */
    private void sendRemoteDownloadRequest() {

        System.out.println("send remote download request");
        // Do not send more than 4096 numbers.
        Request r = new Request("2011".getBytes());
        Attribute<Request> cur_request = ctx.channel().attr(CUR_REQUEST_KEY);

        cur_request.set(r);

        ctx.write(r);
        ctx.flush();
    }
//    private void sendNumbers() {
//        // Do not send more than 4096 numbers.
//        ChannelFuture future = null;
//        for (int i = 0; i < 4096 && next <= TmsClient.COUNT; i++) {
//            future = ctx.write(Integer.valueOf(next));
//            next++;
//        }
//        if (next <= TmsClient.COUNT) {
//            assert future != null;
//            future.addListener(numberSender);
//        }
//        ctx.flush();
//    }

//    private final ChannelFutureListener numberSender = new ChannelFutureListener() {
//        @Override
//        public void operationComplete(ChannelFuture future) throws Exception {
//            if (future.isSuccess()) {
//                sendNumbers();
//            } else {
//                future.cause().printStackTrace();
//                future.channel().close();
//            }
//        }
//    };
}
