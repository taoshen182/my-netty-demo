package pax.service;

import pax.actionhandler.basic.Bundle;
import sand.actionhandler.open.OpenError;
import tool.dao.BizObject;

import java.util.HashMap;
import java.util.List;

public class ReturnCode {

	public static final String SUCCESS = OpenError.SUCCESS.getCode();
	public static final String NO_NEW_VERSION = "0103";
	public static final String ERROR_PARAM_NULL = "0101";
	public static final String ERROR_FILE_TYPE = "0102";
	public static final String ERROR_FILE = "0107";			
	public static final String ERROR_EXSIT = "0104";
	public static final String ERROR_NOTEXSIT = "0005";
	public static final String ERROR_STATUS = "0106";
	
	public static final String ERROR_TERMINAL = OpenError.ERROR_SN.getCode();
	public static final String ERROR_INST = "0108";
	public static final String ERROR_APP = "0109";
	public static final String ERROR_PARSE_APK = "0110";
	public static final String ERROR_PKG = "0111";
	public static final String ERROR_CANNOTDEL = "0112";
	public static final String ERROR_DAY = "0113";
	public static final String ERROR_PARAM_TYPE ="0114";
	
	public static final String ERROR_FILE_NULL = "1001";
	public static final String ERROR_INST_NULL = "1002";
	public static final String ERROR_INFO_NULL = "1003";
	public static final String ERROR_INFO_NO = "1004";
	public static final String ERROR_LOGINNAME_EXSIT = "1005";
	public static final String ERROR_TEL_EXSIT = "1006";
	public static final String ERROR_INST_NODEL = "1007";
	public static final String ERROR_HAVE_SON = "1008";
	public static final String ERROR_FILE_MAX = "1009";
	public static final String ERROR_PN = "1010";
	public static final String ERROR_HAVE_BRANCH = "1011";
	public static final String ERROR_VERSION_CODE = "1012";
	public static final String ERROR_PARSE_LNGANDLAT = "1013";
	public static final String ERROR_UPLOAD_FILE_PLEASE = "1014";
	
	public static final String ERROR_STRATEGY_STRATEGYUPDATE = "1015";
	public static final String ERROR_STRATEGY_APPUPDATE = "1016";
	
	public static final String ERROR_VERSION_NOT_SAME = "1017";
	public static final String ERROR_END_VERSION_GT_START_VERSION = "1018";
	public static final String ERROR_FILE_PATTERN = "1019";
	public static final String ERROR_VERIFY_FAIL = "1020";
	public static final String ERROR_TRANS_TYPE = "1021";
	public static final String ERROR_COUNTRY = "1022";
	public static final String ERROR_PROVINCE = "1023";
	public static final String ERROR_CITY = "1024";
	public static final String ERROR_AREA = "1025";
	public static final String ERROR_MER_CODE = "1026";
	public static final String ERROR_CODE_EQUALS_PCODE = "1027";
	public static final String ERROR_ROOT_INST = "1028";


	public static final String ERROR_SYSTEM = OpenError.ERROR_SYSTEM.getCode();
	public static final String ERROR_DBLCLICK = "2002";
	public static final String CODE_0034 = "PAX0034";
	public static final String CODE_9903 = "PAX9903";

	public static final String ERROR_PARSE_AREA = "1029";
	public static final String ERROR_USER_NULL="1030";//htf
	public static final String ERROR_APK_IS_INSTALL="1031";// apk已被安装 无法替换apk htf
	
	
	
	
	public static HashMap<String,String> returnMap = new HashMap<String,String>();
	
