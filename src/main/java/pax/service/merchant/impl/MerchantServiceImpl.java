package pax.service.merchant.impl;

import org.apache.commons.lang.StringUtils;
import pax.service.merchant.MerchantService;
import sand.actionhandler.system.ActionHandler;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/6/16.
 */
public class MerchantServiceImpl implements MerchantService {
    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where 1=1 ");

        List<Object> paramList = new ArrayList<>();
        if (param == null) {
            param = new BizObject();
        }
        if (StringUtils.isNotBlank(param.getString("status"))) {
            sb.append(" and status = ? ");
            paramList.add(param.getString("status"));
        }
        if (StringUtils.isNotBlank(param.getString("startdate"))) {
            sb.append(" and createdate > ? ");
            paramList.add(param.getString("startdate"));
        }
        if (StringUtils.isNotBlank(param.getString("enddate"))) {
            sb.append(" and createdate < ? ");
            paramList.add(param.getString("enddate"));
        }
        if (StringUtils.isNotBlank(param.getString("name"))) {
            sb.append(" and name like ?");
            paramList.add("%" + param.getString("name") + "%");
        }

        List list = QueryFactory.executeQuerySQL(sb.toString(), paramList, page);
        return list;
    }

    @Override
    public BizObject getById(String id) throws SQLException {
        QueryFactory qf = new QueryFactory(TABLE_NAME);
        return qf.getByID(id);
    }

    @Override
    public BizObject addOrUpdate(BizObject biz) throws SQLException {
        if (StringUtils.isBlank(biz.getString("status"))) {
            biz.set("status", "1");
        }
        ActionHandler.currentSession().addOrUpdate(biz);
        return biz;
    }

    @Override
    public BizObject delById(String id) throws SQLException {
        return null;
    }
}
