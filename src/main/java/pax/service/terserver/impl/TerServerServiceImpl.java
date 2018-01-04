package pax.service.terserver.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pax.service.ReturnCode;
import pax.service.tercompany.TerCompanyService;
import pax.service.terserver.TerServerService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.BillNoGenerator;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by want on 2017/6/2.
 */
public class TerServerServiceImpl implements TerServerService {
    static Logger logger = Logger.getLogger(TerServerServiceImpl.class);

    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sql = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1' ");

        List<Object> param_list = new ArrayList<>();
        if (param == null) param = new BizObject(TABLE_NAME);

        if (StringUtils.isNotBlank(param.getString("type"))) {
            if (param.getString("type").equals("01")) {
                sql.append(" and (type='0' or type='1') ");
            } else {
                sql.append(" and type=? ");
                param_list.add(param.getString("type"));
            }
        }

        if (StringUtils.isNotBlank(param.getString("datemax"))) {
            sql.append(" and createdate<=? ");
            param_list.add(param.getString("datemax"));
        }
        if (StringUtils.isNotBlank(param.getString("datemin"))) {
            sql.append(" and createdate>=? ");
            param_list.add(param.getString("datemin"));
        }
        if (StringUtils.isNotBlank(param.getString("server_name"))) {
            sql.append(" and server_name like ? ");
            param_list.add("%" + param.getString("server_name") + "%");
        }
        if (StringUtils.isNotBlank(param.getString("inst_id"))) {
            sql.append(" and inst_id = ? ");
            param_list.add(param.getString("inst_id"));
        }

        logger.info("[TerServerServiceImpl list]sql=" + sql + ",param=" + param_list + ",page=" + page);
        List list = QueryFactory.executeQuerySQL(sql.toString(), param_list, page);
        return list;
    }

    @Override
    public BizObject getById(String id) throws SQLException {
        QueryFactory qf = new QueryFactory(TABLE_NAME);
        return qf.getByID(id);
    }

    @Override
    public BizObject addOrUpdate(BizObject terServer) throws SQLException {
        if (StringUtils.isBlank(terServer.getString("server_name")) || StringUtils.isBlank(terServer.getString("type"))) {
            return ReturnCode.errorData(ReturnCode.ERROR_PARAM_NULL);
        }
        if (StringUtils.isBlank(terServer.getString("status"))) {
            terServer.set("status", "1");
        }
        if (StringUtils.isBlank(terServer.getString("server_no"))) {
            terServer.set("server_no", BillNoGenerator.getFlowNo2("sv", 6).substring(2));
        }

        ActionHandler.currentSession().addOrUpdate(terServer);
        return terServer;
    }

    @Override
    public BizObject delById(String id) throws SQLException {
        return null;
    }


}
