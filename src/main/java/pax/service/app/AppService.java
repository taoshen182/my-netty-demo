package pax.service.app;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/12.
 */
public interface AppService {
    public static final String TABLE_NAME = "app";
    public static final String TABLE_FULL_NAME = "tms.app";

    public static final String STATUS_NORMAL = "1";
    public static final String STATUS_DEL = "0";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;

    public BizObject delById(String id)  ;
}
