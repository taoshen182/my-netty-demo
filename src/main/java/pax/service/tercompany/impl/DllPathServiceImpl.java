package pax.service.tercompany.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import pax.service.tercompany.DllPathService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/7/20.
 */
public class DllPathServiceImpl implements DllPathService {
    static Logger logger = Logger.getLogger(DllPathServiceImpl.class);

    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1'");

        List<Object> param_list = new ArrayList<>();

        if (param == null) {
            param = new BizObject();
        }
        if (StringUtils.isNotBlank(param.getString("ter_com_id"))) {
            sb.append(" and ter_com_id = ?");
            param_list.add(param.getString("ter_com_id"));
        }
        if (StringUtils.isNotBlank(param.getString("dll_path"))) {
            sb.append(" and dll_path like ?");
            param_list.add("%" + param.getString("dll_path") + "%");
        }
        if (StringUtils.isNotBlank(param.getString("startdate"))) {
            sb.append(" and createdate > ? ");
            param_list.add(param.getString("startdate"));
        }
        if (StringUtils.isNotBlank(param.getString("enddate"))) {
            sb.append(" and createdate < ? ");
            param_list.add(param.getString("enddate"));
        }
//        sb.append(" order by modifydate desc");
        logger.info("[DllPathServiceImpl list]sql = " + sb + ", paramList = " + param_list + ", page = " + page);
        List list = QueryFactory.executeQuerySQL(sb.toString(), param_list, page);
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
        SystemKit.removeCache("dll_path");
        return biz;
    }

    @Override
    public BizObject delById(String id) throws SQLException {
        return null;
    }

    @Override
    public List<BizObject> getDllPathByTerComId(String ter_com_id) throws SQLException {
        BizObject param = new BizObject();
        param.set("ter_com_id", ter_com_id);
        List<BizObject> list = list(param, null);
        return list;

    }
}
