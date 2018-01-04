package pax.service.param;

import tool.dao.BizObject;
import tool.dao.PageVariable;

import java.sql.SQLException;
import java.util.List;

/**
 * 参数模板是到根机构,不到子机构
 */
public interface PTemplateService {
    public static final String TABLE_NAME = "template";
    public static final String TABLE_FULL_NAME = "tms.template";
    public static final String TABLE_RELATION = "re_param_template";
    public static final String TABLE_FULL_RELATION = "tms.re_param_template";
    public static final String TABLE_FULL_DATA_RANGE = "tms.data_range";
    public static final String TABLE_DATA_RANGE = "data_range";

    public static final String DATA_TYPE_PTEMPLATE = "re_param_template";

    public String addOrUpdate(BizObject param);

//	/**
//	 * 机构门户调用 copy保存,应用场景如:原先是平台的模板,现在机构想要在上面修改,则copy一份修改,不能在原有的基础上修改
//	 *
//	 * @param template					要保存的模板对象
//	 * @param old_template_version_id	原模板的id
//     * @return
//     */
//	public String copyAddOrUpdate(BizObject template, String old_template_version_id);

    public String show(String id);

//	public String del(String id);

    public String delRelation(String id);

    public String getList(BizObject param, PageVariable page);

    public List<BizObject> list(BizObject param, PageVariable page) throws SQLException;

//	public String addOrUpdateRelation(String template_version_id,List<BizObject> list);

    public String addOrUpdateRelation(String template_version_id, List<BizObject> list, String[] param_ids);

    public String listParams(String template_version_id);

    public BizObject getById(String id) throws SQLException;

    /**
     * 判断某个默认值是否已存在（如，是否默认值引用过附加字段）
     *
     * @param def 默认值（如，附加字段id）
     * @throws SQLException
     * @return 存在 return true;否则 return false;
     */
    public boolean isExistByDef(String def) throws SQLException;

    /**
     * 添加自定义范围
     *
     * @param id         单据id
     * @param range_name 自定义范围名称
     * @return
     */
    public String addCustom(String id, String range_name);

    /**
     * 添加自定义参数
     *
     * @param param_name
     * @param termplate_version_id
     * @return
     */
    public String addCustomParam(String param_name, String termplate_version_id);

    /**
     * 得到模板参数
     *
     * @param template_version_id
     * @return 参数名，参数值 的list
     */
    public List<BizObject> queryParams(String template_version_id) throws SQLException;

    /**
     * 根据标准的模版生成一个指定APP的模版（生成的是非标准的）
     *
     * @param name
     * @param template_version_id
     * @return
     * @throws SQLException
     */
    public String addCustomTemplateVersion(String name, String template_version_id) throws SQLException;

    /**
     * 删除自定义模版，即非标准模版
     *
     * @param template_version_id 模板id
     * @throws SQLException
     */
    public void delCustomTemplate(String template_version_id) throws SQLException;

    /**
     * 应用可以下拉选择的模版
     *
     * @param template_version_id
     * @return
     * @throws SQLException
     */
    public List<BizObject> getSelectTemplateVersion(String template_version_id, String inst_id) throws SQLException;

}