	static{
		returnMap.put(SUCCESS,"success");//成功
		returnMap.put(NO_NEW_VERSION,"no_new_version");//没有新版本
		returnMap.put(ERROR_SYSTEM,"CODE_9990");//系统错误
		returnMap.put(ERROR_DBLCLICK,"duplicate_commit");//重复提交
		returnMap.put(ERROR_PARAM_NULL,"param_is_null");//参数为空
		returnMap.put(ERROR_EXSIT,"exist.record");//记录已存在
		returnMap.put(ERROR_NOTEXSIT,"no_record");//无记录
		returnMap.put(ERROR_FILE_TYPE, "error.file_type");//文件类型错误
		returnMap.put(ERROR_FILE, "illegal_file");//非法文件
		returnMap.put(ERROR_STATUS, "CODE_0012");//状态错误
		
		returnMap.put(ERROR_TERMINAL, "error.terminal");//终端号错误或终端状态错误
		returnMap.put(ERROR_INST, "error.institution");//机构错误
		returnMap.put(ERROR_PARSE_APK, "error.apk_parse");//APK解析错误
		returnMap.put(ERROR_PKG, "error.pkg");//升级的APP包名与原包名不匹配
		returnMap.put(ERROR_CANNOTDEL, "error.cannot_del");//已被引用，不能删除
		returnMap.put(ERROR_PARAM_TYPE,"error.param_type");//参数类型错误

		
		returnMap.put(ERROR_UPLOAD_FILE_PLEASE, "CODE_0018");//请上传文件
		returnMap.put(ERROR_FILE_NULL, "error.upload_file_is_null");//上传文件为空
		returnMap.put(ERROR_INST_NULL, "error.institution_is_null");//机构为空,请先保存
		returnMap.put(ERROR_INFO_NULL, "error.advert_is_null");//广告为空,请先保存
		returnMap.put(ERROR_INFO_NO, "error.message_code");//信息编号错误
		returnMap.put(ERROR_LOGINNAME_EXSIT, "exist.loginname");//登录名已存在
		returnMap.put(ERROR_TEL_EXSIT, "exist.mobile");//手机号已存在
		returnMap.put(ERROR_INST_NODEL, "error.cannot_del");//已有子机构，不能删除
		returnMap.put(ERROR_HAVE_SON, "error.cannot_del");//已有子记录，不能删除
		returnMap.put(ERROR_HAVE_BRANCH, "error.have_branch");//已有分支，不可更改
		returnMap.put(ERROR_VERSION_CODE, "error.version_code");//版本号错误
		returnMap.put(ERROR_PARSE_LNGANDLAT, "error.parse_lngandlat");//解析经纬度错误
		
		returnMap.put(ERROR_STRATEGY_STRATEGYUPDATE, "error.strategy_strategyupdate");//策略中的策略更新设置错误
		returnMap.put(ERROR_STRATEGY_APPUPDATE, "error.strategy_appupdate");//策略中的应用更新设置错误
		
		returnMap.put(ERROR_VERSION_NOT_SAME, "error.version_not_same");//版本号不一致
		returnMap.put(ERROR_END_VERSION_GT_START_VERSION, "end_version_gt_start_version");//目标版本必须大于起始版本
		returnMap.put(ERROR_FILE_PATTERN, "error.file_pattern");//文件格式错误
		returnMap.put(ERROR_VERIFY_FAIL, "error.verify_fail");//检验失败
		returnMap.put(ERROR_TRANS_TYPE, "error.trans_type");//交易类型错误

		returnMap.put(ERROR_COUNTRY, "error.country");//国家错误
		returnMap.put(ERROR_PROVINCE, "error.province");//省份错误
		returnMap.put(ERROR_CITY, "error.city");//城市错误
		returnMap.put(ERROR_AREA, "error.area");//区域错误
		returnMap.put(ERROR_MER_CODE, "error.mer_code");//商户号错误
		returnMap.put(ERROR_CODE_EQUALS_PCODE, "error.code_equals_pcode");//商户号错误
		returnMap.put(ERROR_ROOT_INST, "error.error_root_inst");//根机构错误



		
		returnMap.put(ERROR_FILE_MAX, "error.file_too_large");//文件过大
		returnMap.put(ERROR_FILE_MAX, "error.pn");//pn错误
		returnMap.put(ERROR_DAY, "error.day");//文件过大
		returnMap.put(CODE_0034, "CODE_0034");//终端未初始化
		returnMap.put(CODE_9903, "CODE_9903");//请求参数错误

		returnMap.put(ERROR_PARSE_AREA, "error.pasre_area");//地区解析错误
		returnMap.put(ERROR_USER_NULL,"error.user_is_null");//用户为空htf
		returnMap.put(ERROR_APK_IS_INSTALL,"error.apk_is_install");//apk已被安装，无法执行此操作  htf
	}
		
	
	public static String toString(String type){
		return "{\"respcode\":\""+type+"\",\"respmsg\":\""+ Bundle.getString(returnMap.get(type))+"\"}";
	}
	
