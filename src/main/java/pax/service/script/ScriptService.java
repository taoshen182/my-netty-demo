package pax.service.script;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/7/18.
 */
public interface ScriptService {
    public static final String TABLE_NAME = "script";
    public static final String TABLE_FULL_NAME = "tms.script";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;
}
