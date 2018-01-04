package pax.actionhandler.app;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.app.AppService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/12.
 */
public class AppAH extends ActionHandler {
    static Logger logger = Logger.getLogger(AppAH.class);

    public AppAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    private AppService appService;

    @Override
    protected void initial() {
        super.initial();
        appService = (AppService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("appService");

    }

    public void list() throws SQLException {
        this.pushLocation("应用列表", "app.AppAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        List<BizObject> list = appService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/app/list.jsp";

    }

    public void show() throws SQLException {
        this.pushLocation("应用编辑", "app.AppAH.show");

        BizObject obj = appService.getById(this._objId);

        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/app/edit.jsp";
    }

    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(AppService.TABLE_NAME);
        BizObject obj = appService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "app.AppAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void del() throws SQLException {

        BizObject ret = appService.delById(this._objId);
        if (!ReturnCode.SUCCESS.equals(ret.getString("respcode"))) {
            throw new ControllableException(ret.getString("respmsg"));
        }

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "app.AppAH.list");
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }
}
