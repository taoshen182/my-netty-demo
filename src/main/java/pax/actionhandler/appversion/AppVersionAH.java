package pax.actionhandler.appversion;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pax.actionhandler.basic.Bundle;
import pax.service.ReturnCode;
import pax.service.appversion.AppVersionService;
import pax.service.tercompany.TerCompanyService;
import pax.service.tertype.TerTypeService;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.tool.system.ControllableException;
import sand.utils.JsonTool;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.coyote.http11.Constants.a;

/**
 * Created by want on 2017/6/12.
 */
public class AppVersionAH extends ActionHandler {
    static Logger logger = Logger.getLogger(AppVersionAH.class);

    public AppVersionAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    private AppVersionService appVersionService;
    private TerTypeService terTypeService;
    private TerCompanyService terCompanyService;

    @Override
    protected void initial() {
        super.initial();
        appVersionService = (AppVersionService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("appVersionService");
        terTypeService = (TerTypeService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("terTypeService");
        terCompanyService = (TerCompanyService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("terCompanyService");
    }

    public void list() throws SQLException {
        this.pushLocation("应用版本列表", "appversion.AppVersionAH.list");

        BizObject param = this.getBizObjectFromMap("param");
        List<BizObject> list = appVersionService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/appversion/list.jsp";

    }

    public void show() throws SQLException {
        this.pushLocation("应用版本编辑", "appversion.AppVersionAH.show");

        BizObject obj = appVersionService.getById(this._objId);
        List<BizObject> ter_type = null;
        if (obj != null) {
            String ter_com_id = obj.getString("ter_com_id");
            ter_type = appVersionService.getTerType(this._objId);

        }
        this._request.setAttribute("ter_type", ter_type);
        this._request.setAttribute("obj", obj);
        this._nextUrl = "template/appversion/edit.jsp";
    }

    @Ajax
    public String saveTertype() throws SQLException {
        String app_ver_id = this.getParameter("app_ver_id");
        String ter_type_ids = this.getParameter("ter_type_ids");

        BizObject appVer = appVersionService.getById(app_ver_id);
        List<BizObject> ter_types = new ArrayList<>();

        String[] ter_type_id_array = ter_type_ids.split(",");
        for (String ter_type_id : ter_type_id_array) {
            BizObject terType = terTypeService.getById(ter_type_id);
            ter_types.add(terType);
        }
        BizObject ret = appVersionService.addOrUpdate(appVer, ter_types);
        return ret.toString();
    }

    public void save() throws JSONException {
        BizObject biz = this.getBizObjectFromMap(AppVersionService.TABLE_NAME);


        BizObject obj = appVersionService.addOrUpdate(biz,null);

        if (!"0000".equals(obj.getString("respcode"))) throw new ControllableException(obj.getString("respmsg"));

        this._tipInfo = Bundle.getString("operation.success");
        this.setAttribute("nextUrl", "appversion.AppVersionAH.show?&objId=" + obj.getId());
        this.setAttribute("msg_type", "SUCCESS");
        this._nextUrl = super._msgUrl;
    }

    public void chooseTertype() throws SQLException {
        BizObject param = this.getBizObjectFromMap(TerTypeService.TABLE_NAME);
        if (param == null) param = new BizObject(TerTypeService.TABLE_NAME);

        List<BizObject> list = terTypeService.list(param, this.preparePageVar());
        this._request.setAttribute("objList", list);
        this._nextUrl = "template/appversion/chooseTertype.jsp";
    }

    @Ajax
    public String upload() throws Exception {
        File uploadFile = this.getUploadFile();
        logger.info("[AppVersionAH upload]uploadFile" + uploadFile);

        String uploadPath = this.getParameter("uploadPath");
        logger.info("[AppVersionAH upload]uploadPath" + uploadPath);

        //获取Rule的路径
        String rule_path = getRulePath(this.getParameter("objId"));
        logger.info("[AppVersionAH upload]rule_path" + rule_path);

        //根据 应用-->机型-->厂商-->插件路径 （如：com.landi.tms.thirdinterface.LandiRule  / "com.landi.tms.thirdinterface.PaxRule"）
        // ，调用不同的jar，解析文件
        boolean b = checkAppNameVer(uploadFile, rule_path);
        logger.info("[AppVersionAH upload]checkAppNameVer" + b);

        if (copyFile(uploadFile, uploadPath, null)) {
            BizObject ret = new BizObject();
            ret.set("name", uploadFile.getName());
            ret.set("size", uploadFile.length());

            return ReturnCode.successData(ret).toString();
        } else {
            return ReturnCode.errorData(ReturnCode.ERROR_SYSTEM).toString();
        }

    }

    /**
     * 检查上传文件的应用名称和应用版本
     *
     * @param uploadFile 上传的应用文件
     * @param rule_path  检查文件的JAR路径
     * @throws Exception
     */
    private boolean checkAppNameVer(File uploadFile, String rule_path) throws Exception {
        logger.info("[AppVersionAH checkAppNameVer]rule_path : " + rule_path);
        Class<com.landi.tms.thirdinterface.Rule> aClass = (Class<com.landi.tms.thirdinterface.Rule>) Class.forName(rule_path);
        com.landi.tms.thirdinterface.Rule rule = aClass.newInstance();

        //在tomcat中的路径
        String absolutePath = uploadFile.getAbsolutePath();
        logger.info("[AppVersionAH checkAppNameVer]absolutePath : " + absolutePath);
        String file_path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator) + 1);

        String appName = rule.getAppName(file_path);//nima ，各种返回值啊
        String appVer = rule.getAppVer(file_path);

        logger.info("[AppVersionAH checkAppNameVer]appName = " + appName);
        logger.info("[AppVersionAH checkAppNameVer]appVer = " + appVer);

        if (appName == null || appName.contains("error")) {
            return false;
        }
        if (appVer == null || appVer.contains("error")) {
            return false;
        }

        return true;
    }

    private String getRulePath(String objId) throws SQLException {
        BizObject appVersion = appVersionService.getById(objId);
        BizObject ter_type = terTypeService.getById(appVersion.getString("ter_type_id"));
        BizObject terCompany = terCompanyService.getById(ter_type.getString("ter_com_id"));
        return terCompany.getString("rule_path");
    }

    /**
     * MD5校验码生成
     */
    @Ajax
    public String getMD5() {


        return "md5";
    }

    /**
     * 将上传的文件写入到指定的路径
     *
     * @param file         上传的文件
     * @param destDirName  目标文件夹，如果没有则新建
     * @param destFileName 目标文件名
     * @return
     */
    private boolean copyFile(File file, String destDirName, String destFileName) throws Exception {
        logger.info("[AppVersionAH copyFile]OS_TYPE = " + ActionHandler.OS_TYPE + ", destDirName = " + destDirName + ", destFileName = " + destFileName + " , file = " + file);
        if (file == null) return false;

        if (StringUtils.isBlank(destFileName)) destFileName = file.getName();//取 原文件名

        if (ActionHandler.OS_TYPE.equals("windows")) {
//            destDirName = System.getProperty("user.home");
            destDirName = ActionHandler.getContextRootPath();
            logger.info("[AppVersionAH copyFile]windows destDirName = " + destDirName);
        }


        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        // 如果目标文件夹存在
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                logger.info("[AppVersionAH copyFile]mkdirs error");
                return false;
            }
            logger.info("[AppVersionAH copyFile]mkdirs success");
        }

        try (InputStream in = new FileInputStream(file);
             OutputStream out = new FileOutputStream(new File(destDirName + destFileName));) {
            byte[] buffer = new byte[1024];
            int byteread = 0; // 读取的字节数
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("[AppVersionAH copyFile]FileNotFoundException ", e);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("[AppVersionAH copyFile]IOException ", e);
            return false;
        }
        return true;
    }

    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;

        try (FileInputStream in = new FileInputStream(file)) {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
