package pax.actionhandler.pos;

import org.apache.log4j.Logger;
import org.json.JSONException;
import pax.model.json.OutCmd;
import pax.service.pos.TerminalService;
import pax.util.AES;
import pax.util.Context;
import pax.util.JsonUtils;
import sand.actionhandler.open.OpenError;
import sand.actionhandler.open.OpenServiceException;
import sand.actionhandler.open.ServiceClient;
import sand.depot.tool.system.SystemKit;
import sand.utils.JsonTool;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class otaservice extends PosOpenService {

	//public static final long expire_minute=10;  //过期时间（分钟）

	static Logger logger = Logger.getLogger(otaservice.class);

	public otaservice(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		//super._isPosOpenService = true;
		//super._isOpenService =true;

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






	public BizObject checkUpdates(){

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
		BizObject t = ServiceClient.getInstance().execute("open.AppOpenService.checkUpdates", obj);
		return t;

	}


	public BizObject  qryZCList() throws SQLException {

		List v = new ArrayList();
		List<BizObject> v2 = SystemKit.getCachePickList("zonecode");

		for(BizObject b2:v2){
			BizObject b = new BizObject();
			b.set("zoneCode",b2.getString("id"));
			b.set("addr",b2.getString("name"));
			v.add(b);
		}


		BizObject ret = new BizObject();
		ret.set("respcode","0000");
		ret.set("respmsg","success");
		ret.set("zc_list",v);

		return ret;


	}





}
