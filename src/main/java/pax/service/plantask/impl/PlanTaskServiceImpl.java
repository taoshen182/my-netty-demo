package pax.service.plantask.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOUtil;
import pax.netty.util.ByteUtils;
import pax.service.ReturnCode;
import pax.service.app.AppService;
import pax.service.appversion.AppVersionService;
import pax.service.param.PTemplateService;
import pax.service.plantask.PlanTaskContext;
import pax.service.plantask.PlanTaskService;
import pax.service.terminals.TerminalsService;
import pax.util.AsyncConnector;
import pax.utils.TcpUtils;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.BillNoGenerator;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/7/26.
 */
public class PlanTaskServiceImpl implements PlanTaskService {
    static Logger logger = Logger.getLogger(PlanTaskServiceImpl.class);

    @Resource(name = "appVersionService")
    private AppVersionService appVersionService;
    @Resource(name = "pTemplateService")
    private PTemplateService pTemplateService;
    @Resource(name = "appService")
    private AppService appService;

    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where 1=1 ");
        if (null == param) {
            param = new BizObject();
        }
        List<Object> paramList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getString("plan_name"))) {
            sb.append(" and plan_name like ? ");
            paramList.add("%" + param.getString("plan_name") + "%");
        }
        if (StringUtils.isNotBlank(param.getString("startdate"))) {
            sb.append(" and createdate > ? ");
            paramList.add(param.getString("startdate"));
        }
        if (StringUtils.isNotBlank(param.getString("enddate"))) {
            sb.append(" and createdate < ? ");
            paramList.add(param.getString("enddate"));
        }
        if (StringUtils.isNotBlank(param.getString("type"))) {
            sb.append(" and type = ? ");
            paramList.add(param.getString("type"));
        }
        if (StringUtils.isNotBlank(param.getString("status"))) {
            sb.append(" and status = ? ");
            paramList.add(param.getString("status"));
        }