	public static String toString(String type,String msg){
		return "{\"respcode\":\""+type+"\",\"respmsg\":\""+ Bundle.getString(msg)+"\"}";
	}
	
	
	public class ParamNull{
		public static final String TEMPLATE_NAME = "error.template_name_is_null";//模板名称为空
		public static final String PARAM_NAME = "error.param_name_is_null";//参数名称为空
		public static final String CATEGORY_NAME = "error.category_name_is_null";//分类名称为空
		public static final String PTEMPLATE = "error.template_is_null";//模板为空
		public static final String APP_VERISON = "error.app_version_is_null";//应用版本为空
		public static final String UPLOAD_TYPE = "error.upload_type_is_null";//上传文件类型为空
		public static final String FILE = "error.upload_type_is_null";//文件为空
		public static final String FLOW = "error.flow_is_null";//审核流程为空
		public static final String APP_NAME = "error.app_name_is_null";//应用名称为空
		public static final String RES_NAME = "error.res_name_is_null";//应用名称为空

		public static final String TYPE = "error.app_class_is_null";//应用分类为空
		public static final String PAY_TYPE = "error.charge_type_is_null";//收费类型为空
		public static final String ISSUE_RANGE = "error.issue_type_is_null";//发布类型为空
		public static final String VERSION_CODE = "error.version_code_is_null";//版本号为空
		public static final String VERSION = "error.version_is_null";//版本为空
		public static final String SHA2 = "error.apk_is_null";//APK为空
		public static final String RESSHA2 = "error.res_is_null";//APK为空

		public static final String PKG = "error.package_name_is_null";//包名为空
		public static final String SUMMARY = "error.summary_is_null";//简介为空
		public static final String DESCRIPTION = "error.description_is_null";//介绍描述为空
		public static final String EXTEND_NAME = "error.extend_name_is_null";//附加名称为空
		public static final String BILL = "error.bill_is_null";//单据为空
		public static final String TERMINAL = "error.terminal_is_null";//终端为空
		public static final String ERR_TYPE = "error.err_type_is_null";//错误分类为空
		public static final String IGNORE_DAY = "error.ignore_day_is_null";//忽略天数为空
		public static final String STRATEGY_UPDATE_VALUE = "error.strategy_update_value_is_null";//策略更新时间为空
		public static final String APP_UPDATE_VALUE = "error.app_update_value_is_null";//应用更新时间为空
		public static final String SERVER_ADDRESS = "error.server_address_is_null";//服务器地址为空
		public static final String PORT = "error.port_day_is_null";//端口为空

		public static final String MER_SERINALNO = "error.mer_is_null";//商户编号为空
		public static final String MER_NAME = "error.mer_name_is_null";//商户名称为空
		public static final String COUNTRY = "error.country_is_null";//国家为空
		public static final String PROVINCE = "error.province_is_null";//省份为空
		public static final String CITY = "error.city_is_null";//城市为空
		public static final String AREA = "error.area_is_null";//区域为空
		public static final String TRANS_TYPE = "error.trans_type_is_null";//交易类型为空
		public static final String CONTACTNAME= "error.contactname_is_null";//联系人为空
		public static final String CONTACTPHONE= "error.contactphone_is_null";//联系电话为空
		public static final String INST ="error.inst_is_null";//机构为空
		public static final String INST_NO ="error.inst_no_is_null";//机构编号为空

	}
	
