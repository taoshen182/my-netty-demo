package pax.actionhandler.terminals;

import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.plantask.PlanTaskService;
import pax.service.tercompany.TerCompanyService;
import pax.service.terminals.TerminalsService;
import pax.service.terserver.TerServerService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/13.
 */
public class TerminalsAH extends ActionHandler {
    public TerminalsAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    private TerminalsService terminalsService;
    private PlanTaskService planTaskService;

    @Override
    protected void initial() {
        super.initial();
        terminalsService = (TerminalsService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("terminalsService");
        planTaskService = (PlanTaskService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("planTaskService");
    }

    public void list() throws SQLException {
        this.pushLocation("终端信息列表", "terminals.TerminalsAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        List<BizObject> list = terminalsService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/terminals/list.jsp";
    }

    public void show() throws SQLException {
        this.pushLocation("终端信息编辑", "terminals.TerminalsAH.show");

        BizObject obj = terminalsService.getById(this._objId);
        List<BizObject> taskList = null;
        if (obj != null) {
            taskList = planTaskService.listTaskByTerId(obj.getString("id"), null,null);
        }

        this._request.setAttribute("taskList", taskList);
        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/terminals/edit.jsp";

    }

    public void resetInit() throws SQLException {

        BizObject obj = terminalsService.getById(this._objId);
        //将状态改为待初始化
        obj.set("ter_status", TerminalsService.TER_STATUS_1);
        ActionHandler.currentSession().update(obj);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "terminals.TerminalsAH.list");
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;

    }

    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(TerminalsService.TABLE_NAME);
        BizObject obj = terminalsService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "terminals.TerminalsAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    /**
     * 注销
     *
     * @throws SQLException
     */
    public void del() throws SQLException {
        BizObject obj = terminalsService.getById(this._objId);
        //将状态改为待初始化
        obj.set("ter_status", TerminalsService.TER_STATUS_4);
        ActionHandler.currentSession().update(obj);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "terminals.TerminalsAH.list");
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;

    }
}
