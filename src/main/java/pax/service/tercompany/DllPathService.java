package pax.service.tercompany;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/7/20.
 */
public interface DllPathService {
    public static final String TABLE_NAME = "dll_path";
    public static final String TABLE_FULL_NAME = "tms.dll_path";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;
    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;

    public BizObject delById(String id) throws SQLException;

    public List<BizObject> getDllPathByTerComId(String ter_com_id) throws SQLException;
}

