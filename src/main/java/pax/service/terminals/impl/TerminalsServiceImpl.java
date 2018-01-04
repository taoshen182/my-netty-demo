package pax.service.terminals.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pax.service.ReturnCode;
import pax.service.tercompany.TerCompanyService;
import pax.service.terminals.TerminalsService;
import pax.service.tertype.TerTypeService;
import sand.actionhandler.system.ActionHandler;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by want on 2017/6/13.
 */
public class TerminalsServiceImpl implements TerminalsService {
    static Logger logger = Logger.getLogger(TerminalsServiceImpl.class);

    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where 1=1 ");

        List<Object> param_list = new ArrayList<>();
        if (param == null) param = new BizObject(TABLE_NAME);

        if (StringUtils.isNotBlank(param.getString("ter_com_id"))) {
            sb.append(" and ter_com_id=?");
            param_list.add(param.getString("ter_com_id"));
        }
        if (StringUtils.isNotBlank(param.getString("ter_type_id"))) {
            sb.append(" and ter_type_id=?");
            param_list.add(param.getString("ter_type_id"));
        }
        if (StringUtils.isNotBlank(param.getString("ter_status"))) {
            sb.append(" and ter_status=?");
            param_list.add(param.getString("ter_status"));
        }
        if (StringUtils.isNotBlank(param.getString("sn"))) {
            sb.append(" and sn like ?");
            param_list.add("%" + param.getString("sn") + "%");
        }

        if (StringUtils.isNotBlank(param.getString("startdate"))) {
            sb.append(" and createdate > ? ");
            param_list.add(param.getString("startdate"));
        }
        if (StringUtils.isNotBlank(param.getString("enddate"))) {
            sb.append(" and createdate < ? ");
            param_list.add(param.getString("enddate"));
        }
        //多个ter_type
        if (StringUtils.isNotBlank(param.getString("ter_type_ids"))) {
            String[] ter_type_id_array = param.getString("ter_type_ids").split(",");

            sb.append(" and ter_type_id in(");
            for (int i = 0; i < ter_type_id_array.length; i++) {
                if (0 == i) {
                    sb.append("?");
                } else {
                    sb.append(",?");
                }
                param_list.add(ter_type_id_array[i]);
            }
            sb.append(")");
        }
//        sb.append(" order by modifydate desc ");

        List list = QueryFactory.executeQuerySQL(sb.toString(), param_list, page);
        return list;
    }

    @Override
    public BizObject getById(String id) throws SQLException {
        QueryFactory qf = new QueryFactory(TABLE_NAME);
        return qf.getByID(id);
    }

    @Override
    public BizObject addOrUpdate(BizObject biz) throws SQLException {
        if (StringUtils.isBlank(biz.getString("ter_status"))) {
            biz.set("ter_status", "1");
        }
        //判断sn是否存在
        if (isExit(biz)) return ReturnCode.errorData("7783", "SN号已存在");

        ActionHandler.currentSession().addOrUpdate(biz);
        return biz;
    }

    private boolean isExit(BizObject biz) throws SQLException {
        StringBuffer sql = new StringBuffer(" select * from ");
        sql.append(TABLE_FULL_NAME).append(" where  sn=?");

        List paramList = new ArrayList();
        paramList.add(biz.getString("sn"));
        if (StringUtils.isNotBlank(biz.getString("id"))) {
            sql.append(" and id!=?");
            paramList.add(biz.getString("id"));
        }

        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), paramList);
        if (list == null || list.size() <= 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public BizObject delById(String id) throws SQLException {
        return null;
    }

    @Override
    public BizObject getTerminal(String mid, String sn, String fid) throws SQLException {
        logger.info("[TerminalsServiceImpl isSNExist]param:mid=" + mid + ",sn=" + sn + ",fid=" + fid);

        if (StringUtils.isBlank(mid) || StringUtils.isBlank(sn) || StringUtils.isBlank(fid)) return null;
        StringBuffer sql = new StringBuffer("select * from ")
                .append(TerminalsService.TABLE_FULL_NAME).append(" tm, ")
                .append(TerTypeService.TABLE_FULL_NAME).append(" tp, ")
                .append(TerCompanyService.TABLE_FULL_NAME).append(" tc ");
        sql.append(" where tm.ter_com_id = tc.id and tm.ter_type_id=tp.id and tm.ter_status='1' and tp.status='1' and tc.status='1' ")
                .append(" and tp.ter_co = ? and tm.sn=? and tc.com_no=? ");

        List paramList = new ArrayList();
        paramList.add(mid);
        paramList.add(sn);
        paramList.add(fid);
        logger.info("[TerminalsServiceImpl isSNExist]paramList=" + paramList + ",sql=" + sql);
        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), paramList);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public BizObject getTerminal(String sn) throws SQLException {
        logger.info("[TerminalsServiceImpl isSNExist]param: " + ",sn=" + sn);

        if (StringUtils.isBlank(sn)) return null;
        StringBuffer sql = new StringBuffer("select * from ")
                .append(TerminalsService.TABLE_FULL_NAME).append(" tm ")
                .append(" where  ")
                .append(" tm.sn=? ");

        List paramList = new ArrayList();

//        paramList.add(TerminalsService.TER_STATUS_0);
//        paramList.add(TerminalsService.TER_STATUS_1);
        paramList.add(sn);

        logger.info("[TerminalsServiceImpl isSNExist]paramList=" + paramList + ",sql=" + sql);
        List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), paramList);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
