package pax.actionhandler.tpm;

import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.tertype.TerTypeService;
import pax.service.tpm.TpmService;
import sand.actionhandler.system.ActionHandler;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/13.
 */
public class TpmAH extends ActionHandler {
    public TpmAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    private TpmService tpmService;

    @Override
    protected void initial() {
        super.initial();
        tpmService = (TpmService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("tpmService");
    }
    public void list() throws SQLException {
        this.pushLocation("TPM列表", "tpm.TpmAH.list");

        BizObject param = this.getBizObjectFromMap(TpmService.TABLE_NAME);
        if (param == null) param = new BizObject(TpmService.TABLE_NAME);

        List<BizObject> list = tpmService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/tpm/list.jsp";
    }
    public void show() throws SQLException {
        this.pushLocation("TPM编辑", "tpm.TpmAH.show");

        BizObject obj = tpmService.getById(this._objId);

        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/tpm/edit.jsp";
    }
    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(TpmService.TABLE_NAME);
        BizObject obj = tpmService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "tpm.TpmAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void del() {
    }
}
