package pax.service.app.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pax.service.ReturnCode;
import pax.service.app.AppService;
import pax.service.appversion.AppVersionService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.BillNoGenerator;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.c;

/**
 * Created by want on 2017/6/12.
 */
public class AppServiceImpl implements AppService {
    private Logger logger = Logger.getLogger(AppServiceImpl.class);

    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status=? ");

        List<Object> paramList = new ArrayList<>();
        if (param == null) {
            param = new BizObject();
        }
        paramList.add(STATUS_NORMAL);

        if (StringUtils.isNotBlank(param.getString("startdate"))) {
            sb.append(" and createdate > ? ");
            paramList.add(param.getString("startdate"));
        }
        if (StringUtils.isNotBlank(param.getString("enddate"))) {
            sb.append(" and createdate < ? ");
            paramList.add(param.getString("enddate"));
        }
        if (StringUtils.isNotBlank(param.getString("app_name"))) {
            sb.append(" and app_name like ? ");
            paramList.add("%" + param.getString("app_name") + "%");
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
        if (StringUtils.isBlank(biz.getString("app_no"))) {
            biz.set("app_no", BillNoGenerator.getFlowNo2("ap", 10).substring(2));
        }
        if (StringUtils.isBlank(biz.getString("status"))) {
            biz.set("status", STATUS_NORMAL);
        }


        //重新加载视图
        SystemKit.removeCache("app");

        ActionHandler.currentSession().addOrUpdate(biz);
        return biz;
    }

    @Override
    public BizObject delById(String id) {
        try {
            BizObject byId = getById(id);
            if (null == byId) {
                return ReturnCode.errorData(ReturnCode.ERROR_NOTEXSIT);
            }
            if (STATUS_DEL.equals(byId.getString("status"))) {
                return ReturnCode.errorData(ReturnCode.ERROR_STATUS);
            }
            //检查有没有被关联
            String sql = String.format("select * from %s where app_id='%s' and status='%s' ", AppVersionService.TABLE_FULL_NAME, byId.getString("id"), AppVersionService.STATUS_NORMAL);
            System.out.println("sql = " + sql);
            List<BizObject> bizObjects = QueryFactory.executeQuerySQL(sql);

            if (null != bizObjects && bizObjects.size() > 0) {
                return ReturnCode.errorData(ReturnCode.ERROR_HAVE_SON);
            }

            byId.set("status", AppService.STATUS_DEL);

            ActionHandler.currentSession().update(byId);
            return ReturnCode.successData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM);
    }

}
