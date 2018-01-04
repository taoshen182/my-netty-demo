package pax.actionhandler.param;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.param.PTemplateService;
import pax.service.param.ParamService;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.SystemKit;
import sand.utils.JsonTool;
import tool.dao.BizObject;
import tool.dao.PageVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PTemplateAH extends ActionHandler {
	
	private PTemplateService pTemplateService;
	private ParamService paramService;

	public PTemplateAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "re_param_template";
		this._moduleId = "param";
	}
	
	protected void initial(){
		super.initial();
		pTemplateService = (PTemplateService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("pTemplateService");
		paramService = (ParamService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("paramService");
	}

	@CandoCheck("listPtemplate")
	public void list() throws SQLException,JSONException {
		this.pushLocation(Bundle.getString("param.template_list"), "param.PTemplateAH.list");
		BizObject pTemplate = this.getBizObjectFromMap("template");
		if(pTemplate==null) pTemplate = new BizObject(PTemplateService.TABLE_NAME);
			if(StringUtils.isBlank(pTemplate.getString("inst_id")) && StringUtils.isNotBlank(this._curuser.getString("inst_root_id"))) {
				pTemplate.set("inst_id", this._curuser.getString("inst_root_id"));
			}
		String json = this.pTemplateService.getList(pTemplate, this.preparePageVar());
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		List<BizObject> list = (ArrayList<BizObject>)biz.get("datas");
		this._request.setAttribute("objList", list);
		this._nextUrl = "template/param/pt_list.jsp";
	}
	
	@CandoCheck("editPtemplate")
	public void show() throws JSONException {
		this.pushLocation(Bundle.getString("param.template_list"), "param.PTemplateAH.list");
		this.pushLocation(Bundle.getString("param.template_edit"), "param.PTemplateAH.show?objId="+this._objId);
		String json = this.pTemplateService.show(this._objId);
		if(json!=null){
			BizObject biz = JsonTool.toBiz(json);
			this._request.setAttribute("obj", biz);
		}
		this._nextUrl = "template/param/pt_edit.jsp";
	}
	
	@CandoCheck("editPtemplate")
	public void save() throws SQLException ,JSONException {
		BizObject temp = this.getBizObjectFromMap("template");
		if(StringUtils.isNotBlank(this._curuser.getString("inst_root_id"))) {
			temp.set("inst_id",this._curuser.getString("inst_root_id"));
		}
		String json = this.pTemplateService.addOrUpdate(temp);
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		SystemKit.removeCache("template");
		this._tipInfo = Bundle.getString("save.success");
		this._request.setAttribute("nextUrl", "param.PTemplateAH.list");
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}
	

	
	@CandoCheck("editPtemplate")
	public void listParams() throws JSONException, SQLException {
		this.pushLocation(Bundle.getString("param.template_list"), "param.PTemplateAH.list");
		this.pushLocation(Bundle.getString("param.template_edit"), "param.PTemplateAH.show?objId="+this.getParameter("template_id"));
		BizObject param2 = this.getBizObjectFromMap("param");

		this.getParams();
		this._request.setAttribute("dialog_type", this.getParameter("dialog_type"));//dialog   form
	}
	
	@CandoCheck("editPtemplate")
	public void saveRelation() throws SQLException, JSONException {
		List<BizObject> list = this.getBizObjectWithType("re_param_template");
		String template_id = this.getParameter("template_id");
		BizObject param = this.getBizObjectFromMap("param");
		String param_ids = this.getParameter("param_ids");
		String json = this.pTemplateService.addOrUpdateRelation(template_id, list,param_ids.split(","));
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		
		this._tipInfo = Bundle.getString("save.success");
		this._request.setAttribute("nextUrl", "param.PTemplateAH.listParams?template_id="+this.getParameter("template_id")+"&param$inst_id="+param.getString("inst_id"));
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}

	@Ajax
	@CandoCheck("editPtemplate")
	public String saveRelationForAjax() throws SQLException, JSONException {
		List<BizObject> list = this.getBizObjectWithType("re_param_template");
		String template_id = this.getParameter("template_id");
		BizObject param = this.getBizObjectFromMap("param");
		String param_ids = this.getParameter("param_ids");
		String json = this.pTemplateService.addOrUpdateRelation(template_id, list,param_ids.split(","));
		return json;
	}

	@Override
	protected void listObj() throws SQLException {
		super.listObj();
	}

	@CandoCheck("editPtemplate")
	public void delRelation() throws SQLException ,JSONException {
		String json = this.pTemplateService.delRelation(this._objId);
		BizObject param = this.getBizObjectFromMap("param");
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		
		this._tipInfo = Bundle.getString("delete.success");
		this._request.setAttribute("nextUrl", "param.PTemplateAH.listParams?template_id="+this.getParameter("template_id")+"&param$inst_id="+param.getString("inst_id"));
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}
	
	public void chooseParams() throws SQLException, JSONException {
		BizObject param2 = this.getBizObjectFromMap("param");
		this._request.setAttribute("inst_id", param2.getString("inst_id"));
		this.getParams();
		this._request.setAttribute("dialog_type", "dialog");//dialog   form
	}
	
	@Ajax
	public String addCustom() {
		String id = this.getParameter("id");
		String range_name = this.getParameter("range_name");
		return this.pTemplateService.addCustom(id, range_name);
	}

	public void addCustomParam() throws JSONException {
		String json = this.pTemplateService.addCustomParam(this.getParameter("custom_param_name"),this.getParameter("template_id"));
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		this._tipInfo = Bundle.getString("save.success");
		this._request.setAttribute("nextUrl", "param.PTemplateAH.listParams?template_id="+this.getParameter("template_id"));
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}
	
	private void getParams() throws SQLException, JSONException {
		String retjson = this.pTemplateService.show(this.getParameter("template_id"));
		if(retjson!=null){
			BizObject biz = JsonTool.toBiz(retjson);
			if(biz!=null) this._request.setAttribute("inst_id", biz.getString("inst_id"));
		}

		//查询模板下的参数列表
		String json = this.pTemplateService.listParams(this.getParameter("template_id"));
		BizObject biz = JsonTool.toBiz(json);
		if(!biz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(biz.getString("respmsg"));
		List<BizObject> list = (ArrayList<BizObject>)biz.get("datas");
		this._request.setAttribute("objList", list);
		if(list.size()>0)
			this._request.setAttribute("data_range", (List<BizObject>)list.get(0).get("data_range_list"));
		//模板可以添加的参数
		BizObject param = this.getBizObjectFromMap("param");
		if(param==null) param = new BizObject(ParamService.TABLE_NAME);
		param.set("inst_id", this.getParameter("param$inst_id"));
		PageVariable page = this.preparePageVar();
		String pjson = this.paramService.getList(param, page);
		BizObject pbiz = JsonTool.toBiz(pjson);
		if(!pbiz.getString("respcode").equals(ReturnCode.SUCCESS)) throw new ErrorException(pbiz.getString("respmsg"));
		List<BizObject> plist = (ArrayList<BizObject>)pbiz.get("datas");
		this._request.setAttribute("pObjList", plist);
		this._request.setAttribute("template_id", this.getParameter("template_id"));
		this._nextUrl = "template/param/tp_param_list.jsp";
	}
	
}
