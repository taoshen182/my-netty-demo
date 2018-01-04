package pax.actionhandler.plantask;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.appversion.AppVersionService;
import pax.service.param.PTemplateService;
import pax.service.plantask.PlanTaskService;
import pax.service.terminals.TerminalsService;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.tool.system.ControllableException;
import sand.utils.JsonTool;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by want on 2017/7/26.
 */
public class PlanTaskAH extends ActionHandler {
    private PlanTaskService planTaskService;
    private AppVersionService appVersionService;
    private PTemplateService pTemplateService;
    private TerminalsService terminalsService;

    public PlanTaskAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    protected void initial() {
        super.initial();
        pTemplateService = (PTemplateService) WebApplicationContextUtils.getRequiredWebApplicationContext(ActionHandler._context).getBean("pTemplateService");
        appVersionService = (AppVersionService) WebApplicationContextUtils.getRequiredWebApplicationContext(ActionHandler._context).getBean("appVersionService");
        planTaskService = (PlanTaskService) WebApplicationContextUtils.getRequiredWebApplicationContext(ActionHandler._context).getBean("planTaskService");
        terminalsService = (TerminalsService) WebApplicationContextUtils.getRequiredWebApplicationContext(ActionHandler._context).getBean("terminalsService");
    }

    public void list() throws SQLException {
        this.pushLocation("更新任务列表", "plantask.PlanTaskAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        List<BizObject> list = planTaskService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/plantask/list.jsp";

    }

    public void del() throws SQLException {

        BizObject ret = planTaskService.del(this._objId);
        if (!ReturnCode.SUCCESS.equals(ret.getString("respcode"))) {
            throw new ControllableException(ret.getString("respmsg"));
        }
        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "plantask.PlanTaskAH.list");
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void show() throws SQLException {
        this.pushLocation("更新任务编辑", "plantask.PlanTaskAH.show");

        BizObject obj = planTaskService.getById(this._objId);
        BizObject app_ver = null;
        BizObject template = null;
        List<BizObject> terTypeList = null;
        List<BizObject> terList = null;
        if (obj != null) {
            app_ver = appVersionService.getById(obj.getString("app_ver_id"));
            template = pTemplateService.getById(obj.getString("template_id"));
            getTerTypeByAppVer(app_ver);//适用的机型
            terList = planTaskService.listTerminalsByTask(obj.getString("id"), null); //终端
        }
        this._request.setAttribute("terList", terList);
        this._request.setAttribute("app_ver", app_ver);
        this._request.setAttribute("template", template);
        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/plantask/edit.jsp";
    }

    private void getTerTypeByAppVer(BizObject app_ver) throws SQLException {
        if (app_ver != null) {
            List<BizObject> terTypeList = appVersionService.getTerType(app_ver.getString("id"));
            if (null != terTypeList && terTypeList.size() > 0) {
                app_ver.set("terTypeList", terTypeList);
                String terTypeIds = "";
                for (BizObject terType : terTypeList) {
                    terTypeIds += terType.getString("ter_type_id") + ",";
                }
                terTypeIds = terTypeIds.substring(0, terTypeIds.length() - 1);
                app_ver.set("terTypeIds", terTypeIds);
            }
        }
    }

    public void chooseAppVer() throws SQLException {
        BizObject param = this.getBizObjectFromMap(AppVersionService.TABLE_NAME);
        List<BizObject> list = appVersionService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/plantask/chooseAppVer.jsp";
    }

    public void chooseTemplate() throws SQLException, JSONException {
        BizObject param = this.getBizObjectFromMap(AppVersionService.TABLE_NAME);
        List<BizObject> tempList = pTemplateService.list(param, this.preparePageVar());

        this._request.setAttribute("objList", tempList);
        this._nextUrl = "template/plantask/chooseTemp.jsp";
    }

    public void chooseTerminal() throws SQLException {
        BizObject param = this.getBizObjectFromMap(TerminalsService.TABLE_NAME);
        List<BizObject> terList = terminalsService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", terList);
        this._nextUrl = "template/plantask/chooseTerminal.jsp";
    }

    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(PlanTaskService.TABLE_NAME);
        BizObject obj = planTaskService.addOrUpdate(biz, null);
        if (!"0000".equals(obj.getString("respcode"))) {
            throw new ControllableException(obj.getString("respmsg"));
        }

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "plantask.PlanTaskAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    @Ajax
    public String saveTerminal() throws SQLException {
        String plantask_id = this.getParameter("plantask_id");
        String terminal_ids = this.getParameter("terminal_ids");

        BizObject planTask = planTaskService.getById(plantask_id);
        if (planTask == null) {
            return ReturnCode.ERROR_PARAM_NULL.toString();
        }
        List<BizObject> terminalList = new ArrayList<>();
        String[] terminal_ids_array = terminal_ids.split(",");
        for (String terminal_id : terminal_ids_array) {
            BizObject terminal = terminalsService.getById(terminal_id);
            terminalList.add(terminal);
        }
        BizObject obj = planTaskService.addOrUpdate(planTask, terminalList);
        return obj.toString();
    }

    @Ajax
    public String saveApp() throws SQLException {
        String plantask_id = this.getParameter("plantask_id");
        String app_ver_id = this.getParameter("app_ver_id");
        String template_id = this.getParameter("template_id");

        BizObject planTask = planTaskService.getById(plantask_id);

        if (planTask == null) {
            return ReturnCode.ERROR_PARAM_NULL.toString();
        }
        if (StringUtils.isNotBlank(app_ver_id)) {
            BizObject appVer = appVersionService.getById(app_ver_id);
            if (appVer == null) {
                return ReturnCode.NoExist.APP_VERSION.toString();
            }
            planTask.set("app_ver_id", app_ver_id);
        }
        if (StringUtils.isNotBlank(template_id)) {
            BizObject temp = pTemplateService.getById(template_id);
            if (temp == null) {
                return ReturnCode.ERROR_PARAM_NULL.toString();
            }
            planTask.set("template_id", template_id);
        }
        BizObject ret = planTaskService.addOrUpdate(planTask, null);
        return ReturnCode.successData(ret).toString();
    }
}



