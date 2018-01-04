package pax.actionhandler.script;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.app.AppService;
import pax.service.script.ScriptService;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/7/18.
 */
public class ScriptAH extends ActionHandler {

    static Logger logger = Logger.getLogger(ScriptAH.class);

    private ScriptService scriptService;

    public ScriptAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }


    @Override
    protected void initial() {
        super.initial();
        scriptService = (ScriptService) WebApplicationContextUtils.getRequiredWebApplicationContext(ActionHandler._context).getBean("scriptService");
    }

    public void list() throws SQLException {
        this.pushLocation("脚本程序列表", "script.ScriptAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        List<BizObject> list = scriptService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/script/list.jsp";

    }

    public void show() throws SQLException {
        this.pushLocation("应用编辑", "script.ScriptAH.show");

        BizObject obj = scriptService.getById(this._objId);

        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/script/edit.jsp";
    }

    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(ScriptService.TABLE_NAME);
        BizObject obj = scriptService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "script.ScriptAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    @Ajax
    public String upload() throws Exception {
        File uploadFile = this.getUploadFile();
        logger.info("[ScriptAH upload]");
        if (!uploadFile.getName().endsWith(".xml")) return ReturnCode.errorData("7782", "请上传xml格式的文件").toString();
        //TODO 上传文件

        return ReturnCode.successData().toString();


    }

}
