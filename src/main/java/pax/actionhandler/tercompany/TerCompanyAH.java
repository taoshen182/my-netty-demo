package pax.actionhandler.tercompany;

import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.tercompany.TerCompanyService;

import pax.service.terserver.TerServerService;
import pax.utils.AnalyzeJarUtils;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by want on 2017/6/5.
 */
public class TerCompanyAH extends ActionHandler {
    private TerCompanyService terCompanyService;

    public TerCompanyAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    protected void initial() {
        super.initial();
        terCompanyService = (TerCompanyService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("terCompanyService");

    }

    public void list() throws SQLException {
        this.pushLocation("终端厂商列表", "tercompany.TerCompanyAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        List<BizObject> list = terCompanyService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/tercompany/list.jsp";
    }

    public void show() throws SQLException {
        this.pushLocation("终端厂商信息编辑", "tercompany.TerCompanyAH.show");

        BizObject obj = terCompanyService.getById(this._objId);

        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/tercompany/edit.jsp";
    }

    public void save() throws SQLException {
        BizObject biz = this.getBizObjectFromMap(TerCompanyService.TABLE_NAME);
        BizObject obj = terCompanyService.addOrUpdate(biz);

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "tercompany.TerCompanyAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void del() {
    }

    @Ajax
    public String uploadJar() {
        try {
            BizObject obj = terCompanyService.getById(this._objId);
            System.out.println("obj = " + obj);

            File uploadFile = this.getUploadFile();

            //不包含 rule_path 中的类
            if (!checkFile(obj, uploadFile)) return ReturnCode.errorData("0101", "jar is wrong").toString();

            //将厂商jar写入到 lib_ter 里面 C:\project\tms\lib_ter
            String root_path = this._request.getSession().getServletContext().getRealPath("/");
            String lib_ter = new File(root_path).getParentFile().getParentFile().getParent() + File.separator + "lib_ter";

            //路径不存在，创建
            if (!lib_ter.endsWith(File.separator)) {
                lib_ter = lib_ter + File.separator;
            }
            File lib = new File(lib_ter);
            if (!lib.exists()) {
                if (!lib.mkdirs()) {
                    System.out.println("mkdirs error");
                }
            }

            File lib_jar = new File(lib_ter + uploadFile.getName());
            if (lib_jar.exists()) {
                //检查本地原有的jar
                if (checkFile(obj, lib_jar)) return ReturnCode.errorData("0102", "jar is exists").toString();
                else lib_jar.delete();
            }

            InputStream in = new FileInputStream(uploadFile);
            OutputStream out = new FileOutputStream(lib_jar);

            byte[] buffer = new byte[1024];
            int byteread = 0; // 读取的字节数
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }

            // TODO 重新编译一下

            BizObject ret = new BizObject();
            ret.set("name", uploadFile.getName());
            ret.set("size", uploadFile.length());

            return ReturnCode.successData(ret).toString();

        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM).toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM).toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM).toString();
        }


    }

    private boolean checkFile(BizObject obj, File uploadFile) throws IOException, ClassNotFoundException {
        List<String> allClassNamesByFile = AnalyzeJarUtils.getAllClassNamesByFile(uploadFile);
        if (allClassNamesByFile.contains(obj.getString("rule_path"))) {
            return true;//包含
        }
        return false;
    }
}
