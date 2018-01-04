package pax.service.param.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.inst.impl.InstServiceImpl;
import pax.service.param.ParamService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.BillNoGenerator;
import sand.depot.tool.system.ErrorException;
import tool.basic.BasicContext;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pax.service.param.PTemplateService.TABLE_FULL_RELATION;

public class ParamServiceImpl implements ParamService {

    Logger logger = Logger.getLogger(ParamServiceImpl.class);

    @Resource(name = "instService")
    public InstServiceImpl instService;

    @Override
    public BizObject getById(String id) throws SQLException {
        logger.info("ParamServiceImpl.getById[id]" + id);
        QueryFactory qf = new QueryFactory(TABLE_NAME);
        return qf.getByID(id);
    }

    @Override
    public BizObject getByName(String name) throws SQLException {
        String sql = "select * from " + TABLE_FULL_NAME + " where name=?";
        Object[] param = new Object[]{name};

        logger.info("ParamServiceImpl.getByName[sql]" + sql);
        logger.info("ParamServiceImpl.getByName[param]" + Arrays.toString(param));
        return QueryFactory.getOne(sql.toString(), param);
    }

    @Override
    public String addOrUpdate(BizObject param) {
        try {
            validate(param);
            if (StringUtils.isBlank(param.getId())) {
                param.set("code", BillNoGenerator.getFlowNo2("pm", 10).substring(2));
                param.set("status","1");
                param.set("is_standard", BasicContext.WHETHER_YES);
            }
            ActionHandler.currentSession().addOrUpdate(param);
            param.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        } catch (ErrorException e) {
            e.printStackTrace();
            logger.error("", e);

            if (e.getMessage().equals(ReturnCode.ERROR_PARAM_NULL))
                return ReturnCode.toString(ReturnCode.ERROR_PARAM_NULL, ReturnCode.ParamNull.PARAM_NAME);
            else if (e.getMessage().equals(ReturnCode.ERROR_EXSIT))
                return ReturnCode.toString(ReturnCode.ERROR_EXSIT, ReturnCode.returnMap.get(ReturnCode.ERROR_EXSIT));
        }
        return ReturnCode.successData(param).toString();
    }

