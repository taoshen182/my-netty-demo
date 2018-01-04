package pax.actionhandler.inst;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.inst.InstService;
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
public class InstAH extends ActionHandler {
    public InstAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    private InstService instService;

    @Override
    protected void initial() {
        super.initial();
        instService = (InstService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("instService");
    }

    public void list() throws SQLException {
        this.pushLocation("机构列表", "inst.InstAH.list");

        BizObject param = this.getBizObjectFromMap(InstService.TABLE_NAME);
        List<BizObject> list = instService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/inst/list.jsp";

    }

    public void show() throws SQLException {
        this.pushLocation("机构编辑", "inst.InstAH.show");
        BizObject obj = instService.getById(this._objId);
        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/inst/edit.jsp";
    }

    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(InstService.TABLE_NAME);

        //加密方式
        String enc_type = biz.getString("enc_type");
        if ("0".equals(enc_type)) {//软加密
            if ("1".equals(biz.getString("key_type"))) {//固定密钥
                if (StringUtils.isBlank(biz.getString("fixed_key"))) {
                    throw new ControllableException("固定密钥不能为空");
                }


            } else {//随机密钥
                biz.set("fixed_key", "");
            }

            biz.set("ptc_key_index", "");
            biz.set("mis_key_index", "");

        } else {//硬加密
            //除了保护密钥索引和mis保护密钥索引的，其他为空
            if (StringUtils.isBlank(biz.getString("ptc_key_index")) || StringUtils.isBlank(biz.getString("mis_key_index"))) {
                throw new ControllableException("保护密钥索引和mis保护密钥索引必填");
            }
            biz.set("key_type", "");
            biz.set("fixed_key", "");
        }

        BizObject obj = instService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "inst.InstAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }
}
