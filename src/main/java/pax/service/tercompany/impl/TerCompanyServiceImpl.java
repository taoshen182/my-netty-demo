package pax.service.tercompany.impl;

import org.apache.commons.lang3.StringUtils;
import pax.service.tercompany.TerCompanyService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/6/5.
 */
public class TerCompanyServiceImpl implements TerCompanyService {
    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1'");
        if (param == null) {
            param = new BizObject();
        }
        List<Object> paramList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getString("startdate"))) {
            sb.append(" and createdate > ? ");
            paramList.add(param.getString("startdate"));
        }
        if (StringUtils.isNotBlank(param.getString("enddate"))) {
            sb.append(" and createdate < ? ");
            paramList.add(param.getString("enddate"));
        }
        if (StringUtils.isNotBlank(param.getString("com_name"))) {
            sb.append(" and com_name like ? ");
            paramList.add("%" + param.getString("com_name") + "%");
        }

        List list = QueryFactory.executeQuerySQL(sb.toString(), paramList, page);
        return list;


    }

    @Override
    public BizObject getById(String id) throws SQLException {
        return new QueryFactory(TABLE_NAME).getByID(id);
    }

    @Override
    public BizObject addOrUpdate(BizObject biz) throws SQLException {
        if (StringUtils.isBlank(biz.getString("status"))) {
            biz.set("status", "1");
        }
        ActionHandler.currentSession().addOrUpdate(biz);

        //重新加载视图
        SystemKit.removeCache("ter_company");
        return biz;
    }

    @Override
    public BizObject delById(String id) throws SQLException {
        return null;
    }
}
