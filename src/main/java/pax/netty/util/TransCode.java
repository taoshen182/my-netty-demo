package pax.netty.util;

import org.apache.log4j.Logger;
import sand.actionhandler.open.OpenError;
import tool.dao.BizObject;


/**
 * 错误编码
 * <p>
 * 实现带有构造器的枚举
 *
 * @author pig
 */
public enum TransCode {
    // 通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
    // 赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错

    //POS端发送交易码
    PC_LOGIN("1000", "PC端登陆"),
    DATA_SYNC("1010", "脱机联机报到数据同步"),
    LOCAL_INIT("2001", "本地初始化"),
    REMOTE_KEY_DOWNLOAD("2002", "远程密钥下载"),
    CHANGE_MACHINE("2003", "换机交易"),
    ONLINE_REGISTER("2004", "联机注册交易"),
    ONLINE_AUTHZ_VERIFY("2005", "联机授权验证交易"),
    REMOTE_PROGRAM_DOWNLOAD("2011", "远程程序下载"),
    INIT_DOWNLOAD("2012", "初始化下载"),
    REMOTE_DOWNLOAD("2013", "远程下载"),

    DOWNLOAD("7001", "远程程序（参数）下载"),
    CONFIRM("7003", "确认通知"),
    INIT("7004", "初始化"),
    CHANGE("7005", "换机交易"),
    ISSUE_APPINFO("7006", "应用信息下发"),
    SCRIPT("7007", "脚本文件下载"),


    NM("2222", "Shenmgui"),

    //POS端接收交易码
    SUCCESS("00", "成功"),//成功
    ERROR_TRADE_TYPE("01", "无此交易类型"),//无此交易类型
    ERROR_SYSTEM("96", "系统故障"),//系统故障
    ERROR_FID("02", "无此厂商标识"),//无此厂商标识
    ERROR_TER_TYPE("03", "无此终端型号标识"),//无此终端型号标识
    ERROR_SN("04", "无此硬件序列号"),//无此硬件序列号
    ERROR_DOWNLOAD_TYPE("05", "无此下载类型"),//无此下载类型
    ERROR_APP_NAME("06", "应用名称错误"),//应用名称错误
    ERROR_AUTH("07", "无授权"),//无授权
    ERROR_TERMINAL("08", "无此逻辑终端/无匹配逻辑终端"),//无此逻辑终端/无匹配逻辑终端
    ERROR_NO_DOWNLOAD("10", "不用下载"),//不用下载
    ERROR_TER_STATUS("11", "终端状态异常"),//终端状态异常
    ERROR_KEY("12", "密钥不存在"),//密钥不存在
    ERROR_AUTH_FAIL("13", "双向认证失败"),//双向认证失败
    ERROR_INIT("14", "该终端已做初始化"),//该终端已做初始化
    ERROR_NO_STRATEGY("15", "策略未分配"),//策略未分配
    ERROR_VERSION("16", "未上传该版本程序"),//未上传该版本程序
    ERROR_PC_VERSION("17", "PC工具未下载该版本程序"),//PC工具未下载该版本程序
    ERROR_ACTIVATION_CODE("18", "激活码错误"),//激活码错误


    //dll-server 交易码

    DLL_SERVER_LOAD("00000", "加载动态库"),// 加载动态库	00000
    DLL_SERVER_DOWNLOAD("00001", "应用下载"),// 应用下载	00001
//    DLL_SERVER_TASKLIST("00002", "获取任务列表"),// 获取任务列表	00002
//    DLL_SERVER_DOWNLOAD("00003", "下载数据"), // 下载数据	00003
//    DLL_SERVER_END_DOWNLOAD("00004", "结束下载"),// 结束下载	00004


    ERROR_UNKNOW("0000", "未知码");
    //ERROR_TOKEN8003=许可令牌失效
    private final String msg;
    private final String code;

    static Logger logger = Logger.getLogger(OpenError.class);

    // 普通方法
    public static String getMsg(String code) {
        for (TransCode c : TransCode.values()) {
            if (c.getCode().equals(code)) {
                return c.msg;
            }
        }
        return null;
    }

    public static byte[] getCodeByte(TransCode transCode) {
        return transCode.code.getBytes();
    }

    // 普通方法
    public static TransCode getTransCode(String code) {
        for (TransCode c : TransCode.values()) {
            if (c.getCode().equals(code)) {
                return c;
            }
        }
        logger.info("error code " + code + " not defined !!!!!!");
        System.out.println("error code " + code + " not defined !!!!!!");
        return ERROR_UNKNOW;
    }


    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
    TransCode(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getJson() {
        return "{\"respcode\":\"" + code + "\",\"respmsg\":\"" + msg + "\"}";
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return this.getJson();
    }

    public BizObject toBiz() {
        BizObject b = new BizObject();
        try {
            b.set("respcode", code);
            b.set("respmsg", msg);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("", e);
            return new BizObject();

        }
    }

}