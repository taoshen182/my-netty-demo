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
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.netty.client.TmsClient;
import pax.netty.data.*;
import pax.netty.util.ByteUtils;
import pax.netty.util.TransCode;
import pax.service.appversion.AppVersionService;
import pax.service.inst.InstService;
import pax.service.plantask.PlanTaskContext;
import pax.service.plantask.PlanTaskService;
import pax.service.terminals.TerminalsService;
import pax.service.tertype.TerTypeService;
import pax.util.AsyncConnector;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.SystemKit;
import sand.utils.JsonTool;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

import javax.net.ssl.SSLException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static pax.netty.coder.MessageDecoder.CUR_SN_KEY;
import static pax.netty.coder.MessageDecoder.HTTP_CLIENT_KEY;
import static pax.service.plantask.PlanTaskService.TYPE_1;

/**
 * Handler for a server-side channel.  This handler maintains stateful
 * information which is specific to a certain channel using member variables.
 * Therefore, an instance of this handler can cover only one channel.  You have
 * to create a new handler instance whenever you create a new channel and insert
 * this handler  to avoid a race condition.
 */
public class TmsServerHandler extends SimpleChannelInboundHandler<Message> {
    static Logger logger = Logger.getLogger(TmsServerHandler.class);

    private BigInteger lastMultiplier = new BigInteger("1");
    private BigInteger factorial = new BigInteger("1");

    private ChannelHandlerContext ctx;
//    private SSLService sslService;

    private TerminalsService terminalsService;
    private InstService instService;
    private AppVersionService appVersionService;
    private TerTypeService terTypeService;
    private PlanTaskService planTaskService;

//    public static String dll_server_url = "http://113.204.232.6:24019";
//    public static String dll_server_url = "http://localhost:8181/basic.LoginAH.test";