    @Override
    public String show(String id) {
        try {
            BizObject biz = this.getById(id);
            if (biz == null) return null;
            else {
                this.setText(biz);
                return ReturnCode.successData(biz).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    @Override
    public BizObject showToBiz(String id) {
        try {
            BizObject biz = this.getById(id);
            if (biz == null) return null;
            else {
                this.setText(biz);
                return biz;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            return null;
        }
    }

    @Override
    public String del(String id) {
        try {
            BizObject biz = this.getById(id);
            if (biz == null)
                ReturnCode.toString(ReturnCode.ERROR_NOTEXSIT, ReturnCode.returnMap.get(ReturnCode.ERROR_NOTEXSIT));

            biz.set("status", "0");
            ActionHandler.currentSession().update(biz);
            return ReturnCode.toString(ReturnCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    @Override
    public String getList(BizObject param, PageVariable page) {
        try {
            List<BizObject> list = this.query(param, page);
            BizObject ret = new BizObject();
            ret.set("datas", list);

            ret.set("psize", page.getPagesize());
            ret.set("rowcount", page.rowcount);
            ret.set("totalpage", page.totalpage);
            ret.set("npage", page.getNpage());

            return ReturnCode.successData(ret).toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    @Override
    public String getSimpleList(BizObject param, PageVariable page) {
        try {
            List<BizObject> list = this.query(param, page);

            for (BizObject biz : list) {
//				biz.set("type_text", AppContext.PARAM_TYPE_MAP.get(biz.getString("type")));
                biz.set("instname", instService.getById(biz.getString("inst_id")).getString("name"));
//				biz.set("data_type_text", AppContext.DATA_TYPE_MAP.get(biz.getString("data_type")));
                biz.remove("description");
                biz.remove("operation_mode");
                biz.remove("status");
                biz.remove("creator");
                biz.remove("createdate");
                biz.remove("modifier");
                biz.remove("modifydate");
            }
            BizObject ret = new BizObject();
            ret.set("datas", list);
            return ReturnCode.successData(ret).toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    private List<BizObject> query(BizObject param, PageVariable page) throws SQLException, ParseException {
        List<BizObject> list = new ArrayList<BizObject>();
        if (param == null) param = new BizObject(ParamService.TABLE_NAME);
        StringBuffer strSql = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
        strSql.append("select app.*,(case when (select count(*) from ").append(TABLE_FULL_RELATION).append(" rt  where app.id=rt.param_id )>0 then 1 else 0 end ) as is_have").append(" from ").append(ParamService.TABLE_FULL_NAME).append(" app ").append("  where status='")
                .append("1").append("' and is_standard='").append(BasicContext.WHETHER_YES).append("'");

        if (StringUtils.isNotBlank(param.getString("code"))) {
            strSql.append(" and app.code like ?");
            params.add(param.getString("code"));
        }
        if (StringUtils.isNotBlank(param.getString("name"))) {
            strSql.append(" and app.name like ?");
            params.add("%" + param.getString("name") + "%");
        }
        if (StringUtils.isNotBlank(param.getString("group_name"))) {
            strSql.append(" and app.group_name like ?");
            params.add("%" + param.getString("group_name") + "%");
        }
        if (StringUtils.isNotBlank(param.getString("type"))) {
            strSql.append(" and app.type = ?");
            params.add(param.getString("type"));
        }
        if (StringUtils.isNotBlank(param.getString("inst_id"))) {
            strSql.append(" and app.inst_id = ?");
            params.add(param.getString("inst_id"));
        }
        if (StringUtils.isNotBlank(param.getString("searchkey"))) {
            strSql.append(" and (app.name like ? or app.code like ? or app.group_name like ?)");
            params.add("%" + param.getString("name") + "%");
            params.add("%" + param.getString("code") + "%");
            params.add("%" + param.getString("group_name") + "%");
        }

//        strSql.append(" order by modifydate desc");

        if (page != null) list = QueryFactory.executeQuerySQL(strSql.toString(), params, page);
        else list = QueryFactory.executeQuerySQL(strSql.toString(), params);
        logger.info("ParamServiceImpl.query[List]" + list);
        logger.info("sql : " + strSql.toString());
        for (BizObject biz : list) this.setText(biz);

        return list;
    }

    @Override
    public String getList(String code, String name, String type, String inst_id, PageVariable page) {
        try {
            QueryFactory qf = new QueryFactory(ParamService.TABLE_NAME);
            StringBuilder sql = new StringBuilder("select * from ");
            sql.append(ParamService.TABLE_FULL_NAME).append(" where 1=1 and status='").append("1").append("' and is_standard='")
                    .append(BasicContext.WHETHER_YES).append("'");

            List<Object> sqlList = new ArrayList<Object>();
            if (StringUtils.isNotBlank(code)) {
                sql.append(" and code like ? ");
                sqlList.add("%" + code + "%");
            }
            if (StringUtils.isNotBlank(name)) {
                sql.append(" and name like ? ");
                sqlList.add("%" + name + "%");
            }
            if (StringUtils.isNotBlank(type)) {
                sql.append(" and type = ? ");
                sqlList.add(type);
            }
            if (StringUtils.isNotBlank(inst_id)) {
                sql.append(" and inst_id = ? ");
                sqlList.add(inst_id);
            }

            List<BizObject> list = new ArrayList<BizObject>();
            qf.setOrderBy("modifydate desc");
            if (page != null)
                list = qf.executeQuerySQL(sql.toString(), sqlList, page, ActionHandler.currentSession().getCon());
            else list = qf.executeQuerySQL(sql.toString(), sqlList);
            BizObject ret = new BizObject();
            ret.set("datas", list);
            return ReturnCode.successData(ret).toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            return ReturnCode.toString(ReturnCode.ERROR_SYSTEM);
        }
    }

    public String addCustomParam(String name) throws SQLException {
        BizObject param = new BizObject(ParamService.TABLE_NAME);
        param.set("code", BillNoGenerator.getFlowNo2("pa", 10).substring(2));
        param.set("name", name);
        param.set("type", "1");
        param.set("operation_mode", "0");//后台操作
        param.set("is_standard", BasicContext.WHETHER_NO);
        param.set("status", "1");
        ActionHandler.currentSession().add(param);
        return param.getId();
    }

    private boolean validate(BizObject param) throws SQLException, ErrorException {
        if (StringUtils.isBlank(param.getString("name"))) throw new ErrorException(ReturnCode.ERROR_PARAM_NULL);

        List<Object> sqlList = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("select * from ");
        sql.append(TABLE_FULL_NAME).append(" where name=? and status='").append("1").append("' and is_standard!='")
                .append(BasicContext.WHETHER_NO).append("'");
        sqlList.add(param.getString("name"));

        if (StringUtils.isNotBlank(param.getString("inst_id"))) {
            sql.append(" and inst_id=?");
            sqlList.add(param.getString("inst_id"));
        } else sql.append(" and inst_id is null");

        if (StringUtils.isNotBlank(param.getId())) {
            sql.append(" and id!=?");
            sqlList.add(param.getId());
        }

        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), sqlList);
        if (list.size() > 0) throw new ErrorException(ReturnCode.ERROR_EXSIT);
        return true;
    }

    public void setText(BizObject biz) throws SQLException {
        if (StringUtils.isNotBlank(biz.getString("type")))
            biz.set("type_text", Bundle.getParamById("param_type", biz.getString("type")));
        biz.set("operation_mode_text", Bundle.getParamById("operation_mode", biz.getString("operation_mode")));
        biz.set("status_text", Bundle.getParamById("status", biz.getString("status")));

        if (StringUtils.isNotBlank(biz.getString("modifydate")))
            biz.set("modifydate_text", DateUtils.formatDate(biz.getDate("modifydate"), DateUtils.PATTERN_YYYYMMDDHHMMSS));
    }

}
