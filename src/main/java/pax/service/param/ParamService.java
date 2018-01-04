package pax.service.param;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;

/**
 * 参数是到根机构,不到子机构
 */
public interface ParamService {
	
	public static final String TABLE_NAME = "param";
	public static final String TABLE_FULL_NAME = "tms.param";
	
	/**
	 * 通过参数ID查找参数信息
	 * @param id 参数ID
	 * @return
	 * @throws SQLException
	 */
	public BizObject getById(String id)  throws SQLException;
	
	/**
	 * 通过参数名称查找参数信息
	 * @param name 参数名称
	 * @return
	 * @throws SQLException
	 */
	public BizObject getByName(String name) throws SQLException;
	
	public String addOrUpdate(BizObject param);

	public String addCustomParam(String name) throws SQLException;
	
	public String show(String id);

	public BizObject showToBiz(String id);
	
	public String del(String id);
	
	public String getList(BizObject param, PageVariable page);
	
	public String getSimpleList(BizObject param, PageVariable page) ;
	
	public String getList(String code, String name, String type, String inst_id, PageVariable page);
	
}