    {
        appVersionService = (AppVersionService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("appVersionService");
        terminalsService = (TerminalsService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("terminalsService");
        instService = (InstService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("instService");
        terTypeService = (TerTypeService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("terTypeService");
        planTaskService = (PlanTaskService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("planTaskService");
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("[TmsServerHandler channelActive]" + ctx.channel().id());
        this.ctx = ctx;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {

        Attribute<BizObject> cur_sn_session = ctx.channel().attr(CUR_SN_KEY);

        if (ConfirmMsg.class.isInstance(msg)) {
            ConfirmMsg confirmMsg = (ConfirmMsg) msg;
            ConfirmMsg respMsg = new ConfirmMsg();
            confirmMsg.copyInfoTo(respMsg);

            //应答终端，确认下载通知
            respMsg.setRespcode(TransCode.SUCCESS);
            ctx.writeAndFlush(respMsg);
        }

        //初始化 交易
        if (TerMsg.class.isInstance(msg)) {
            TerMsg terMsg = (TerMsg) msg;
            TerMsg respMsg = new TerMsg();
            terMsg.copyInfoTo(respMsg);
            BizObject ter = terMsg.toBiz();
            //连接动态库 , 获取初始化任务
            TransCode transCode = checkTerminal(ter, TYPE_1);
            if (TransCode.SUCCESS.getCode().equals(transCode.getCode())) {
                //动态库加载成功
                cur_sn_session.set(ter);
            }
            respMsg.setRespcode(transCode);
            ctx.writeAndFlush(respMsg);
        }
        //DLL 交互
        if (CmdMsg.class.isInstance(msg)) {
            CmdMsg cmdMsg = (CmdMsg) msg;
            //获取当前终端
            BizObject ter = ctx.channel().attr(CUR_SN_KEY).get();
            //连接DLL，获取返回数据
            String data = getDateFromDll(ter, cmdMsg);
            if (StringUtils.isNotBlank(data)) {
                byte[] ret_data = Hex.decode(data);
                cmdMsg.setRetData(ret_data);
                cmdMsg.setRespcode(TransCode.SUCCESS);
            } else {
                byte[] ret_data = Hex.decode("020001A4A403");
                cmdMsg.setRetData(ret_data);
                cmdMsg.setRespcode(TransCode.ERROR_VERSION);
            }
            ctx.writeAndFlush(cmdMsg);
        }

        if (IssuAppMsg.class.isInstance(msg)) {
            IssuAppMsg issuAppMsg = (IssuAppMsg) msg;

            IssuAppMsg respMsg = new IssuAppMsg();
            issuAppMsg.copyInfoTo(respMsg);

            respMsg.setRespcode(TransCode.SUCCESS);

            ctx.writeAndFlush(respMsg);
        }

        //远程下载交易
        if (DownloadMsg.class.isInstance(msg)) {
            DownloadMsg downloadMsg = (DownloadMsg) msg;

            DownloadMsg respMsg = new DownloadMsg();
            downloadMsg.copyInfoTo(respMsg);

            BizObject ter = downloadMsg.toBiz();

            //获取远程下载的任务
            TransCode transCode = checkTerminal(ter, PlanTaskService.TYPE_0);
            if (TransCode.SUCCESS.getCode().equals(transCode.getCode())) {
                cur_sn_session.set(ter);
            }
            respMsg.setRespcode(transCode);
            ctx.writeAndFlush(respMsg);
        }

    }

    private String getDateFromDll(BizObject ter, CmdMsg cmdMsg) {
        BizObject ret = prepareDateForDll(ter, cmdMsg);
        String content = ret.toJsonString();
        AsyncConnector httpClient = getHttpClient(false);
        if (httpClient == null) {
            logger.error("httpClient is null");
            return null;
        }
        logger.info("[TmsServerHandler download]httpclient send data :" + content);
        Object json = httpClient.post(SystemKit.getCacheParamById("dll_server", "dll_server"), content);
        logger.info("[TmsServerHandler download]httpclient receive data : " + json);

        if (null == json) {
            logger.error("no response from DLL Server");
            return null;
        }

        try {
            BizObject biz = JsonTool.toBiz(json.toString());
            Map map = biz.toMap();
            if (!"00".equals(map.get("respCode"))) {
                logger.error("response code from DLL Server is not 00，the code is ：" + map.get("respCode"));


                return null;
            }
            if ("00".equals(map.get("respCode"))) {
                //请求成功，进一步验证信息
                checkRespMsg(map);
                String data = map.get("data").toString();
                //如果是A4指令了，即下载结束
                boolean flag = dealCmdResult(ter.getString("ter_sn"), ter.getString("code"), data);
                if (flag) {
                    //下载成功后移除
                    ter.removeField("code");
                }
                return data;
            }
        } catch (JSONException e) {
            logger.error("JSONException", e);
            e.printStackTrace();
        } catch (SQLException e) {
            logger.error("SQLException", e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.error("InterruptedException", e);
            e.printStackTrace();
        } catch (SSLException e) {
            logger.error("SSLException", e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
            e.printStackTrace();
        }
        return null;
    }


    private boolean dealCmdResult(String ter_sn, String tradeCode, String data) throws SQLException, InterruptedException, UnsupportedEncodingException, SSLException {
        //下载结束
        if ("020001A4A403".equals(data.toUpperCase())) {
            logger.info("download successfully! sn : " + ter_sn + ",code is : " + tradeCode);
            BizObject terminal = terminalsService.getTerminal(ter_sn);
            //初始化完成
            if (TransCode.INIT.getCode().equals(tradeCode)) {
                //处理终端状态
                setTerStatus(terminal);
                //处理任务状态
                setTaskFlag(terminal, TYPE_1);
            }
            //远程下载
            if (TransCode.DOWNLOAD.getCode().equals(tradeCode)) {
                setTaskFlag(terminal, PlanTaskService.TYPE_0);
            }
            return true;
        }
        //下载未结束
        return false;
    }

    private void setTerStatus(BizObject terminal) throws SQLException {
        String sql = " update " + TerminalsService.TABLE_FULL_NAME + " set ter_status='" + TerminalsService.TER_STATUS_2 + "' where id ='" + terminal.getString("id") + "'";
        logger.info(" sql is : " + sql);
        int i = QueryFactory.executeUpdateSQL(sql);
        logger.info("i = " + i);
    }

    private void setTaskFlag(BizObject terminal, String type) throws SQLException, InterruptedException, UnsupportedEncodingException, SSLException {
        //获取所有初始化任务 或者 远程下载任务
        List<BizObject> taskList = planTaskService.listTaskByTerId(terminal.getString("id"), type, PlanTaskService.STATUS_1);
        if (taskList != null && taskList.size() > 0) {
            //将终端对应的已完成任务状态改为 status = 2
            StringBuffer ids = new StringBuffer();
            for (int j = 0; j < taskList.size(); j++) {
                if (j == 0) {
                    ids.append("'").append(taskList.get(j).getString("id")).append("'");
                } else {
                    ids.append(",").append("'").append(taskList.get(j).getString("id")).append("'");
                }
            }
            //更细终端任务表中终端下载状态
            String task_sql = " update " + PlanTaskService.RE_TABLE_FULL_NAME + " set status='" + PlanTaskService.STATUS_2
                    + "' where plantask_id in (" + ids + ") and status='" + PlanTaskService.STATUS_1 + "'";
            logger.info(" sql is : " + task_sql);
            int iii = QueryFactory.executeUpdateSQL(task_sql);
            logger.info("i = " + iii);

            for (int j = 0; j < taskList.size(); j++) {
                //查询此任务下 是否还有正常状态的终端任务
                String normalSql = " select count(*) as num_n from " + PlanTaskService.RE_TABLE_FULL_NAME + " where status='"
                        + PlanTaskService.STATUS_1 + "' and plantask_id='" + taskList.get(j).getString("id") + "' ";
                logger.info(" sql is : " + normalSql);
                List<BizObject> normalList = QueryFactory.executeQuerySQL(normalSql);
                logger.info(" sql is : " + normalList.size());
                if (null != normalList && normalList.size() == 1 && StringUtils.equals("0", normalList.get(0).getString("num_n"))) {
                    //无正常状态的任务，则表示全部下载完成，更新任务状态为下载完成
                    String taskSql = " update " + PlanTaskService.TABLE_FULL_NAME + " set status='" + PlanTaskService.STATUS_2
                            + "' where id='" + taskList.get(j).getString("id") + "'";
                    int iiii = QueryFactory.executeUpdateSQL(taskSql);
                    logger.info("sql is : " + taskSql + ",the result is : " + iiii);
                }
            }

            //远程下载同步到交易系统
            if (StringUtils.equals(type, PlanTaskService.TYPE_0)) {
                update2TradeServer(taskList);
            }
        }
    }

    public static void update2TradeServer(List<BizObject> taskList) throws UnsupportedEncodingException, SSLException, InterruptedException {
        //组装数据
        List<byte[]> dataList = new ArrayList<>();
        for (BizObject task : taskList) {
            byte[] tpdu = {0x60, 0x00, 0x36, 0x00, 0x00};
            //应用数据 部分
            StringBuffer taskInfo = new StringBuffer();
            //1	交易码	Ans6	007008
            taskInfo.append(PlanTaskContext.TRADE_CODE_TASK_FINISH).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                    //2	渠道id	Ans4	CPOS
                    .append(PlanTaskContext.CHANNEL_CPOS).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                    //3	商户号	Ans15
                    .append(task.getString("mer_no")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                    //4	终端号	Ans8
                    .append(task.getString("ter_no")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
                    //5	任务内容ID	Ans20
                    .append(task.getString("id")).append(PlanTaskContext.FIELD_SEPARATE_CHAR)
            ;
            logger.info(String.format("taskInfo is : %s", taskInfo));
            System.out.println(String.format("taskInfo is : %s", taskInfo));
            byte[] main_data = taskInfo.toString().getBytes("gbk");
            byte[] bt_len = ByteUtils.intToBytes(main_data.length + tpdu.length);
            byte[] all_data = ByteUtils.byteMerger(ByteUtils.byteMerger(bt_len, tpdu), main_data);
            dataList.add(all_data);
        }
        //同步到 交易系统
        if (dataList != null && dataList.size() > 0) {
            logger.info(String.format("start a client ... the data list is : %s", dataList.size()));
            System.out.println(String.format("start a client ... the data list is : %s", dataList.size()));
            TmsClient.startClient(SystemKit.getCacheParamById("trade_server", "trade_ip"), Integer.parseInt(SystemKit.getCacheParamById("trade_server", "trade_port")), dataList);
        }
    }


    private TransCode checkTerminal(BizObject ter, String type) {
        try {
            BizObject terminal = terminalsService.getTerminal(ter.getString("ter_sn"));
            //sn不存在
            if (terminal == null) {
                return TransCode.ERROR_SN;
            }
            //判断状态  如果是已经注销 或者 待确认
            if (TerminalsService.TER_STATUS_4.equals(terminal.getString("ter_status")) || TerminalsService.TER_STATUS_5.equals(terminal.getString("ter_status"))) {
                logger.info("error status!!! the status is : " + terminal.getString("ter_status"));
                return TransCode.ERROR_TER_STATUS;
            }
            //初始化
            if (StringUtils.equals(TYPE_1, type)) {
                //交易码不对
                if (!TransCode.INIT.getCode().equals(ter.getString("code")) && !TransCode.CHANGE.getCode().equals(ter.getString("code")) && !TransCode.SCRIPT.getCode().equals(ter.getString("code"))) {
                    return TransCode.ERROR_TRADE_TYPE;
                }
                //当交易码为 换机 ，且换机条件不允许
                if (TransCode.CHANGE.getCode().equals(ter.getString("code")) && !checkChangeInfo(ter.getString("mer_no"), ter.getString("ter_no"))) {
                    return TransCode.ERROR_TERMINAL;
                }
                //该终端已做初始化
                if (TransCode.INIT.getCode().equals(ter.getString("code")) &&
                        !(TerminalsService.TER_STATUS_0.equals(terminal.getString("ter_status")) || TerminalsService.TER_STATUS_1.equals(terminal.getString("ter_status")))) {
                    logger.info("can no init (status=0 or 1)!!! status is : " + terminal.getString("ter_status"));
                    return TransCode.ERROR_INIT;
                }
            }
            //远程下载交易
            else {
                //交易码不对
                if (!TransCode.DOWNLOAD.getCode().equals(ter.getString("code"))) {
                    return TransCode.ERROR_TRADE_TYPE;
                }
            }
            //根据终端获取所有任务
            List<BizObject> taskList = planTaskService.listTaskByTerId(terminal.getString("id"), type, PlanTaskService.STATUS_1);
            //无任务 不用下载
            if (null == taskList || taskList.size() < 1) {
                logger.info("no task to download !!!");
                return TransCode.ERROR_NO_DOWNLOAD;
            }
            //准备加载动态库的数据
            String content = prepareDateForDll(ter, taskList).toJsonString();

            logger.info("[TmsServerHandler loading dll ]dll_server_url is ： " + SystemKit.getCacheParamById("dll_server", "dll_server") + "httpclient send data : " + content);
            Object json = getHttpClient(true).post(SystemKit.getCacheParamById("dll_server", "dll_server"), content);
            logger.info("[TmsServerHandler  loading dll]httpclient receive data : " + json);
            //请求不到数据
            if (null == json) {
                logger.error("no response from DLL Server");
                return TransCode.ERROR_SYSTEM;
            }
            BizObject resp = JsonTool.toBiz(json.toString());
            Map map = resp.toMap();
            if (!"00".equals(map.get("respCode"))) {
                logger.error("response code from DLL Server is not 00，the code is ：" + resp.getString("respCode"));
                return TransCode.ERROR_SYSTEM;
            }
            //请求成功，进一步验证信息
            checkRespMsg(map);

            //设置响应码
            return TransCode.SUCCESS;
        } catch (SQLException e) {
            logger.error("[TmsServerHandler loading dll]SQLException", e);
            e.printStackTrace();
            return TransCode.ERROR_SYSTEM;
        } catch (JSONException e) {
            logger.error("[TmsServerHandler loading dll]JSONException", e);
            e.printStackTrace();
            return TransCode.ERROR_SYSTEM;
        }
    }

    private BizObject prepareDateForDll(BizObject ter, CmdMsg cmdMsg) {
        BizObject ret = new BizObject();
        //终端信息	termInfo
        ret.set("termInfo", ter.getString("termInfo"));
        //数据	data
        ret.set("data", cmdMsg.getDllMsg());
        //交易码	TransID
        ret.set("TransID", TransCode.DLL_SERVER_DOWNLOAD.getCode());
        //交易日期	TransDate
        ret.set("TransDate", getDateAndTime()[0]);
        //交易时间	TransTime
        ret.set("TransTime", getDateAndTime()[1]);
        return ret;
    }

    private BizObject prepareDateForDll(BizObject ter, List<BizObject> taskList) {
        BizObject ret = new BizObject();
        // 动态库名称	dllName
        ret.set("dllName", "libPaxTms");
        //日志级别	logLvl
        ret.set("logLvl", "2");
        //日志文件路径	logUrl
        ret.set("logUrl", "/home/dllserver/log/pax.log");
        //终端信息	termInfo
        ret.set("termInfo", ter.getString("ter_sn"));
        //应用路径	appUrl
        ret.set("appUrl", "/home/dllserver/001000/");

        List<BizObject> appList = new ArrayList<>();
        for (BizObject task : taskList) {
            BizObject app = new BizObject();
            //应用程序 更新方式	appType
            app.set("appType", "0002");
            //应用参数 更新方式	appParaType
            app.set("appParaType", "0002");
            //应用名称	appName
            app.set("appName", task.getString("app_name"));
            //应用版本号	appVer
            app.set("appVer", task.getString("tms_app_ver"));
            appList.add(app);
        }

        ret.set("appInfo", appList);
        //应用个数	appNum
        ret.set("appNum", appList.size());
        //交易码	TransID  加载动态库 00000
        ret.set("TransID", TransCode.DLL_SERVER_LOAD.getCode());
        // 交易日期	TransDate
        ret.set("TransDate", getDateAndTime()[0]);
        // 交易时间	TransTime
        ret.set("TransTime", getDateAndTime()[1]);
        return ret;
    }

    private void checkRespMsg(Map map) {
        // 终端信息	termInfo
//        System.out.println("termInfo = " + map.get("termInfo"));
        // 应答码	respCode
//        System.out.println("respCode = " + map.get("respCode"));
        // 应答描述	respMsg
//        System.out.println("respMsg = " + map.get("respMsg"));
        // 交易码	TransID
//        System.out.println("TransID = " + map.get("TransID"));
        // 交易日期	TransDate
//        System.out.println("TransDate = " + map.get("TransDate"));
        // 交易时间	TransTime
//        System.out.println("TransTime = " + map.get("termInfo"));
        System.out.println("checkRespMsg map is : " + map.toString());

    }

    private boolean checkChangeInfo(String mer_no, String ter_no) {
        if (StringUtils.isBlank(mer_no) || StringUtils.isBlank(ter_no)) {
            return false;
        }
        System.out.println("mer_no = " + mer_no + ",ter_no" + ter_no);
        System.out.println("验证成功，允许换机！！！");
        return true;
    }

    private String[] getDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String date = dateFormat.format(new Date());
        return date.split(" ");
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format("[TmsServerHandler.channelInactive]inactive Factorial of %,d is: %,d%n", lastMultiplier, factorial));
        ctx.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("[TmsServerHandler.exceptionCaught]My exceptionCaught !!!  " + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }


    private AsyncConnector getHttpClient(boolean flag) {
        Attribute<AsyncConnector> http_client = ctx.channel().attr(HTTP_CLIENT_KEY);
        AsyncConnector connector = null;

        //重新创建
        if (flag) {
            connector = AsyncConnector.getInstance();
            connector.renewClient();
            http_client.set(connector);

            logger.info("[SSLService getHttpClient]create a connector , " + connector.hashCode());
            return connector;
        } else {
            connector = http_client.get();
            logger.info("[SSLService getHttpClient]using an existed connector , " + connector.hashCode());
            return connector;
        }
    }

}
