package pax.service.plantask;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/7/26.
 */
public interface PlanTaskService {
    public static final String TABLE_NAME = "plan_task";
    public static final String TABLE_FULL_NAME = "tms.plan_task";
    public static final String RE_TABLE_NAME = "re_plantask_terminal";
    public static final String RE_TABLE_FULL_NAME = "tms.re_plantask_terminal";

    //任务状态
    public static final String STATUS_0 = "0"; //删除
    public static final String STATUS_1 = "1"; //正常
    public static final String STATUS_2 = "2"; //下载完成
    public static final String STATUS_3 = "3";

    //任务类型
    public static final String TYPE_0 = "0"; //远程下载  7001
    public static final String TYPE_1 = "1"; //初始化    7004


    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

    public BizObject getById(String id) throws SQLException;

    public BizObject addOrUpdate(BizObject biz, List<BizObject> terminals);

    public List<BizObject> listTerminalsByTask(String plantask_id,String status) throws SQLException;

    public List<BizObject> listTaskByTerId(String terminal_id, String type,String status) throws SQLException;

    public BizObject del(String plantask_id) throws SQLException;
}
