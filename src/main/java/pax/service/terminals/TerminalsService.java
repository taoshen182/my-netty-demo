package pax.service.terminals;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/13.
 */
public interface TerminalsService {
    public static final String TABLE_NAME = "terminals";
    public static final String TABLE_FULL_NAME = "tms.terminals";

//    union select DISTINCT '0','未初始化','ter_status','0'
//    union select DISTINCT '1','待初始化','ter_status','1'
//    union select DISTINCT '2','已初始化','ter_status','2'
//    union select DISTINCT '3','已注册','ter_status','3'
//    union select DISTINCT '4','注销','ter_status','4'
//    union select DISTINCT '5','待确认','ter_status','5'

    public static final String TER_STATUS_0 = "0";
    public static final String TER_STATUS_1 = "1";
    public static final String TER_STATUS_2 = "2";
    public static final String TER_STATUS_3 = "3";
    public static final String TER_STATUS_4 = "4";
    public static final String TER_STATUS_5 = "5";

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz) throws SQLException;

    public BizObject delById(String id) throws SQLException;

    public BizObject getTerminal(String mid, String sn, String fid) throws SQLException;

    public BizObject getTerminal(String sn) throws SQLException;
}
