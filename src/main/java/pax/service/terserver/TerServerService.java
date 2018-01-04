package pax.service.terserver;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/2.
 */
public interface TerServerService {
    public static final String TABLE_NAME = "ter_server";
    public static final String TABLE_FULL_NAME = "tms.ter_server";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;

    public BizObject delById(String id) throws SQLException;


}
