package pax.service.appversion.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pax.service.ReturnCode;
import pax.service.app.AppService;
import pax.service.appversion.AppVersionService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.sn;
import static sun.rmi.transport.TransportConstants.Return;

/**
 * Created by want on 2017/6/12.
 */
public class AppVersionServiceImpl implements AppVersionService {
    static Logger logger = Logger.getLogger(AppVersionServiceImpl.class);

    @Override
    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from ")
                .append(TABLE_FULL_NAME)
                .append(" where status='1' ");

        List<Object> paramList = new ArrayList<>();
        if (param == null) {
            param = new BizObject();
        }
        if (StringUtils.isNotBlank(param.getString("startdate"))) {
            sb.append(" and createdate > ? ");
            paramList.add(param.getString("startdate"));
        }
        if (StringUtils.isNotBlank(param.getString("enddate"))) {
            sb.append(" and createdate < ? ");
            paramList.add(param.getString("enddate"));
        }
        if (StringUtils.isNotBlank(param.getString("app_id"))) {
            sb.append(" and app_id = ? ");
            paramList.add(param.getString("app_id"));
        }
        if (StringUtils.isNotBlank(param.getString("app_type"))) {
            sb.append(" and app_type = ? ");
            paramList.add(param.getString("app_type"));
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
    public BizObject addOrUpdate(BizObject biz, List<BizObject> ter_types) {
        try {
            //验证所选的厂商和终端型号是否匹配
            String ter_com_id = biz.getString("ter_com_id");
            if (ter_types != null && ter_types.size() > 0) {
                for (BizObject ter_type : ter_types) {
                    String ter_type_name = SystemKit.getCacheParamById("ter_type" + ter_com_id, ter_type.getString("id"));
                    if (StringUtils.isBlank(ter_type_name)) return ReturnCode.errorData("7777", "终端型号与所选厂商不匹配");
                }
            }

            if (StringUtils.isBlank(biz.getString("status"))) {
                biz.set("status", "1");
            }
            ActionHandler.currentSession().beginTrans();
            ActionHandler.currentSession().addOrUpdate(biz);

            if (ter_types != null && ter_types.size() > 0) {
                //先删除存在的
                String sql = "update " + RE_TABLE_FULL_NAME + " set status='0' where app_ver_id='" + biz.getString("id") + "'";
                int result = ActionHandler.currentSession().executeUpdateSQL(sql);
                logger.info("[AppVersionServiceImpl addOrUpdate]update ter_type , sql = " + sql + ",result = " + result);

                //将终端型号保存关联表
                for (BizObject ter_type : ter_types) {
                    BizObject re_app_tertype = new BizObject(RE_TABLE_NAME);
                    re_app_tertype.set("ter_type_id", ter_type.getString("id"));
                    re_app_tertype.set("app_ver_id", biz.getString("id"));
                    re_app_tertype.set("status", "1");

                    ActionHandler.currentSession().add(re_app_tertype);
                    logger.info("[AppVersionServiceImpl addOrUpdate]add ter_type,re_app_tertype = " + re_app_tertype);
                }
            }
            ActionHandler.currentSession().commit();
            return ReturnCode.successData(biz);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("[AppVersionServiceImpl addOrUpdate]", e);
            ActionHandler.currentSession().rollback();
        }
        return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM);
    }

    @Override
    public BizObject delById(String id) throws SQLException {
        return null;
    }

    /**
     * 获取终端型号对应的可用APP  (appName appVer)
     *
     * @param ter_type_id
     * @return
     * @throws SQLException
     */
    @Override
    public List<BizObject> getAppList(String ter_type_id) throws SQLException {
        StringBuffer sb = new StringBuffer("select app.app_name as appName ,app_ver.app_ver_code as appVer from ")
                .append(AppService.TABLE_FULL_NAME).append(" app ")
                .append(", ")
                .append(AppVersionService.TABLE_FULL_NAME).append(" app_ver ")
                .append(" where app.id=app_ver.app_id and app.status='1' and app_ver.status='1' and app_ver.ter_type_id=? ");
        List<Object> param_list = new ArrayList<>();
        param_list.add(ter_type_id);
        List list = QueryFactory.executeQuerySQL(sb.toString(), param_list);
        return list;
    }

    @Override
    public List<BizObject> getTerType(String app_ver_id) throws SQLException {
        if (StringUtils.isBlank(app_ver_id)) {
            return null;
        }

        StringBuffer sql = new StringBuffer("select ap.app_ver_id,ap.ter_type_id,tp.ter_co,tp.ter_name,tp.ter_com_id,tp.program_path,tp.dll_path_id,tp.net_license from ")
                .append(RE_TABLE_FULL_NAME).append(" ap ,")
                .append(RE_TABLE_FULL_NAME2).append(" tp ")
                .append(" where ap.status='1' and tp.status='1' and ap.ter_type_id=tp.id ")
                .append(" and ap.app_ver_id=? ");

        List<Object> param_list = new ArrayList<>();
        param_list.add(app_ver_id);
        logger.info("[getTerType]sql=" + sql + ",param=" + param_list);
        List list = QueryFactory.executeQuerySQL(sql.toString(), param_list);
        return list;
    }
}
