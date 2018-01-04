package pax.service.tpm.impl;

import org.apache.commons.lang.StringUtils;
import pax.service.tpm.TpmService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/6/13.
 */
public class TpmServiceImpl implements TpmService {
    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1' ");

        List<Object> param_list = new ArrayList<>();

        if (param == null) param = new BizObject(TABLE_NAME);

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
        if (StringUtils.isBlank(biz.getString("status"))) {
            biz.set("status", "1");
        }
        ActionHandler.currentSession().addOrUpdate(biz);

        //重新加载视图
        SystemKit.removeCache("tpm");
        SystemKit.removeCache("tpm" + biz.getString("ter_com_id"));

        return biz;
    }

    @Override
    public BizObject delById(String id) throws SQLException {
        return null;
    }
}
