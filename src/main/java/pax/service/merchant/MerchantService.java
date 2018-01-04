package pax.service.merchant;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/16.
 */
public interface MerchantService {
    public static final String TABLE_NAME = "merchant_info";
    public static final String TABLE_FULL_NAME = "tms.merchant_info";
    //未启用
    public static final String STATUS_0 = "0";
    //冻结
    public static final String STATUS_1 = "1";
    //启用
    public static final String STATUS_2 = "2";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;

    public BizObject delById(String id) throws SQLException;
}
