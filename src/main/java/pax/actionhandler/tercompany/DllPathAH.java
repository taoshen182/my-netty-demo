package pax.actionhandler.tercompany;

import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.tercompany.DllPathService;
import pax.service.tercompany.TerCompanyService;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import tool.dao.BizObject;
import tool.dao.JDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.l;

/**
 * Created by want on 2017/7/20.
 */
public class DllPathAH extends ActionHandler {
    private DllPathService dllPathService;

    public DllPathAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
        dllPathService = (DllPathService) WebApplicationContextUtils.getRequiredWebApplicationContext(ActionHandler._context).getBean("dllPathService");
    }

    public void list() throws SQLException {
        this.pushLocation("DLL列表", "tercompany.DllPathAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        List<BizObject> list = dllPathService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/dll_path/list.jsp";
    }

    public void show() throws SQLException {
        this.pushLocation("DLL编辑", "tercompany.DllPathAH.show");

        BizObject obj = dllPathService.getById(this._objId);

        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/dll_path/edit.jsp";
    }

    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(DllPathService.TABLE_NAME);
        BizObject obj = dllPathService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "tercompany.DllPathAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    @Ajax
    public String getDllPathByTerComId() throws SQLException {
        String ter_com_id = this.getParameter("ter_com_id");
        List<BizObject> dllPathByTerComId = dllPathService.getDllPathByTerComId(ter_com_id);
        BizObject ret = new BizObject();
        ret.set("datas", dllPathByTerComId);
        return ReturnCode.successData(ret).toString();

    }

    public void del() {

    }
}
