package pax.service.tertype.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pax.service.ReturnCode;
import pax.service.tertype.TerTypeService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/6/8.
 */
public class TerTypeServiceImpl implements TerTypeService {
    static Logger logger = Logger.getLogger(TerTypeServiceImpl.class);

    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1' ");

        List<Object> paramList = new ArrayList<>();
        if (param == null) {
            param = new BizObject(TABLE_NAME);
        }
        if (StringUtils.isNotBlank(param.getString("startdate"))) {
            sb.append(" and createdate > ? ");
            paramList.add(param.getString("startdate"));
        }
        if (StringUtils.isNotBlank(param.getString("enddate"))) {
            sb.append(" and createdate < ? ");
            paramList.add(param.getString("enddate"));
        }
        if (StringUtils.isNotBlank(param.getString("ter_com_id"))) {
            sb.append(" and ter_com_id=?");
            paramList.add(param.getString("ter_com_id"));
        }
        if (StringUtils.isNotBlank(param.getString("ter_name"))) {
            sb.append(" and ter_name like ?");
            paramList.add("%" + param.getString("ter_name") + "%");
        }


//        sb.append(" order by modifydate desc ");
        logger.info("[TerTypeServiceImpl list]sql = " + sb + ", paramList = " + paramList + ", page = " + page);
        List list = QueryFactory.executeQuerySQL(sb.toString(), paramList, page);
        return list;
    }

    @Override
    public List<BizObject> getTerTypeByTerComId(String ter_com_id) throws SQLException {
        BizObject param = new BizObject(TABLE_NAME);
        param.set("ter_com_id", ter_com_id);
        return list(param, null);
    }

    @Override
    public BizObject getById(String id) throws SQLException {
        QueryFactory qf = new QueryFactory(TABLE_NAME);
        return qf.getByID(id);

    }

    @Override
    public BizObject addOrUpdate(BizObject biz) throws SQLException {
        //TODO 验证厂商 和 dll路径的对应关系是否正确
        String ter_com_id = biz.getString("ter_com_id");//厂商
        String dll_path_id = biz.getString("dll_path_id");//dll路径
        String dll_path_name = SystemKit.getCacheParamById("com_dll_path" + ter_com_id, dll_path_id);
        System.out.println("dll_path_name = " + dll_path_name);
        if (StringUtils.isBlank(dll_path_name)) {
            return ReturnCode.errorData("7784", "所选厂商与dll路径不相符");
        }

        if (StringUtils.isBlank(biz.getString("status"))) {
            biz.set("status", "1");
        }

        //重新加载视图
        SystemKit.removeCache("ter_type");

        ActionHandler.currentSession().addOrUpdate(biz);
        return biz;
    }

    @Override
    public BizObject delById(String id) throws SQLException {
        return null;
    }

    @Override
    public BizObject getBySN(String sn) throws SQLException {
        // TODO: 暂时不用这个，没写完
        if (StringUtils.isBlank(sn)) return null;

        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1' and ");

        List<Object> param_list = new ArrayList<>();


        List<BizObject> list = QueryFactory.executeQuerySQL(sb.toString(), param_list);

        return list.get(0);
    }
}
