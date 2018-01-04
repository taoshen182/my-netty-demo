package pax.service.inst;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/16.
 */
public interface InstService {
    public static final String TABLE_NAME = "institution";
    public static final String TABLE_FULL_NAME = "tms.institution";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;

    public BizObject delById(String id) throws SQLException;
}
