package pax.actionhandler.param;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.param.ParamService;
import pax.service.param.impl.ParamServiceImpl;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.CandoCheck;
import sand.depot.tool.system.ErrorException;
import sand.utils.JsonTool;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParamAH extends ActionHandler {
	Logger logger = Logger.getLogger(ParamServiceImpl.class);
	
	private ParamService paramService;

	public ParamAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "param";
		this._moduleId ="param";
	}
	
	/**
	 * 参数列表
	 * 
	 * @throws SQLException
	 * @throws JSONException
	 */
	@CandoCheck("listParam")
	public void list() throws SQLException, JSONException {
		this.pushLocation("参数列表", "param.ParamAH.list");
		BizObject param = this.getBizObjectFromMap("param");
//		if(param==null) param = new BizObject(ParamService.TABLE_NAME);
//			if(StringUtils.isBlank(param.getString("inst_id")) && StringUtils.isNotBlank(this._curuser.getString("inst_root_id"))) {
//				param.set("inst_id", this._curuser.getString("inst_root_id"));
//			}
		String json = this.getParamService().getList(param, this.preparePageVar());
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		List<BizObject> list = (ArrayList<BizObject>)biz.get("datas");
		logger.info("ParamAH.list[List]" + list.toString());
		this._request.setAttribute("objList", list);
		this._nextUrl = "template/param/list.jsp";
	}
	
	/**
	 * 模板选择参数列表
	 * @throws SQLException
	 * @throws JSONException
	 */
	public void chooseParams() throws SQLException, JSONException {
		BizObject param = this.getBizObjectFromMap("param");
		if(param==null) param = new BizObject(ParamService.TABLE_NAME);
		param.set("inst_id", this._curuser.getString("inst_id"));
		String json = this.getParamService().getList(param, this.preparePageVar());
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		List<BizObject> list = (ArrayList<BizObject>)biz.get("datas");
		this._request.setAttribute("objList", list);
		this._request.setAttribute("type", "dialog");
		this._nextUrl = "template/template/chooseList.jsp";
	}
	
	/**
	 * 显示编辑页
	 * @throws SQLException
	 * @throws JSONException
	 */
	@CandoCheck("editParam")
	public void show() throws SQLException, JSONException {
		this.pushLocation(Bundle.getString("param.list"), "param.ParamAH.list");
		this.pushLocation(Bundle.getString("param.edit"), "param.ParamAH.show?objId="+this._objId);
		String json = this.getParamService().show(this._objId);
		if(json!=null){
			BizObject biz = JsonTool.toBiz(json);
			this._request.setAttribute("obj", biz);
		}
		this._nextUrl = "template/param/edit.jsp";
	}
	
	@CandoCheck("editParam")
	public void save() throws SQLException ,JSONException {
		BizObject param = this.getBizObjectFromMap("param");

		String json = this.getParamService().addOrUpdate(param);
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		this.clearQueryParam();
		this._tipInfo = Bundle.getString("save.success");
		this._request.setAttribute("nextUrl", "/param.ParamAH.list");
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}
	
	@CandoCheck("editParam")
	public void del() throws SQLException ,JSONException {
		String json = this.getParamService().del(this._objId);
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		
		this._tipInfo = Bundle.getString("delete.success");
		this._request.setAttribute("nextUrl", "param.ParamAH.list");
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}
	
	public ParamService getParamService() {
		if (paramService == null)
			paramService = (ParamService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("paramService");
		return paramService;
	}

}
