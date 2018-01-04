package pax.depot.job;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOUtil;
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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : wangtao
 * @Description : 未同步到交易系统的任务  同步到 交易系统中
 * @Date : 2017/10/18 13:47
 */
public class TaskSyncJob extends BaseJob {
    static Logger logger = Logger.getLogger(TaskSyncJob.class);

    @Override
    public String run() throws Exception {
        logger.info("任务 同步到 交易系统。。。");
        String result = this.syncTask();
        logger.info("任务 同步到 交易系统。。。" + result);
        return result;
    }

    private String syncTask() {

        //获取待同步的计划任务
        StringBuffer sql = new StringBuffer();
        sql.append(" select pt.id,pt.plan_name,pt.closeddate,pt.plandate,ter.mer_no,ter.ter_no from ").append(PlanTaskService.TABLE_FULL_NAME).append(" pt ")
                .append(" left join ").append(PlanTaskService.RE_TABLE_FULL_NAME).append(" rp on pt.id=rp.plantask_id ")
                .append(" left join ").append(TerminalsService.TABLE_FULL_NAME).append(" ter on ter.id=rp.terminal_id ")
                .append(" where pt.is_task_update= ? and pt.status!=? and pt.type=? and rp.status=? ")
                //商户号和终端号不能为空
                .append(" and ter.ter_no is not null and ter.mer_no is not null and ter.ter_no !='' and ter.mer_no !='' ");
        List paramList = new ArrayList();
        //未同步的
        paramList.add(PlanTaskContext.TASK_UPDATE_NO);
        //排除已成功的
        paramList.add(PlanTaskService.STATUS_2);
        //远程下载
        paramList.add(PlanTaskService.TYPE_0);
        //任务终端状态正常
        paramList.add(PlanTaskService.STATUS_1);
        logger.info(String.format("syncTask sql is : %s , param is : %s ", sql, paramList));
        try {
            List<BizObject> taskList = QueryFactory.executeQuerySQL(sql.toString(), paramList);
            logger.info(String.format("syncTask taskList is : %s ", taskList));
            //组装数据
            List<byte[]> dataList = new ArrayList<>();

            for (BizObject task : taskList) {
                byte[] tpdu = {0x60, 0x00, 0x36, 0x00, 0x00};

                //时间处理   去掉末尾的 .0   去掉 - ： 空格
                String closeddate = task.getString("closeddate");
                closeddate = closeddate.substring(0, closeddate.indexOf("."));
                closeddate = closeddate.replaceAll("-| |:|", "");

                String plandate = task.getString("plandate");
                plandate = plandate.substring(0, plandate.indexOf("."));
                plandate = plandate.replaceAll("-| |:|", "");

                InetAddress[] allByName = InetAddress.getAllByName(SystemKit.getCacheParamById("tms_server", "hostname"));
                String tmsServer = String.format("%s:%s", allByName[0].getHostAddress(), SystemKit.getCacheParamById("tms_server", "tms_port"));
                logger.info(String.format("tmsServer is : %s ", tmsServer));
                //应用数据 部分
                StringBuffer taskInfo = new StringBuffer();
                //1	交易码	Ans6	007001
                taskInfo.append(PlanTaskContext.TRADE_CODE_TASK).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //2	渠道id	Ans4	CPOS
                        .append(PlanTaskContext.CHANNEL_CPOS).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //3	商户号	Ans15
                        .append(task.getString("mer_no")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //4	终端号	Ans8
                        .append(task.getString("ter_no")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //5	任务内容ID	Ans20
                        .append(task.getString("id")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //6	平台标识	Ans8	11111111
                        .append(PlanTaskContext.PLATFORM).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //7	下载内容标志	Ans2	00
                        .append(PlanTaskContext.DOWNLOAD_CONTENT_MARK).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //8 限制日期	Ans8	YYYYMMDD，下载结束时间中提取，如20170904
                        .append(closeddate.substring(0, 8)).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //9	应用版本	Ans50	000001
                        .append(PlanTaskContext.APP_VER).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //10	下载时机标志	Ans4	9001
                        .append(PlanTaskContext.DOWNLOAD_TIME_MARK).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //11	TMS电话号码1	Ans20
                        .append(SystemKit.getCacheParamById("download_set", "phone1")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //12	TMS电话号码2	Ans20
                        .append(SystemKit.getCacheParamById("download_set", "phone2")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //13	TMS的IP端口1	Ans30	格式如"116.226.105.229:8087"
                        .append(tmsServer).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //14	TMS的IP端口2	Ans30
                        .append("").append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        // 15	GPRS参数	Ans60
                        .append(SystemKit.getCacheParamById("download_set", "gprs")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //16	CDMA参数	Ans60
                        .append(SystemKit.getCacheParamById("download_set", "cdma")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //17	下载任务校验	Ans32
                        .append("").append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //18	自动重拨间隔	Ans2	60
                        .append(PlanTaskContext.AUTO_REDIAL_INTERVAL).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //19	任务消息	Ans30
                        .append(task.getString("plan_name")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //20	tpdu	Ans10
                        .append(ISOUtil.hexString(tpdu)).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //21	下载开始时间	Ans12	YYMMDDhhmmss， 如170804000000，表示2017年8月4日0时0分0秒
                        .append(plandate.substring(2)).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                        //22	下载结束时间	Ans12	YYMMDDhhmmss， 如170904235959，表示2017年9月4日23时59分59秒
                        .append(closeddate.substring(2)).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                ;
                logger.info(String.format("taskInfo is : %s", taskInfo));
                byte[] mainData = taskInfo.toString().getBytes("gbk");
                byte[] byteLen = ByteUtils.intToBytes(mainData.length + tpdu.length);
                byte[] allData = ByteUtils.byteMerger(ByteUtils.byteMerger(byteLen, tpdu), mainData);
                dataList.add(allData);
            }
            //同步到 交易系统
            if (dataList != null && dataList.size() > 0) {
                logger.info(String.format("start a client ... the data list is : %s", dataList.size()));
                TmsClient.startClient(SystemKit.getCacheParamById("trade_server", "trade_ip"), Integer.parseInt(SystemKit.getCacheParamById("trade_server", "trade_port")), dataList);
            }
            return BaseJob.OK;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("[TaskSyncJob]", e);
            return "error is : " + e.getMessage();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("[TaskSyncJob]", e);
            return "error is : " + e.getMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("[TaskSyncJob]", e);
            return "error is : " + e.getMessage();
        } catch (SSLException e) {
            e.printStackTrace();
            logger.error("[TaskSyncJob]", e);
            return "error is : " + e.getMessage();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("[TaskSyncJob]", e);
            return "error is : " + e.getMessage();
        }

    }
}
