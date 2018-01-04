package pax.actionhandler.pos;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import pax.model.RsaKey;
import pax.model.SessionVar;
import pax.model.json.InCmd;
import pax.model.json.OutCmd;
import pax.model.json.cont.C2302;
import pax.service.CacheService;
import pax.service.FileService;
import pax.service.MongoFile;
import pax.service.pos.DispatchService;
import pax.service.pos.TerminalService;
import pax.util.*;
import sand.actionhandler.open.OpenError;
import sand.actionhandler.open.OpenServiceException;
import sand.actionhandler.open.ServiceClient;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Download;
import sand.basic.Global;
import sand.utils.JsonTool;
import tool.dao.BizObject;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class openservice extends PosOpenService {
	
	//public static final long expire_minute=10;  //过期时间（分钟）

	static Logger logger = Logger.getLogger(openservice.class);

	public openservice(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		//super._isPosOpenService = true;
		//super._isOpenService =true;

	}

	public BizObject detail(){

	//	CmdUtils.checkCmd(incmd,Context.cmd_detail_in);
		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//		JSONObject obj = JSONObject.fromObject(content);
		Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
		//logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());

		//obj.get
		return TerminalService.getInstance().getAppDetail(obj);

	}
	
	public BizObject detailForPkg(){

		//	CmdUtils.checkCmd(incmd,Context.cmd_detail_in);
			String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//			JSONObject obj = JSONObject.fromObject(content);
			Map obj = JsonUtils.read(content);
//			logger.info("var is "+var);
		//	logger.info("obj is "+obj);
			obj.put("terminal_sn", var.getSn());
			
			//obj.get
			return TerminalService.getInstance().getAppDetailForPkg(obj);

	}
	
	
	public BizObject init(){
		
//		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//		logger.info("content "+content);
//		Map obj = JsonUtils.read(content);

		Map obj = new HashMap();//JsonUtils.read(content);

	//	logger.info("var is "+var);
	//	logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
	//	logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.TerminalOpenService.init", obj);
		return t;

	}	

	
	public BizObject initNotice(){
		
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//		String content = incmd.getCont();
//		logger.info("content "+content);
//		Map obj = JsonUtils.read(content);
		
		Map obj = new HashMap();//JsonUtils.read(content);
	//	logger.info("var is "+var);
	//	logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());

		
		//JSONObject obj = JSONObject.fromObject(content);
	//	logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.TerminalOpenService.initNotice", obj);
		return t;

	}	
	

	public BizObject notice(){
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		String content = incmd.getCont();
		//logger.info("content "+content);
		Map obj = JsonUtils.read(content);
	//	logger.info("var is "+var);
	//	logger.info("obj is "+obj);
//		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
	//	logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.PlanTaskOpenService.notice", obj);
		return t;
		
	}
	public BizObject task(){
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		//logger.info("content "+content);
		Map obj = new HashMap();//JsonUtils.read(content);
	//	logger.info("var is "+var);
		//logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.PlanTaskOpenService.queryPlanTasks", obj);
		return t;
		
	}

	public BizObject monitor(){
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		//logger.info("content "+content);
		Map obj = new HashMap();//JsonUtils.read(content);
		
		String content = incmd.getCont();
//		logger.info("content "+content);
		//Map datas = JsonUtils.read(content);
		obj.put("datas", content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.TerminalOpenService.uploadMonitor", obj);
		return t;
		
	}
	
	public BizObject list_ad(){
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		//logger.info("content "+content);
//		Map obj = new HashMap();//JsonUtils.read(content);
		String content = incmd.getCont();
//		logger.info("content "+content);
		Map obj = JsonUtils.read(content);

//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.InformationOpenService.queryInfo", obj);
		return t;
		
	}

	
	public BizObject detail_ad(){
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		//logger.info("content "+content);
//		Map obj = new HashMap();//JsonUtils.read(content);
		String content = incmd.getCont();
	//	logger.info("content "+content);
		Map obj = JsonUtils.read(content);

	//	logger.info("var is "+var);
		//logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
	//	logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.InformationOpenService.getInfoDetail", obj);
	//	logger.info("return "+t);
		return t;
		
	}
	
	public String uploadParams(){
		String content = incmd.getCont();
	//	logger.info("content "+content);
		//Map obj = JsonUtils.read(content);
		BizObject obj=new BizObject();
		try {
			obj = JsonTool.toBiz(content);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OpenServiceException(OpenError.ERROR_SYSTEM);
		}
	//	logger.info("var is "+var);
	//	logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
	//	logger.info("obj is "+obj);
		String t = ServiceClient.getInstance().execute_str("open.TerminalOpenService.uploadParams", obj);
		return t;
		
	}
	public BizObject param(){
		
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		String content = incmd.getCont();
	//	logger.info("content "+content);
		Map obj = JsonUtils.read(content);
		//logger.info("var is "+var);
	//	logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
	//	logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.PlanTaskOpenService.queryParams", obj);
		return t;

	}	

	
	public BizObject install(){
		
	//CmdUtils.checkCmd(incmd,Context.cmd_list_in);
		
		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//		logger.info("content "+content);
		Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject b = TerminalService.getInstance().install(obj);
		return b;

	}
	
	
	public BizObject uninstall(){
		
	//CmdUtils.checkCmd(incmd,Context.cmd_list_in);
		
		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		Map obj = JsonUtils.read(content);
		obj.put("terminal_sn", var.getSn());
		BizObject b = TerminalService.getInstance().uninstall(obj);
		return b;
//		OutCmd outcmd = new OutCmd(Context.cmd_list_out);
//		
//        SecretKey key = var.getKey();
//        String cont=AES.encrypt(b.toString(), key);
//        outcmd.setCont(cont);
//		return outcmd;

	}

	/**
	 * 已安装列表
	 * @return
	 */
	public OutCmd list(){
		//CmdUtils.checkCmd(incmd,Context.cmd_list_in);

		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//		logger.info("content: "+content);

		BizObject b =null;
		try {
			b = JsonTool.toBiz(content);
			//b.set("");
			b.set("terminal_sn", var.getSn());
		} catch (JSONException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			//throw new OpenServiceException(OpenError.ERROR_SYSTEM.ERROR_BADJSON);
			//兼容2.0

			b= new BizObject();
			b.set("terminal_sn", var.getSn());
			b.set("datas",content);
		}

		//b.set("terminal_sn", var.getSn());

		BizObject t = ServiceClient.getInstance().execute("open.AppOpenService.queryInstallForAppStore", b);

		OutCmd outcmd = new OutCmd(Context.cmd_list_out);

//		C1301 c1301 = new C1301();
//        c1301.setApks(list);

		//SecretKey key = ;
		String cont=AES.encrypt(t.toString(), var.getKey());


		outcmd.setCont(cont);
		//incmd.setCont(cont);
		outcmd.setRespcode("0000");
		outcmd.setRespmsg("成功");
		return outcmd;
	}

	/**商城列表**/
	public OutCmd listAll(){
		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//		BizObject b = new BizObject();//.

		BizObject b =null;
		try {
			b = JsonTool.toBiz(content);
			//b.set("");
			b.set("terminal_sn", var.getSn());
		} catch (JSONException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			//throw new OpenServiceException(OpenError.ERROR_SYSTEM.ERROR_BADJSON);
			//兼容2.0

			b= new BizObject();
			b.set("terminal_sn", var.getSn());
			b.set("datas",content);
		}
//		b.set("terminal_sn", var.getSn());
		logger.info("商城列表:"+b.toString());

		BizObject t = ServiceClient.getInstance().execute("open.AppOpenService.queryAppsForAppStore", b);

		OutCmd outcmd = new OutCmd(Context.cmd_list_out);

//		C1301 c1301 = new C1301();
//        c1301.setApks(list);

		//SecretKey key = ;
		String cont=AES.encrypt(t.toString(), var.getKey());


		outcmd.setCont(cont);
		//incmd.setCont(cont);
		outcmd.setRespcode("0000");
		outcmd.setRespmsg("成功");
		return outcmd;

		//return t;
		
	}	

	




	/**
	 * ota通知
	 */
	public BizObject ignoreApp(){
		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		//String content = incmd.getCont();
	//	logger.info("content "+content);
		Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());


		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.AppOpenService.ignoreApp", obj);
		return t;

		//return b;
	}
	/**
	 * ota通知
	 */
	public BizObject qryOta(){
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		String content = incmd.getCont();
//		logger.info("content "+content);
		Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.FirmwareOpenService.queryNewFirmware", obj);
		return t;

		//return b;
	}
	
	public BizObject download_success(){
		
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		String content = incmd.getCont();
	//	logger.info("content "+content);
		Map obj = JsonUtils.read(content);
	//	logger.info("var is "+var);
	//	logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.FirmwareOpenService.downloadSuccess", obj);
		return t;

	}	

	
	public BizObject otaNotice(){
		
		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		String content = incmd.getCont();
	//	logger.info("content "+content);
		Map obj = JsonUtils.read(content);
	//	logger.info("var is "+var);
	//	logger.info("obj is "+obj);
		obj.put("terminal_sn", var.getSn());
		
		//JSONObject obj = JSONObject.fromObject(content);
	//	logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.FirmwareOpenService.installSuccess", obj);
		return t;

	}


	public BizObject queryUpdatesList(){

		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
	//	String content = incmd.getCont();
//		logger.info("content "+content);
	//	Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		//		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//		logger.info("content: "+content);


		BizObject b = null;//.

		try {
			b = JsonTool.toBiz(content);
			//b.set("");
			b.set("terminal_sn", var.getSn());
		} catch (JSONException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			//throw new OpenServiceException(OpenError.ERROR_SYSTEM.ERROR_BADJSON);
			//兼容2.0

			b= new BizObject();
			b.set("terminal_sn", var.getSn());
			b.set("datas",content);
		}

		//Map m = new HashMap();
		//m.put("terminal_sn", var.getSn());

//		obj.put("terminal_sn", var.getSn());

		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.AppOpenService.queryUpdatesList", b);
		return t;

	}



	public BizObject queryVersionsByPkg(){
		String content = incmd.getCont();
//		logger.info("content "+content);
		Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		//Map m = new HashMap();

		obj.put("terminal_sn", var.getSn());

		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.AppOpenService.queryVersionsByPkg", obj);
		return t;

	}

	public BizObject queryUpdates(){

		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		String content = incmd.getCont();
//		logger.info("content "+content);
		Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		//Map m = new HashMap();
		obj.put("terminal_sn", var.getSn());

		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.AppOpenService.queryUpdates", obj);
		return t;

	}


	public BizObject uploadTerminalLog(){

		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		String content = incmd.getCont();
//		logger.info("content "+content);
		Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		//Map m = new HashMap();
		obj.put("terminal_sn", var.getSn());

		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.TerminalLogTaskOpenService.uploadTerminalLog", obj);
		return t;

	}

	public BizObject queryGetLogTasks(){

		//String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
		//String content = incmd.getCont();
//		logger.info("content "+content);
		//Map obj = JsonUtils.read(content);
//		logger.info("var is "+var);
//		logger.info("obj is "+obj);
		Map obj = new HashMap();
		obj.put("terminal_sn", var.getSn());

		//JSONObject obj = JSONObject.fromObject(content);
//		logger.info("obj is "+obj);
		BizObject t = ServiceClient.getInstance().execute("open.TerminalLogTaskOpenService.queryGetLogTasks", obj);
		return t;

	}


	public BizObject queryStrategy(){

		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();


		//String method = "AppAH.AppOpenService.queryInstallForAppStore";
		Map m = new HashMap();
		m.put("terminal_sn", var.getSn());
		m.put("datas", content);
		BizObject t = ServiceClient.getInstance().execute("open.AppOpenService.queryStrategy", m);
//		logger.info("t is "+t);
		return t;


	}


	public BizObject getMerBySN(){

//		String content = AES.decrypt(incmd.getCont(), var.getKey());//incmd.getCont();
//		//String content = incmd.getCont();
//		logger.info("content "+content);


		//String method = "AppAH.AppOpenService.queryInstallForAppStore";
		Map m = new HashMap();
		m.put("terminal_sn", var.getSn());
	//	m.put("datas", content);
		BizObject t = ServiceClient.getInstance().execute("open.TerminalOpenService.getMerBySN", m);
		//logger.info("t is "+t);
		return t;


	}



}