//        sb.append(" order by modifydate desc");
        logger.info("[PlanTaskServiceImpl list]sql = " + sb + ", param = " + paramList + ", page = " + page);
        List<BizObject> list = QueryFactory.executeQuerySQL(sb.toString(), paramList, page);
        for (BizObject task : list) {
            //查询正常
            String normalSql = " select count(*) as num_n from " + PlanTaskService.RE_TABLE_FULL_NAME + " where status='" + PlanTaskService.STATUS_1 + "' and plantask_id='" + task.getString("id") + "' ";
            List<BizObject> normalList = QueryFactory.executeQuerySQL(normalSql);
            task.set("num_n", normalList == null ? "0" : normalList.get(0).getString("num_n"));
            //查询下载完成
            String successSql = " select count(*) as num_s from " + PlanTaskService.RE_TABLE_FULL_NAME + " where status='" + PlanTaskService.STATUS_2 + "' and plantask_id='" + task.getString("id") + "' ";
            List<BizObject> successList = QueryFactory.executeQuerySQL(successSql);
            task.set("num_s", successList == null ? "0" : successList.get(0).getString("num_s"));
        }
        return list;
    }

    @Override
    public BizObject getById(String id) throws SQLException {
        return new QueryFactory(TABLE_NAME).getByID(id);
    }

    @Override
    public List<BizObject> listTerminalsByTask(String plantask_id, String status) throws SQLException {
        StringBuffer sb = new StringBuffer("select re.*,ter.ter_com_id,ter.ter_type_id,ter.sn from ")
                .append(RE_TABLE_FULL_NAME).append(" re ,")
                .append(TerminalsService.TABLE_FULL_NAME).append(" ter ")
                .append(" where ter.id=re.terminal_id ");
        List<Object> param_list = new ArrayList<>();

        if (StringUtils.isNotBlank(status)) {
            sb.append(" and re.status=?");
            param_list.add(status);
        }

        sb.append(" and re.plantask_id=?");
        param_list.add(plantask_id);
//        sb.append(" order by re.modifydate desc");
        logger.info("[PlanTaskServiceImpl listTerminalsByTask]sql = " + sb + ", param = " + param_list);
        List list = QueryFactory.executeQuerySQL(sb.toString(), param_list);
        return list;
    }

    @Override
    public BizObject del(String plantask_id) {
        try { //删除任务
            BizObject plantask = getById(plantask_id);
            if (plantask == null) {
                return ReturnCode.errorData(ReturnCode.ERROR_NOTEXSIT);
            }

            if ("0".equals(plantask.getString("status"))) {
                return ReturnCode.errorData(ReturnCode.Status.NO_OPERATOR);
            }


            plantask.set("status", STATUS_0);

            ActionHandler.currentSession().beginTrans();
            ActionHandler.currentSession().addOrUpdate(plantask);
            //删除关联表
            String sql = "update " + RE_TABLE_FULL_NAME + " set status='0' where plantask_id='" + plantask_id + "'";
            int result = ActionHandler.currentSession().executeUpdateSQL(sql);
            logger.info("[PlanTaskServiceImpl del]update ter_type , sql = " + sql + ",result = " + result);
            ActionHandler.currentSession().commit();
            return ReturnCode.successData();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("[PlanTaskServiceImpl del]", e);
            ActionHandler.currentSession().rollback();
            return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM);
        }
    }

    @Override
    public List<BizObject> listTaskByTerId(String terminal_id, String type, String status) throws SQLException {
        if (StringUtils.isBlank(terminal_id)) {
            return null;
        }
        StringBuffer sql = new StringBuffer("select ter.mer_no,ter.ter_no,pt.id,pt_ter.plantask_id,pt_ter.terminal_id,pt.plan_no,pt.type,pt.plan_name ,pt.template_id,pt.app_ver_id,apv.tms_app_ver,apv.app_id,ap.app_name from ");
        sql.append(RE_TABLE_FULL_NAME).append(" pt_ter ")//任务终端表
                .append(" left join ").append(TABLE_FULL_NAME).append(" pt on pt_ter.plantask_id=pt.id")
                .append(" left join ").append(AppVersionService.TABLE_FULL_NAME).append(" apv on apv.id=pt.app_ver_id")
                .append(" left join ").append(AppService.TABLE_FULL_NAME).append(" ap on  apv.app_id=ap.id ")
                .append(" left join ").append(TerminalsService.TABLE_FULL_NAME).append(" ter on ter.id=pt_ter.terminal_id ")
                .append(" where pt.status='1' and apv.status='1' and ap.status='1' ")//状态筛选
                .append(" and pt_ter.terminal_id=? ")

        ;
        List paramList = new ArrayList();
        paramList.add(terminal_id);
        if (StringUtils.isNotBlank(type)) {
            sql.append(" and pt.type=?");
            paramList.add(type);
        }
        if (StringUtils.isNotBlank(status)) {
            sql.append("  and pt_ter.status=?");
            paramList.add(status);
        }
//        sql.append(" order by pt_ter.modifydate desc ");
        List list = QueryFactory.executeQuerySQL(sql.toString(), paramList);
        logger.info("[PlanTaskServiceImpl listTaskByTerId]sql = " + sql + ", param = " + paramList);
        return list;
    }

    @Override
    public BizObject addOrUpdate(BizObject biz, List<BizObject> terminals) {
        if (StringUtils.isBlank(biz.getString("status"))) {
            biz.set("status", PlanTaskService.STATUS_1);
        }
        if (StringUtils.isBlank(biz.getString("ter_num_finish"))) {
            biz.set("ter_num_finish", "0");
        }
        if (STATUS_0.equals(biz.getString("status")) || STATUS_2.equals(biz.getString("status"))) {
            return ReturnCode.errorData(ReturnCode.ERROR_STATUS);
        }
        //新增时，默认未同步
        if (StringUtils.isBlank(biz.getString("id")) && StringUtils.isBlank(biz.getString("is_task_update"))) {
            biz.set("is_task_update", PlanTaskContext.TASK_UPDATE_NO);
        }
        try {
            //修改时，哪些字段变了表示任务更改过，需要重新同步
            if (StringUtils.isNotBlank(biz.getString("id"))) {
                BizObject the_obj = getById(biz.getString("id"));
                if (StringUtils.isNotBlank(biz.getString("mer_no")) && !biz.getString("mer_no").equals(the_obj.getString("mer_no"))) {
                    biz.set("is_task_update", PlanTaskContext.TASK_UPDATE_NO);
                }
                if (StringUtils.isNotBlank(biz.getString("ter_no")) && !biz.getString("ter_no").equals(the_obj.getString("ter_no"))) {
                    biz.set("is_task_update", PlanTaskContext.TASK_UPDATE_NO);
                }
            }
            //下载成功结果 未同步
            if (StringUtils.isBlank(biz.getString("is_task_finish"))) {
                biz.set("is_task_finish", PlanTaskContext.TASK_UPDATE_NO);
            }

            if (StringUtils.isBlank(biz.getString("plan_no"))) {
                biz.set("plan_no", BillNoGenerator.getFlowNo2("pn", 6).substring(2));
            }

            if (StringUtils.isNotBlank(biz.getString("app_ver_id")) && StringUtils.isNotBlank(biz.getString("template_id"))) {
                BizObject appVer = appVersionService.getById(biz.getString("app_ver_id"));
                BizObject temp = pTemplateService.getById(biz.getString("template_id"));
                BizObject app1 = appService.getById(appVer.getString("app_id"));
                BizObject app2 = appService.getById(temp.getString("app_id"));
                if (app1 == null || app2 == null) {
                    return ReturnCode.errorData("7778", "所选应用不存在");
                }
                if (appVer == null) {
                    return ReturnCode.errorData("7779", "所选应用版本不存在");
                }
                if (temp == null) {
                    return ReturnCode.errorData("7780", "所选应用参数模板不存在");
                }
                if (!appVer.getString("app_id").equals(temp.getString("app_id"))) {
                    return ReturnCode.errorData("7781", "所选应用版本和所选应用参数模板不为同一个应用");
                }
            }

            ActionHandler.currentSession().beginTrans();
            if (terminals != null && terminals.size() > 0) {
                //记录有多少个终端
                biz.set("ter_num", terminals.size());
            }
            ActionHandler.currentSession().addOrUpdate(biz);

            if (terminals != null && terminals.size() > 0) {
                //先删除存在的
                String sql = "update " + RE_TABLE_FULL_NAME + " set status='0' where plantask_id='" + biz.getString("id") + "'";
                int result = ActionHandler.currentSession().executeUpdateSQL(sql);
                logger.info("[PlanTaskServiceImpl addOrUpdate]update ter_type , sql = " + sql + ",result = " + result);

                //保存到关联表
                for (BizObject terminal : terminals) {
                    BizObject re_plantask_terminal = new BizObject(RE_TABLE_NAME);
                    re_plantask_terminal.set("plantask_id", biz.getString("id"));
                    re_plantask_terminal.set("terminal_id", terminal.getString("id"));
                    re_plantask_terminal.set("status", "1");
                    ActionHandler.currentSession().add(re_plantask_terminal);
                    logger.info("[PlanTaskServiceImpl addOrUpdate]add terminal,re_plantask_terminal = " + re_plantask_terminal);
                }
            }


            ActionHandler.currentSession().commit();
            return ReturnCode.successData(biz);
        } catch (SQLException e) {
            e.printStackTrace();
            ActionHandler.currentSession().rollback();
            return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM);
        }
    }
}