	public class Exist{
		public static final String APP_VERSION = "exist.app_version";//应用版本已存在
		public static final String MER = "exist.merchant";//商户已存在
		public static final String GROUP_TA = "exist.group";//分组已存在
		public static final String TAG_TA = "exist.tag";//标签已存在
		public static final String PN = "exist.pn";//pn号已存在
		public static final String FIRMWARE = "exist.firmware";//固件已存在
		public static final String STRATEGY_DEFAULT = "exsit.strategy_default";//默认策略已存在
		public static final String MER_SERINALNO = "CODE_0010";//商户编号重复
		public static final String AREA_NO = "exist.area_no";//区域编号重复
		public static final String FIRMWARE_NO = "exist.firmware_no";//固件编号重复

	}
	
	public class NoExist{
		public static final String APP_VERSION_SAVE = "noexist.app_version_is_null_save";//应用版本不存在，请先保存
		public static final String APP_VERSION = "CODE_0035";//应用版本不存在
		public static final String APP = "CODE_0024";//应用不存在
		public static final String TERMINAL = "CODE_0013";//终端不存在
		public static final String MER = "CODE_0011";//商户不存在
		public static final String GROUP_TA = "noexist.group";//分组不存在
		public static final String TAG_TA = "noexist.tag";//标签不存在
		public static final String INFORMATION = "noexist.information";//信息不存在
		public static final String FIRMWARE = "noexist.firmware";//固件不存在
		public static final String PARENT_MER_SERINALNO = "noexist.parent_mer_serinalno";//父商户编号不存在
		public static final String INST_NO = "noexist.inst_no";//机构编号不存在或不可用
	}
	
	public class Status{
		public static final String APP_VERSION_NOT_OFFLINE = "error.not_offline_app";//不是下线应用或黑名单应用
		public static final String APP_VERSION_NOT_BALCK = "error.not_balck_app";//不是黑名单应用
		public static final String NO_OPERATOR="error.status_cannot_operate";//该状态不能进行此操作
		
	}
	
	public static BizObject successData(){
		BizObject data =new BizObject();
		data.set("respcode", SUCCESS);
		data.set("respmsg", Bundle.getString(returnMap.get(SUCCESS)));
		return data;	
	}
	
	public static BizObject successData(BizObject data){
		data.set("respcode", SUCCESS);
		data.set("respmsg", Bundle.getString(returnMap.get(SUCCESS)));
		return data;	
	}
	public static BizObject noNewVersion(BizObject data){
		data.set("respcode", NO_NEW_VERSION);
		data.set("respmsg", Bundle.getString(returnMap.get(NO_NEW_VERSION)));
		return data;
	}
	
	public static BizObject errorData(String respcode){
		BizObject data =new BizObject();
		data.set("respcode", respcode);
		data.set("respmsg", Bundle.getString(returnMap.get(respcode)));
		return data;	
	}

	public static BizObject errorData(String respcode, BizObject data){
		data.set("respcode", respcode);
		data.set("respmsg", Bundle.getString(returnMap.get(respcode)));
		return data;
	}

	public static BizObject errorData(String respcode, String msgkey ){
		BizObject data =new BizObject();
		data.set("respcode", respcode);
		data.set("respmsg", Bundle.getString(msgkey));
		return data;	
	}
	
	public static BizObject errorData(String respcode, List<BizObject> list){
		BizObject data =new BizObject();
		data.set("respcode", respcode);
		data.set("respmsg", Bundle.getString(returnMap.get(respcode)));
		data.set("datas",list);
		return data;	
	}
	
	
	
}
