package pax.actionhandler.tertype;

import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.terserver.TerServerService;
import pax.service.tertype.TerTypeService;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/8.
 */
public class TerTypeAH extends ActionHandler {
    public TerTypeAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    private TerTypeService terTypeService;

    protected void initial() {
        super.initial();
        terTypeService = (TerTypeService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("terTypeService");

    }

    public void list() throws SQLException {
        this.pushLocation("终端型号列表", "tertype.TerTypeAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        if (param == null) param = new BizObject("param");

        List<BizObject> list = terTypeService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/tertype/list.jsp";
    }

    public void show() throws SQLException {
        this.pushLocation("终端型号编辑", "tertype.TerTypeAH.show");

        BizObject obj = terTypeService.getById(this._objId);
        if (obj != null) {
            String dll_path = SystemKit.getCacheParamById("dll_path", obj.getString("dll_path_id"));
            obj.set("dll_path", dll_path);
        }

        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/tertype/edit.jsp";
    }

    @Ajax
    public String getTertypeByTerComId() throws SQLException {
        List<BizObject> terTypeList = terTypeService.getTerTypeByTerComId(this.getParameter("ter_com_id"));
        BizObject ret = new BizObject();
        ret.set("datas", terTypeList);
        return ReturnCode.successData(ret).toString();
    }


    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(TerTypeService.TABLE_NAME);
        BizObject obj = terTypeService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "tertype.TerTypeAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void del() {
    }
}
