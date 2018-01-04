package pax.service.tertype;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/8.
 */
public interface TerTypeService {
    public static final String TABLE_NAME = "ter_type";
    public static final String TABLE_FULL_NAME = "tms.ter_type";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;

    public BizObject delById(String id) throws SQLException;

    public BizObject getBySN(String sn) throws SQLException;

    public List<BizObject> getTerTypeByTerComId(String ter_com_id) throws SQLException;
}
