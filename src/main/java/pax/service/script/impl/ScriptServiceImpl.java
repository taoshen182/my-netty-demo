package pax.service.script.impl;

import org.apache.commons.lang3.StringUtils;
import pax.service.script.ScriptService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/7/18.
 */
public class ScriptServiceImpl implements ScriptService {
    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1'");
        if (null == param) {
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
        if (StringUtils.isNotBlank(param.getString("pck_name"))) {
            sb.append(" and pck_name like ? ");
            paramList.add("%" + param.getString("pck_name") + "%");
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
//        SystemKit.removeCache("ter_company");
        return biz;
    }
}
