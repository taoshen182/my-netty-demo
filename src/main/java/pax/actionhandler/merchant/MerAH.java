package pax.actionhandler.merchant;

import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.merchant.MerchantService;
import pax.service.tercompany.TerCompanyService;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/16.
 */
public class MerAH extends ActionHandler {
    public MerAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    private MerchantService merchantService;

    @Override
    protected void initial() {
        super.initial();
        merchantService = (MerchantService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("merchantService");
    }

    public void list() throws SQLException {
        this.pushLocation("商户列表", "merchant.MerAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        List<BizObject> list = merchantService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/merchant/list.jsp";
    }

    public void show() throws SQLException {
        this.pushLocation("商户信息编辑", "merchant.MerAH.show");

        BizObject obj = merchantService.getById(this._objId);

        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/merchant/edit.jsp";
    }

    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(MerchantService.TABLE_NAME);
        BizObject obj = merchantService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "merchant.MerAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void del() {
    }

    public void frozen() throws SQLException {
        BizObject obj = merchantService.getById(this._objId);
        if (!MerchantService.STATUS_2.equals(obj.getString("status"))) {
            throw new ControllableException("只要启用状态才可以冻结");
        }
        obj.set("status", MerchantService.STATUS_1);
        ActionHandler.currentSession().update(obj);
        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "merchant.MerAH.list");
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void unfrozen() throws SQLException {
        BizObject obj = merchantService.getById(this._objId);
        if (MerchantService.STATUS_2.equals(obj.getString("status"))) {
            throw new ControllableException("该商户已启用");
        }
        obj.set("status", MerchantService.STATUS_2);
        ActionHandler.currentSession().update(obj);
        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "merchant.MerAH.list");
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }
}
