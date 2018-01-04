package pax.service.appversion;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/12.
 */
public interface AppVersionService {
    public static final String TABLE_NAME = "app_version";
    public static final String TABLE_FULL_NAME = "tms.app_version";


    public static final String RE_TABLE_NAME = "re_app_tertype";
    public static final String RE_TABLE_FULL_NAME = "tms.re_app_tertype";


    public static final String RE_TABLE_NAME2 = "ter_type";
    public static final String RE_TABLE_FULL_NAME2 = "tms.ter_type";

    public static final String STATUS_NORMAL = "1";
    public static final String STATUS_DEL = "0";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz, List<BizObject> ter_types);

    public BizObject delById(String id) throws SQLException;


    public List<BizObject> getAppList(String ter_type_id) throws SQLException;

    List<BizObject> getTerType(String app_ver_id) throws SQLException;
}
