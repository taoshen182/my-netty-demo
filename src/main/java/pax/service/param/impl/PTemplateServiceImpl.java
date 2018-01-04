package pax.service.param.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.app.AppService;
import pax.service.param.PTemplateService;
import pax.service.param.ParamService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.BillNoGenerator;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.SystemKit;
import tool.basic.BasicContext;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PTemplateServiceImpl implements PTemplateService {

    private Logger logger = Logger.getLogger(PTemplateServiceImpl.class);

    @Resource(name = "appService")
    private AppService appService;
    @Resource(name = "paramService")
    private ParamService paramService;

    @Override
    public String addOrUpdate(BizObject template) {

        try {
            if (template == null) throw new ErrorException(ReturnCode.ERROR_PARAM_NULL);


            String id = template.getId();
            //如果是新创建的，则给code和status初始化
            if (template != null && StringUtils.isBlank(id)) {
                template.set("code", BillNoGenerator.getFlowNo2("pt", 10).substring(2));
                template.set("status", "1");
            }


            //验证模板信息
            validate(template);

            ActionHandler.currentSession().beginTrans();
            ActionHandler.currentSession().addOrUpdate(template);

            ActionHandler.currentSession().commit();
            SystemKit.removeRelateCache("template");
            template.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.addOrUpdate:", e);
            ActionHandler.currentSession().rollback();

            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        } catch (ErrorException e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.addOrUpdate", e);
            ActionHandler.currentSession().rollback();

            if (e.getMessage().equals(ReturnCode.ERROR_PARAM_NULL))
                return ReturnCode.toString(ReturnCode.ERROR_PARAM_NULL, ReturnCode.ParamNull.TEMPLATE_NAME);
            else if (e.getMessage().equals(ReturnCode.ERROR_EXSIT))
                return ReturnCode.toString(ReturnCode.ERROR_EXSIT, ReturnCode.returnMap.get(ReturnCode.ERROR_EXSIT));
        }
        return ReturnCode.successData(template).toString();
    }


    @Override
    public String show(String id) {
        try {
            BizObject biz = this.getById(id);
            if (biz == null) return null;
            else {
//                biz.set("app_version_list", appService.queryVersionsByTemplateVersionId(id));
                return ReturnCode.successData(biz).toJsonString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.show:", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }


    @Override
    public String delRelation(String id) {
        try {
            BizObject biz = this.getRelationById(id);
            if (biz == null)
                ReturnCode.toString(ReturnCode.ERROR_NOTEXSIT, ReturnCode.returnMap.get(ReturnCode.ERROR_NOTEXSIT));

            ActionHandler.currentSession().beginTrans();
            biz.set("status", "0");
            ActionHandler.currentSession().update(biz);

            ActionHandler.currentSession().commit();
            return ReturnCode.toString(ReturnCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.delRelation:", e);
            ActionHandler.currentSession().rollback();
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1' ");

        List<Object> param_list = new ArrayList<>();
        if (param == null) param = new BizObject(TABLE_NAME);

        if (StringUtils.isNotBlank(param.getString("app_id"))) {
            sb.append(" and app_id = ?");
            param_list.add(param.getString("app_id"));
        }

//        sb.append(" order by modifydate desc");
        List list = QueryFactory.executeQuerySQL(sb.toString(), param_list, page);
        return list;
    }

    @Override
    public String getList(BizObject param, PageVariable page) {
        try {
            List<BizObject> list = this.query(param, page);
            BizObject ret = new BizObject();
            ret.set("psize", page.getPagesize());
            ret.set("rowcount", page.rowcount);
            ret.set("totalpage", page.totalpage);
            ret.set("npage", page.getNpage());
            ret.set("datas", list);
            return ReturnCode.successData(ret).toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.getList:", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }


    @Override
    public String addOrUpdateRelation(String template_id, List<BizObject> list, String[] param_ids) {
        try {
            if (list != null) {
                List<BizObject> errList = this.validateRelation(list);
                if (errList.size() > 0) {
                    BizObject ret = new BizObject();
                    ret.set("respmsg", errList);
                    ret.set("respcode", "0001");
                    return ret.toJsonString();
                }
            }

            ActionHandler.currentSession().beginTrans();
            if (list != null) {
                for (BizObject biz : list) {
                    biz.resetObjType(PTemplateService.TABLE_RELATION);
                    ActionHandler.currentSession().addOrUpdate(biz);
                }
            }
            if (param_ids != null) {
                for (String param_id : param_ids) {
                    BizObject biz = new BizObject(PTemplateService.TABLE_RELATION);
                    if (StringUtils.isBlank(param_id) || isExsitRelation(template_id, param_id)) continue;

                    biz.set("data_type", "text");
                    biz.set("length", 50);
                    biz.set("status", "1");
                    biz.set("template_id", template_id);
                    biz.set("param_id", param_id);

                    ActionHandler.currentSession().addOrUpdate(biz);
                }
            }
            ActionHandler.currentSession().commit();
            return ReturnCode.toString(ReturnCode.SUCCESS);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.addOrUpdateRelation:", e);
            ActionHandler.currentSession().rollback();
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.addOrUpdateRelation:", e);
            ActionHandler.currentSession().rollback();
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    @Override
    public String addCustomParam(String param_name, String termplate_version_id) {
        try {
            if (StringUtils.isBlank(termplate_version_id))
                return ReturnCode.toString(ReturnCode.ERROR_PARAM_NULL, ReturnCode.ParamNull.PTEMPLATE);
            String[] names = param_name.split(",");
            ActionHandler.currentSession().beginTrans();
            for (String name : names) {
                if (StringUtils.isBlank(name)) continue;
                String id = paramService.addCustomParam(name);
                BizObject biz = new BizObject(PTemplateService.TABLE_RELATION);
                biz.set("template_version_id", termplate_version_id);
                biz.set("param_id", id);
                biz.set("data_type", "text");
                biz.set("length", 50);
                biz.set("isfixed", "0");
                biz.set("status", "1");
                ActionHandler.currentSession().add(biz);
            }
            ActionHandler.currentSession().commit();
            return ReturnCode.toString(ReturnCode.SUCCESS);

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.addCustomRelation:", e);
            ActionHandler.currentSession().rollback();
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    @Override
    public String listParams(String template_id) {
        try {
            if (StringUtils.isBlank(template_id))
                return ReturnCode.toString(ReturnCode.ERROR_PARAM_NULL, ReturnCode.ParamNull.PTEMPLATE);

            StringBuilder sql = new StringBuilder("select t.id,t.template_id,t.def,t.param_id,p.code,p.group_name,t.data_type,p.def pdef,p.description,p.inst_id,"
                    + "t.length,p.name,p.operation_mode,p.type,t.isfixed,t.range_start,t.range_end,p.is_standard from ");
            sql.append(PTemplateService.TABLE_FULL_RELATION).append(" t,").append(ParamService.TABLE_FULL_NAME)
                    .append(" p where t.param_id=p.id and t.status='").append("1").append("' and template_id=? order by t.modifydate desc");
            List<Object> sqlList = new ArrayList<Object>();
            sqlList.add(template_id);
            List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), sqlList);

            for (BizObject biz : list) {
                BizObject param = paramService.showToBiz(biz.getString("param_id"));
                biz.set("code", param.getString("code"));
                biz.set("name", param.getString("name"));
                biz.set("group_name", param.getString("group_name"));
                biz.set("description", param.getString("description"));
                biz.set("type_text", param.getString("type_text"));
                biz.set("operation_mode_text", param.getString("operation_mode_text"));
                biz.set("data_range_list", getDataRangeList(biz.getID(), DATA_TYPE_PTEMPLATE));
                if (biz.getString("is_standard").equals(BasicContext.WHETHER_YES))
                    biz.set("custom_class", Bundle.getString("standard"));
                else biz.set("custom_class", Bundle.getString("custom"));
            }

            BizObject ret = new BizObject();
            ret.set("datas", list);
            return ReturnCode.successData(ret).toString();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.listParams:", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.listParams:", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    public boolean isExistByDef(String def) throws SQLException {
        QueryFactory qf = new QueryFactory(PTemplateService.TABLE_RELATION);
        BizObject biz = new BizObject(PTemplateService.TABLE_RELATION);
        biz.set("def", def);
        List list = qf.query(biz);
        if (list.size() > 0) return true;
        else return false;
    }

    @Override
    public String addCustom(String id, String range_name) {
        try {
//			if(StringUtils.isBlank(id) || StringUtils.isBlank(range_name)) return ReturnCode.toString(ReturnCode.ERROR_PARAM_NULL);
//			StringBuilder sql = new StringBuilder("select * from ");
//			sql.append(TABLE_FULL_DATA_RANGE).append(" where bill_id=? and name=? and bill_type='").append(DATA_TYPE_PTEMPLATE).append("'");
//			List<Object> sqlList = new ArrayList<Object>();
//			sqlList.add(id);
//			sqlList.add(range_name);
//			List list = QueryFactory.executeQuerySQL(sql.toString(),sqlList);
//			if(list.size()>0) return ReturnCode.toString(ReturnCode.ERROR_EXSIT);
//			else {
            ActionHandler.currentSession().beginTrans();
            String[] names = range_name.split(",");
            delCustom(id, DATA_TYPE_PTEMPLATE);
            BizObject ret = new BizObject();
            List<BizObject> list = new ArrayList<BizObject>();
            for (String s : names) {
                if (StringUtils.isNotBlank(s)) {
                    BizObject biz = new BizObject(TABLE_DATA_RANGE);
                    biz.set("bill_id", id);
                    biz.set("bill_type", DATA_TYPE_PTEMPLATE);
                    biz.set("name", s);
                    biz.set("times", new Date().getTime());
                    ActionHandler.currentSession().addOrUpdate(biz);
                    list.add(biz);
                }
            }
            if (list.size() < 1) return ReturnCode.toString(ReturnCode.ERROR_PARAM_NULL);
            ActionHandler.currentSession().commit();
            ret.set("data", list);
            return ReturnCode.successData(ret).toString();
//			}
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.addCustom:", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PTemplateServiceImpl.addCustom:", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    /**
     * 根据标准的模版生成一个指定APP的模版（生成的是非标准的）
     *
     * @param name
     * @param template_version_id
     * @return
     * @throws SQLException
     */
    public String addCustomTemplateVersion(String name, String template_version_id) throws SQLException {
        BizObject template_version = this.getById(template_version_id);
        template_version.set("name", template_version.getString("name") + "  " + name);
        template_version.set("is_standard", BasicContext.WHETHER_NO);
        template_version.setID("");
        ActionHandler.currentSession().addOrUpdate(template_version);
        this.copyTemplateParam(template_version_id, template_version.getId());
        return template_version.getId();
    }

    private void delCustom(String id, String bill_type) throws SQLException {
        StringBuilder sql = new StringBuilder("delete from ");
        sql.append(TABLE_FULL_DATA_RANGE).append(" where bill_id=? and bill_type=?");
        List<Object> sqlList = new ArrayList<Object>();
        sqlList.add(id);
        sqlList.add(bill_type);
        QueryFactory.executeUpdateSQL(sql.toString(), sqlList);
    }

    private List<BizObject> query(BizObject param, PageVariable page) throws SQLException, ParseException {
        List<BizObject> list = new ArrayList<BizObject>();
        if (param == null) param = new BizObject(PTemplateService.TABLE_NAME);
        StringBuffer strSql = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
        strSql.append("select tv.*,(case when (select count(*) from ").append(TABLE_FULL_RELATION).append(" rt  where tv.id=rt.param_id )>0 then 1 else 0 end ) as is_have").append(" from ").append(PTemplateService.TABLE_FULL_NAME).append(" tv where status='")
                .append("1").append("'");

        if (StringUtils.isNotBlank(param.getString("code"))) {
            strSql.append(" and tv.code like ?");
            params.add(param.getString("code"));
        }
        if (StringUtils.isNotBlank(param.getString("name"))) {
            strSql.append(" and tv.name like ?");
            params.add("%" + param.getString("name") + "%");
        }
        if (StringUtils.isNotBlank(param.getString("inst_id"))) {
            strSql.append(" and tv.inst_id like ?");
            params.add("%" + param.getString("inst_id") + "%");
        }
        if (StringUtils.isNotBlank(param.getString("searchkey"))) {
            strSql.append(" and (tv.name like ? or tv.code like ?)");
            params.add("%" + param.getString("name") + "%");
            params.add("%" + param.getString("code") + "%");
        }
//        strSql.append(" order by modifydate desc");

        if (page != null) list = QueryFactory.executeQuerySQL(strSql.toString(), params, page);
        else list = QueryFactory.executeQuerySQL(strSql.toString(), params);
        logger.info("PTemplateServiceImpl.query[List]" + list);
        logger.info("sql : " + strSql.toString());
        List<BizObject> appList = null;
        for (BizObject biz : list) {
//            appList = appService.queryVersionsByTemplateVersionId(biz.getId());
            if (appList != null) {
                if (appList.size() > 1) biz.set("app_versions", Bundle.getString("multiple_versions"));
                else if (appList.size() == 1)
                    biz.set("app_versions", appList.get(0).getString("name") + "  " + appList.get(0).getString("version"));
                else biz.set("app_versions", "-");
            }
            biz.set("status_text", Bundle.getParamById("status", biz.getString("status")));
            if (StringUtils.isNotBlank(biz.getString("createdate")))
                biz.set("createdate_text", DateUtils.formatDate(biz.getDate("createdate"), DateUtils.PATTERN_YYYYMMDDHHMMSS));
            if (StringUtils.isNotBlank(biz.getString("modifydate")))
                biz.set("modifydate_text", DateUtils.formatDate(biz.getDate("modifydate"), DateUtils.PATTERN_YYYYMMDDHHMMSS));
        }
        return list;
    }

    public BizObject getById(String id) throws SQLException {
        QueryFactory qf = new QueryFactory(TABLE_NAME);
        return qf.getByID(id);
    }

    private BizObject getRelationById(String id) throws SQLException {
        QueryFactory qf = new QueryFactory(PTemplateService.TABLE_RELATION);
        return qf.getByID(id);
    }

    private boolean validate(BizObject template) throws SQLException, ErrorException {
        if (template == null) throw new ErrorException(ReturnCode.ERROR_PARAM_NULL);
        if (StringUtils.isBlank(template.getString("name"))) throw new ErrorException(ReturnCode.ERROR_PARAM_NULL);

        List<Object> sqlList = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("select * from ");
        sql.append(TABLE_FULL_NAME).append(" where name=? and status='").append("1").append("' and version=?");
        sqlList.add(template.getString("name"));
        sqlList.add(template.getString("version"));

        if (StringUtils.isNotBlank(template.getString("inst_id"))) {
            sql.append(" and inst_id=?");
            sqlList.add(template.getString("inst_id"));
        } else sql.append(" and inst_id is null");

        if (StringUtils.isNotBlank(template.getId())) {
            sql.append(" and id!=?");
            sqlList.add(template.getId());
        }

        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), sqlList);
        if (list != null && list.size() > 0) throw new ErrorException(ReturnCode.ERROR_EXSIT);
        return true;
    }

    private List<BizObject> validateRelation(List<BizObject> list) {
        List<BizObject> errList = new ArrayList<BizObject>();

        for (BizObject biz : list) {
            biz.resetObjType(PTemplateService.TABLE_RELATION);
            //判断超出长度限制
            if (StringUtils.isNotBlank(biz.getString("length"))) {
                int def_len = -1;
                try {
                    def_len = Integer.parseInt(biz.getString("length"));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("错误参数 " + biz.getString("name") + "：", e);
                    def_len = -1;
                }
                if (def_len == -1 || biz.getString("def").length() > def_len) {
                    biz.set("errMsg", biz.getString("name") + "   " + Bundle.getString("error.param_length"));//超出长度限制
                }
            }

            if (biz.getString("data_type").equals("int")) {
                if (StringUtils.isNotBlank("range_start") && StringUtils.isNotBlank("range_end")) {//如果范围不为空
                    int range_start = -1;
                    int range_end = -1;
                    try {
                        range_start = Integer.parseInt(biz.getString("range_start"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("错误参数 " + biz.getString("name") + "：", e);
                        range_start = -1;
                    }
                    try {
                        range_end = Integer.parseInt(biz.getString("range_end"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("错误参数 " + biz.getString("name") + "：", e);
                        range_end = -1;
                    }
                    if (range_start == -1 || range_end == -1 || range_start == range_end) {
                        if (StringUtils.isNotBlank(biz.getString("errMsg")))
                            biz.set("errMsg", biz.getString("errMsg") + ";" + biz.getString("name") + "   " + Bundle.getString("error.param_range"));
                        else
                            biz.set("errMsg", biz.getString("name") + "   " + Bundle.getString("error.param_range"));//范围设置错误
                    }

                    if (StringUtils.isNotBlank(biz.getString("def"))) {
                        int def = -1;
                        try {
                            def = Integer.parseInt(biz.getString("def"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("错误参数 " + biz.getString("name") + "：", e);
                            def = -1;
                        }

                        if (def == -1 || def < range_start || def > range_end) {
                            if (StringUtils.isNotBlank(biz.getString("errMsg")))
                                biz.set("errMsg", biz.getString("errMsg") + ";" + biz.getString("name") + "   " + Bundle.getString("error.param_range"));
                            else
                                biz.set("errMsg", biz.getString("name") + "   " + Bundle.getString("error.param_def"));//默认值超出范围
                        }
                    }
                }
            }

            if (biz.getString("data_type").equals("float")) {
                if (StringUtils.isNotBlank("range_start") && StringUtils.isNotBlank("range_end")) {//如果范围不为空
                    double range_start = 0;
                    double range_end = 0;
                    try {
                        range_start = Double.parseDouble(biz.getString("range_start"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("错误参数 " + biz.getString("name") + "：", e);
                        range_start = -1;
                    }
                    try {
                        range_end = Double.parseDouble(biz.getString("range_end"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("错误参数 " + biz.getString("name") + "：", e);
                        range_end = -1;
                    }
                    if (range_start == -1 || range_end == -1 || range_start == range_end) {
                        if (StringUtils.isNotBlank(biz.getString("errMsg")))
                            biz.set("errMsg", biz.getString("errMsg") + ";" + biz.getString("name") + "   " + Bundle.getString("error.param_range"));
                        else
                            biz.set("errMsg", biz.getString("name") + "   " + Bundle.getString("error.param_range"));//范围设置错误
                    }

                    if (StringUtils.isNotBlank(biz.getString("def"))) {
                        double def = -1;
                        try {
                            def = Double.parseDouble(biz.getString("def"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("错误参数 " + biz.getString("name") + "：", e);
                            def = -1;
                        }

                        if (def == -1 || def < range_start || def > range_end) {
                            if (StringUtils.isNotBlank(biz.getString("errMsg")))
                                biz.set("errMsg", biz.getString("errMsg") + ";" + biz.getString("name") + "   " + Bundle.getString("error.param_range"));
                            else
                                biz.set("errMsg", biz.getString("name") + "   " + Bundle.getString("error.param_def"));//默认值超出范围
                        }
                    }
                }
            }

            if (biz.getString("data_type").equals("select")) {
                try {
                    List li = this.getDataRangeList(biz.getString("id"), DATA_TYPE_PTEMPLATE);
                    if (li.size() <= 0) {
                        if (StringUtils.isNotBlank(biz.getString("errMsg")))
                            biz.set("errMsg", biz.getString("errMsg") + ";" + biz.getString("name") + "   " + Bundle.getString("error.param_select"));
                        else
                            biz.set("errMsg", biz.getString("name") + "   " + Bundle.getString("error.param_select"));//未设置下拉选项
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (StringUtils.isNotBlank(biz.getString("errMsg"))) errList.add(biz);
        }
        return errList;
    }

    private boolean isExsitRelation(String template_id, String param_id) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(PTemplateService.TABLE_FULL_RELATION).append(" where template_id=? and param_id=? and status='")
                .append("1").append("'");
        List<Object> sqlList = new ArrayList<Object>();
        sqlList.add(template_id);
        sqlList.add(param_id);
        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), sqlList);
        if (list.size() > 0) return true;
        return false;
    }

    private void copyTemplateParam(String template_version_id, String new_template_version_id) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(PTemplateService.TABLE_FULL_RELATION).append(" where template_version_id=? and status='")
                .append("1").append("'");
        List<Object> sqlList = new ArrayList<Object>();
        sqlList.add(template_version_id);
        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), sqlList);
        for (BizObject biz : list) {
            biz.setID("");
            biz.set("template_version_id", new_template_version_id);
            ActionHandler.currentSession().addOrUpdate(biz);
        }
    }

    private List<BizObject> getDataRangeList(String bill_id, String bill_type) throws SQLException {
        StringBuilder sql = new StringBuilder("select name id,name from ");
        sql.append(TABLE_FULL_DATA_RANGE).append(" where bill_id=? and bill_type=? order by times ");
        List<Object> sqlList = new ArrayList<Object>();
        sqlList.add(bill_id);
        sqlList.add(bill_type);

        return QueryFactory.executeQuerySQL(sql.toString(), sqlList);
    }


    @Override
    public List<BizObject> queryParams(String template_version_id) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT (case when t.def is null then p.def else t.def end) value,p.name from ");
        sql.append(PTemplateService.TABLE_FULL_RELATION).append(" t,").append(ParamService.TABLE_FULL_NAME)
                .append(" p where t.param_id=p.id and t.status='").append("1").append("' and template_id=?");
        List<Object> sqlList = new ArrayList<Object>();
        sqlList.add(template_version_id);

        logger.info("PTemplateServiceImpl.queryParams sql=" + sql.toString());
        logger.info("PTemplateServiceImpl.queryParams[sqlList]" + sqlList.toString());

        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), sqlList);
        return list;

    }

    public void delCustomTemplate(String template_version_id) throws SQLException {
        BizObject template_version = this.getById(template_version_id);
        //如果模版为空，或者模版不是自定义模版（非标准模版）
        if (template_version == null || !template_version.getString("is_standard").equals(BasicContext.WHETHER_NO))
            return;

        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(PTemplateService.TABLE_FULL_RELATION).append(" where template_version_id=?");
        List<Object> sqlList = new ArrayList<Object>();
        sqlList.add(template_version_id);
        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), sqlList);
        for (BizObject biz : list) {
            ActionHandler.currentSession().delete(biz);
        }
        ActionHandler.currentSession().delete(template_version);
    }

    public List<BizObject> getSelectTemplateVersion(String template_version_id, String inst_id) throws SQLException {
        StringBuilder sql = new StringBuilder("select id,concat(name,'      ',version) name from ").append(TABLE_FULL_NAME).append(" where status='1' and (is_standard='1' or id=?)");
        List<Object> param = new ArrayList<Object>();
        param.add(template_version_id);
        if (StringUtils.isNotBlank(inst_id)) {
            sql.append(" and (inst_id=? or inst_id is null)");
            param.add(inst_id);
        }
        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), param);
        return list;
    }

    public void setText(BizObject biz) throws SQLException {
        if (StringUtils.isNotBlank(biz.getString("type")))
            biz.set("type_text", Bundle.getParamById("param_type", biz.getString("type")));
        biz.set("status_text", Bundle.getParamById("status", biz.getString("status")));

        if (StringUtils.isNotBlank(biz.getString("modifydate")))
            biz.set("modifydate_text", DateUtils.formatDate(biz.getDate("modifydate"), DateUtils.PATTERN_YYYYMMDDHHMMSS));
        if (StringUtils.isNotBlank(biz.getString("createdate")))
            biz.set("createdate_text", DateUtils.formatDate(biz.getDate("createdate"), DateUtils.PATTERN_YYYYMMDDHHMMSS));
    }
}

