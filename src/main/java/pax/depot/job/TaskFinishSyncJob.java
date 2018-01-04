package pax.depot.job;

import org.apache.log4j.Logger;
import pax.netty.client.TmsClient;
import pax.netty.util.ByteUtils;
import pax.service.plantask.PlanTaskContext;
import pax.service.plantask.PlanTaskService;
import pax.service.terminals.TerminalsService;
import sand.depot.job.BaseJob;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

import javax.net.ssl.SSLException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.caucho.hessian.io.HessianInputFactory.log;
import static pax.netty.server.TmsServerHandler.update2TradeServer;

/**
 * @Author : wangtao
 * @Description : 下载任务完成后同步到交易系统
 * @Date : 2017/10/18 17:56
 */
public class TaskFinishSyncJob extends BaseJob {
    static Logger logger = Logger.getLogger(TaskFinishSyncJob.class);

    @Override
    public String run() throws Exception {
        logger.info("任务完成 同步到 交易系统。。。");
        String result = this.syncTaskFinish();
        logger.info("任务完成 同步到 交易系统。。。" + result);
        return result;
    }

    private String syncTaskFinish() {

        //获取待同步的计划任务
        StringBuffer sql = new StringBuffer();
        sql.append(" select pt.id,pt.plan_name,ter.mer_no,ter.ter_no from ").append(PlanTaskService.TABLE_FULL_NAME).append(" pt ")
                .append(" left join ").append(PlanTaskService.RE_TABLE_FULL_NAME).append(" rp on pt.id=rp.plantask_id ")
                .append(" left join ").append(TerminalsService.TABLE_FULL_NAME).append(" ter on ter.id=rp.terminal_id ")
                .append(" where pt.status = ? and pt.is_task_finish = ? and pt.is_task_update=? and pt.type=? and rp.status=? ")
                //商户号和终端号不能为空
                .append(" and ter.ter_no is not null and ter.mer_no is not null and ter.ter_no !='' and ter.mer_no !='' ");
        List paramList = new ArrayList();
        //下载完成
        paramList.add(PlanTaskService.STATUS_2);
        //任务完成 未同步的
        paramList.add(PlanTaskContext.TASK_UPDATE_NO);
        //任务 已同步的
        paramList.add(PlanTaskContext.TASK_UPDATE_YES);
        //远程下载
        paramList.add(PlanTaskService.TYPE_0);
        //任务终端状态成功
        paramList.add(PlanTaskService.STATUS_2);
        logger.info(String.format("syncTaskFinish sql is : %s , param is : %s ", sql, paramList));
        try {
            List<BizObject> taskList = QueryFactory.executeQuerySQL(sql.toString(), paramList);
            logger.info(String.format("syncTaskFinish taskList is : %s ", taskList));
            update2TradeServer(taskList);
//            //组装数据
//            List<byte[]> dataList = new ArrayList<>();
//
//            for (BizObject task : taskList) {
//                byte[] tpdu = {0x60, 0x00, 0x36, 0x00, 0x00};
//                //应用数据 部分
//                StringBuffer taskInfo = new StringBuffer();
//                //1	交易码	Ans6	007008
//                taskInfo.append(PlanTaskContext.TRADE_CODE_TASK_FINISH).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
//                        //2	渠道id	Ans4	CPOS
//                        .append(PlanTaskContext.CHANNEL_CPOS).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
//                        //3	商户号	Ans15
//                        .append(task.getString("mer_no")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
//                        //4	终端号	Ans8
//                        .append(task.getString("ter_no")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
//                        //5	任务内容ID	Ans20
//                        .append(task.getString("id")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
//                ;
//                logger.info(String.format("taskInfo is : %s", taskInfo));
//                byte[] main_data = taskInfo.toString().getBytes("gbk");
//                byte[] bt_len = ByteUtils.intToBytes(main_data.length + tpdu.length);
//                byte[] all_data = ByteUtils.byteMerger(ByteUtils.byteMerger(bt_len, tpdu), main_data);
//                dataList.add(all_data);
//            }
//            //同步到 交易系统
//            if (dataList != null && dataList.size() > 0) {
//                logger.info(String.format("[TaskFinishSyncJob]start a client ... the data list is : %s", dataList.size()));
//                TmsClient.startClient(SystemKit.getCacheParamById("trade_server", "trade_ip"), Integer.parseInt(SystemKit.getCacheParamById("trade_server", "trade_port")), dataList);
//            }
            return BaseJob.OK;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("[TaskFinishSyncJob]", e);
            return "[TaskFinishSyncJob]error is : " + e.getMessage();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("[TaskFinishSyncJob]", e);
            return "[TaskFinishSyncJob]error is : " + e.getMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("[TaskFinishSyncJob]", e);
            return "[TaskFinishSyncJob]error is : " + e.getMessage();
        } catch (SSLException e) {
            e.printStackTrace();
            logger.error("[TaskFinishSyncJob]", e);
            return "[TaskFinishSyncJob]error is : " + e.getMessage();
        }
    }
}
