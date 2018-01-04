package pax.service.tercompany;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/5.
 */
public interface TerCompanyService {
    public static final String TABLE_NAME = "ter_company";
    public static final String TABLE_FULL_NAME = "tms.ter_company";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;
    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;

    public BizObject delById(String id) throws SQLException;
}
