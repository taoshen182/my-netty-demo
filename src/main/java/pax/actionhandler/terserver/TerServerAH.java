package pax.actionhandler.terserver;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.terserver.TerServerService;
import sand.actionhandler.system.ActionHandler;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/2.
 * <p>
 * 终端服务商管理 ：终端网络接入商管理 & 终端产权方管理
 */
public class TerServerAH extends ActionHandler {

    private TerServerService terServerService;

    public TerServerAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    protected void initial() {
        super.initial();
        terServerService = (TerServerService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("terServerService");

    }

    public void list() throws SQLException {
        this.pushLocation("服务商信息列表", "terserver.TerServerAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        if (param == null) param = new BizObject("param");
//        String type = this.getParameter("type");
//        if (StringUtils.isNotBlank(type)) param.set("type", type);

        List<BizObject> list = terServerService.list(param, this.preparePageVar());
        this._request.setAttribute("param", param);
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/terserver/list.jsp";
    }

    public void show() throws SQLException {
        this.pushLocation("服务商信息编辑", "terserver.TerServerAH.show");

        BizObject obj = terServerService.getById(this._objId);

        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/terserver/edit.jsp";
    }

    public void save() throws SQLException {

        BizObject biz = this.getBizObjectFromMap(TerServerService.TABLE_NAME);
        BizObject obj = terServerService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "terserver.TerServerAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void del() {
    }
}
