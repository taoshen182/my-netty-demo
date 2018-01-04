package pax.actionhandler.basic;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import pax.model.SessionVar;
import pax.service.ReturnCode;
import pax.util.Context;
import sand.actionhandler.basic.GlobalAH;
import sand.actionhandler.open.ServiceClient;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.basic.Global;
import sand.depot.business.system.Employee;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.SystemKit;
import sand.image.RandomGraphic;
import sand.mail.MailServer;
import tool.crypto.Crypto;
import tool.dao.BizObject;
import tool.dao.DBPool;
import tool.dao.QueryFactory;
import tool.dao.UidGenerator;
import tool.dao.mongodb.MongoDB;
import tool.encrypt.RSAEncrypt;
import tool.encrypt.TripleDES;
import tool.taglib.html.TokenTag;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

//import tool.uda.task.inter.imp.UTaskServiceImpl;

//import org.json.*;
//import net.sf.json.*;

/**
 * <p>
 * Title: 工具处理类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SAND
 * </p>
 *
 * @author liustone
 * @version 1.0
 */
@AccessControl("no")
// 不需要访问控制
public class LoginAH extends ActionHandler {

    public static String SSOServiceURL = "";
    public static String SSOLoginPage = "";

    public static String APP_MY = "0"; // 会员中心网站
    public static String APP_MOBILE = "1"; // 手机应用
    public static String APP_WZ = "2"; // 网络支付


    private static int _expirytime = 60 * 60 * 24 * 7;  // 默认的自动 登录  失效时间，一秒为单位， 现为1周

    /**
     * 构造方法
     *
     * @param req
     * @param res
     */

    public LoginAH(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
        this._objType = "MODULE"; // 随便写一个了，没有还不行
        this._moduleId = "basic"; // 本模块暂时未定
    }

    static Logger logger = Logger.getLogger(LoginAH.class);

    private static final String JEPASS = "erplawson";

    // 验证码图片的宽度。
    private int width = 80;

    // 验证码图片的高度。
    private int height = 30;

    // 验证码字符个数
    private int codeCount = 4;

    private int x = 0;

    //File f;
    // 字体高度
    private int fontHeight;

    private int codeY;

    char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    // 初始化
    protected void initial() {


        // 从web.xml中获取初始信息
        // 宽度
        String strWidth = this.getParameter("width");
        // 高度
        String strHeight = this.getParameter("height");
        // 字符个数
        String strCodeCount = this.getParameter("codeCount");

        // 将配置的信息转换成数值
        try {
            if (strWidth != null && strWidth.length() != 0) {
                width = Integer.parseInt(strWidth);
            }
            if (strHeight != null && strHeight.length() != 0) {
                height = Integer.parseInt(strHeight);
            }
            if (strCodeCount != null && strCodeCount.length() != 0) {
                codeCount = Integer.parseInt(strCodeCount);
            }
        } catch (NumberFormatException e) {
        }

        x = width / (codeCount + 1);
        fontHeight = height - 2;
        codeY = height - 4;
    }

    /**
     * 返回[from,to)之间的一个随机整数
     *
     * @param from 起始值
     * @param to   结束值
     * @return [from, to)之间的一个随机整数
     */
    protected int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    /**
     * 网络支付登陆跳转
     */
    public void wz_login() {
        String callback = this.getParameter("callbackurl");
        this.setAttribute("callback", callback);
        this._nextUrl = "/wzlogin.template";
    }

    /**
     * 网络支付登录
     *
     * @throws SQLException
     * @throws ServletException
     * @throws IOException
     */
    public void wzLogin() throws SQLException, ServletException, IOException {
        this.ssoLogin();
        this._nextUrl = "/basic.LoginAH.wzAutoLogin";
        // his.wzAutoLogin();
        // this.redirectTo("http://10.71.79.58:8080/PISMP/user/charge?token="+token+"&email="+email+"&sign="+SSoAH.calSign(m,_curuser.getId()));
    }

    @CandoCheck("session")
    public void wzAutoLogin() throws ServletException, IOException,
            SQLException {
    }

    @Ajax
    public String showSSoCenter() {
        // String sign = UidGenerator.getUUId() ;
        // Global.SSoCenter.put(sign, currentUser().getEmployee().getId()) ;
        // currentUser().setSSoToken(sign);
        // logger.info(Global.SSoCenter+"");
        return Global.SSoCenter.toString();
    }

    public void autoLoginTest2() throws ServletException, IOException {
    }

    public void ssoLoginTest() throws IOException, ServletException {
    }

    public void exitTest() throws IOException, ServletException {
    }

    public void authorizeTest() throws ServletException, IOException,
            SQLException {
    }

    /**
     * 生成检验图片
     *
     * @throws Exception
     */
    public void createVerifyImage() throws Exception {

        this._dispatched = true;

        // // 定义图像buffer
        // BufferedImage buffImg = new BufferedImage(width, height,
        // BufferedImage.TYPE_INT_RGB);
        // Graphics2D g = buffImg.createGraphics();
        //
        // // 创建一个随机数生成器类
        // Random random = new Random();
        //
        // // 将图像填充为白色
        // g.setColor(Color.WHITE);
        // g.fillRect(0, 0, width, height);
        //
        // // 创建字体，字体的大小应该根据图片的高度来定。
        // Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        // // 设置字体。
        // g.setFont(font);
        //
        // // 画边框。
        // g.setColor(Color.BLACK);
        // g.drawRect(0, 0, width - 1, height - 1);
        //
        // // 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
        // g.setColor(Color.BLACK);
        // for (int i = 0; i < 60; i++) {
        // int x = random.nextInt(width);
        // int y = random.nextInt(height);
        // int xl = random.nextInt(12);
        // int yl = random.nextInt(12);
        // g.drawLine(x, y, x + xl, y + yl);
        // }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        // StringBuffer randomCode = new StringBuffer();
        // int red = 0, green = 0, blue = 0;
        //
        // // 随机产生codeCount数字的验证码。
        // for (int i = 0; i < codeCount; i++) {
        // // 得到随机产生的验证码数字。
        // String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
        // // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
        // red = random.nextInt(255);
        // green = random.nextInt(255);
        // blue = random.nextInt(255);
        //
        // // 用随机产生的颜色将验证码绘制到图像中。
        // //g.setColor(new Color(red, green, blue));
        //
        // //int ypos = randomInt(height+fontHeight,height+fontHeight*2);
        // //g.drawString(strRand, (i + 1) * x, ypos);
        //
        // // 将产生的四个随机数组合在一起。
        // randomCode.append(strRand);
        // }

        RandomGraphic rg = RandomGraphic.createInstance(5);
        String charValue = rg.randAlpha();

        // 将四位数字的验证码保存到Session中。
        HttpSession session = _request.getSession();
        session.setAttribute("validateCode", charValue);

        // 禁止图像缓存。
        _response.setHeader("Pragma", "no-cache");
        _response.setHeader("Cache-Control", "no-cache");
        _response.setDateHeader("Expires", 0);

        _response.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = _response.getOutputStream();
        rg.drawAlpha(charValue, RandomGraphic.GRAPHIC_PNG, sos);
        // ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
    }

    @Ajax
    public String verifyCode() {
        String veryCode = "";
        if (this.getBizObjectFromMap("employee") != null)
            veryCode = this.getBizObjectFromMap("employee").getString(
                    "verifycode");

        return super.verifyCode(veryCode);
        // _response.setContentType("text/html;charset=utf-8");
    }

    public void active() throws SQLException {

        BizObject user = new BizObject("employee");
        user.set("activecode", this.getParameter("code"));
        List<BizObject> v = user.getQF().query(user);
        if (v.size() == 1) {
            user = v.get(0);
            if (user.getString("state").equals("-1")) {

                /**
                 * 如果是个人用户，无须审核
                 */
                if (user.getString("type").equals("0")) {
                    user.set("state", 1);
                }
                /**
                 * 如果是企业用户，未审核
                 */
                if (user.getString("type").equals("1")) {
                    user.set("state", 0);
                }
                this.getJdo().update(user);

                this._tipInfo = "你已成功激活uda账号";
                this._nextUrl = "/template/basic/msg.template";

            } else {
                throw new ControllableException("该用户已经激活");
            }
        } else {
            throw new ControllableException("无效的激活码");
        }

    }

    public void createEnterpriseUser() throws SQLException {
        this.createUser();
        // List<BizObject> v = GlobalAH.getTaskClass();
        // logger.info("显示数量为："+v.size());
        // _request.setAttribute("classes", v);

        this._nextUrl = "/user/reg_enterprise.template";
    }

    public void createUser() throws SQLException {
        List<BizObject> v = GlobalAH.getTaskClass();
        logger.info("显示数量为：" + v.size());
        _request.setAttribute("classes", v);

        this._nextUrl = "/user/reg.template";
    }

    /**
     * 注册
     *
     * @throws SQLException
     */
    public void register() throws SQLException {
        BizObject user = this.getBizObjectFromMap("employee");
        if (this.verifyCode().equals("1")) {
            BizObject u = new BizObject("employee");
            u.set("loginname", user.getString("loginname"));
            QueryFactory qf = u.getQF();
            // 设置过滤条件，非注销用户
            qf.setHardcoreFilter("scrap!=0 or scrap is null ");
            if (qf.query(u).size() > 0)
                throw new ControllableException("对不起,"
                        + user.getString("loginname") + "已经被人注册了！");
            u.clear();
            u.set("email", user.getString("email"));
            if (qf.query(u).size() > 0)
                throw new ControllableException("对不起,"
                        + user.getString("email") + "已经被人注册了！");

            user.set("state", "-1");// 未激活
            user.set("activecode",
                    TokenTag.generateToken(user.getString("loginname")));
            user.set("photoid", "ANNE14514412");

            user.set("UDAGOLD", 0);
            user.set("UDASCORE", 0);
            user.set("LOGINCOUNT", 0);
            user.set("TOTALTASK", 0);
            user.set("SUCCESSTASK", 0);
            user.set("GOODCOMMENT", 0);
            user.set("TOTALSUM", 0);
            user.set("FOOTSTEP", 0);
            user.set("TOTALPUBTASK", 0);
            user.set("UDAABILITY", 0);
            user.set("RANKING", 0);

            Employee employee = new Employee();
            employee.setEmployee(user);
            List<BizObject> roles = new ArrayList();
            BizObject role = new BizObject("role");

            /**
             * 如果是个人用户，无须审核
             */
            if (user.getString("type").equals("0")) {
                // user.set("state", 1);
                role.setID("person");
                roles.add(role);
            }
            /**
             * 如果是企业用户，未审核
             */
            if (user.getString("type").equals("1")) {

                role.setID("enterprise");
                roles.add(role);
            }

            employee.setRoles(roles);
            employee.save();

            List<BizObject> v = this.getBizObjectWithType("concern");
            logger.info("v size is " + v.size() + " user id is " + user.getId());
            if (v != null) {
                // 删除所有擅长
                this.getJdo().resetObjType("concern");
                int i = this.getJdo().delete("userid", user.getId());
                logger.info("delete " + i);
            }

            String concernstr = "";
            logger.info("v size is " + v.size() + " user id is " + user.getId());

            for (BizObject concern : v) {
                concern.set("userid", user.getId());
                concernstr = concernstr + concern.getString("taskclassid")
                        + ",";
                this.getJdo().addOrUpdate(concern);
            }
            user.set("concern", concernstr);
            this.getJdo().update(user);

            MailServer.sendMail(user, null, "完成  注册", "请完成注册",
                    "<a href='http://116.236.224.52:7989/basic.LoginAH.active?code="
                            + user.getString("activecode") + "'>激活账号</a>");// .sendMail(user,
            // "", "完成uda网注册",
            // "");
            logger.info("active code is " + user.getString("activecode"));
            // this.getJdo().add(user);

        } else
            throw new ControllableException("您的验证码不正确");

        String mail = user.getString("email");
        String mailaddress = "http://mail."
                + mail.substring(mail.indexOf('@') + 1);
        this._tipInfo = "注册确认信已经发往您的邮箱，请点击<a href='" + mailaddress
                + "'> 这里  </a>查收";
        this._nextUrl = "/template/basic/msg.template?showtime=3";
    }

    //public void showAdminCenter() throws SQLException {}

    /**
     * 丢失密码
     *
     * @throws SQLException
     */
    public void lostPasswd() throws SQLException {
        BizObject user = this.getBizObjectFromMap("employee");
        if (this.verifyCode().equals("1")) {
            List a = user.getQF().query(user);
            if (a.size() > 0) {
                user = (BizObject) user.getQF().query(user).get(0);
                // MailServer.sendMail(user, cc, subject, message, attaches);
                if (user != null) {
                    Crypto crypto = new Crypto();
                    String ln = user.getString("loginname");
                    // System.out.println("ln    "+ln+" l "+ln.length()+"  "+user.getString("EMAIL"));
                    String b = ln.substring(0, 1);
                    String e = ln.substring(ln.length() - 1, ln.length());
                    String t = "*";
                    for (int i = 0; i < ln.length() - 2; i++)
                        t += "*";

                    MailServer.sendMail(
                            user,
                            "",
                            "plm密码提醒",
                            "您的登录账号是 " + b + t + e + "\n密码是 "
                                    + crypto.deDes(user.getString("Password"))
                                    + "\n请注意保管好您的密码");
                    this._tipInfo = "您的密码已经发送到你的信箱里去了";

                } else
                    this._tipInfo = "对不起，您的账户没有找到";
            } else
                this._tipInfo = "对不起，您的账户没有找到";
            this._nextUrl = "/template/basic/msg.template";
            // int i = loginImpl(user);
        }

    }

    public void index() {
        this._nextUrl = "/demo_index.html";
    }

    /**
     * 登陆
     *
     * @throws SQLException
     * @throws IOException
     * @ajax
     */
    // @Ajax
    public void login() throws SQLException, IOException {

        _nextUrl = "/basic.UserActionHandler.showUserCenter";
        _nextUrl = "/basic.HomeAH.index";
        BizObject user = this.getBizObjectFromMap("employee");
        String i = loginImpl(user);
        /*
         * if (user != null) { if (this.verifyCode().equals("1")) {
		 *
		 * int i = loginImpl(user); } else throw new
		 * ControllableException("验证码不正确~~");
		 *
		 * }
		 */

    }

    /**
     * 根据登陆名查找用户 ，登录名可能为email , telno ,loginname
     *
     * @param loginname
     * @return
     * @throws SQLException
     */
    public static BizObject getUserByLoginName(String loginname)
            throws SQLException {

        BizObject bizObject = new BizObject("EMPLOYEE");
        // logger.info("in employee validate 2");
        bizObject.clear();
        bizObject.set("loginname", loginname);
        QueryFactory queryFactory = new QueryFactory(bizObject);
        // logger.info("in employee validate 3");
        queryFactory.setHardcoreFilter(" status=0 ");
        List<BizObject> queryResult = queryFactory.query(bizObject);

        // logger.debug("login sql is "+queryFactory.getSql());

        String logincol = "loginname";
        if (queryResult.size() == 0) {
            bizObject.clear();
            bizObject.set("telno", loginname);
            queryResult = queryFactory.query(bizObject);
            logincol = "telno";
        }
        if (queryResult.size() == 0) {
            bizObject.clear();
            bizObject.set("email", loginname);
            queryResult = queryFactory.query(bizObject);
            logincol = "email";
        }
        if (queryResult.size() == 0) {
            bizObject.clear();
            bizObject.set("identifier", loginname);
            queryResult = queryFactory.query(bizObject);
            logincol = "identifier";
        }
        logger.info("login sql is " + queryFactory.getSql());
        logger.info(logincol + "  " + loginname + "  " + queryResult.size());

        if (queryResult.size() == 0) {
            bizObject.clear();
            bizObject.set("username", loginname);
            queryResult = queryFactory.query(bizObject);
            logincol = "username";
        }
        //File f;

        logger.info("login sql is " + queryFactory.getSql());
        logger.info(logincol + "  " + loginname + "  " + queryResult.size());

        if (queryResult.size() > 0) {
            BizObject biz_User = queryResult.get(0);
            logger.info("status is  " + biz_User.getString("status"));
            return biz_User;
        } else
            return null;
        // 如果是离职员工，返回false
        // if(biz_User.getString("scrap").equals("0"))
        // return FIRED; //如果是审核中账号
        // if(biz_User.getString("state").equals("-1"))
        // return APPLY;

    }

    // static private ConcurrentMap SSOIDs ;
    // public static String CookieName="WangYuDesktopSSOID";
    // String cookiename="WangYuDesktopSSOID";
    String domainname = "sand";

    public void ssoEntrance() throws SQLException {

        _nextUrl = "/";
        if (!this.getParameter("action").equals("")) {
            _nextUrl = this.getParameter("action");
        }
        if (this._curuser != null)
            return;


        String result = "nobody";

        // 检查认证结果
        System.out.println("result is " + result);
        if (result.equals("nobody")) { // 效验失败或没有找到cookie，则需要登录
            throw new ControllableException("对不起，单点登录失败");

        } else {// 效验成功
            // request.setAttribute("SSOUser",result);
            BizObject user = new BizObject("employee");
            user.set("loginname", result);
            user = user.getQF().getOne(user);
            // this.writeSession(req, user, jdo)
            result = writeSession(_request, user);
            // ssoresponse.setCookieid(cookieid);
            // 写入session,转为已登录状态
            // this.loginImpl(user);

        }

    }


    @Ajax
    public String testSet() {
        SessionVar sv = this.getVar();
        sv.setIp(this.getParameter("ip"));//.setId(900);
        //this._request.setAttribute("var", sv);
        return sv.getIp() + "  " + sv.getToken();
    }

    @Ajax
    public String testGet() {

        SessionVar sv = this.getVar();
        String s = sv.getIp() + "  " + sv.getToken();
        logger.info("get " + s);
        return s;
        //sv.setId(900);
        //this._request.setAttribute("var", sv);

    }

    private SessionVar getVar() {
        logger.info("session id " + _request.getSession().getId());
        Object obj = _request.getSession().getAttribute(Context.VAR);
        if (obj == null) {
            logger.info("create session var ");
            obj = new pax.model.SessionVar();
            _request.getSession().setAttribute(Context.VAR, obj);
        }

        return (SessionVar) obj;
    }

    private void saveVar(SessionVar sv) {
        _request.getSession().setAttribute(Context.VAR, sv);
    }

    /**
     * 后台管理
     **/
    @CandoCheck("session")
    public void admin() {
        if (_curuser.getString("isadmin").equals("1")
                || _curuser.getString("loginname").equals("system")) {
            this._nextUrl = "/template/basic/admin/index.template";

        } else
            throw new ControllableException("对不起，您没有管理员权限");

    }

    /**
     * sso 跳转
     */
    public void ssoRedirect() {
    }

    /**
     * 第三方自动登录
     *
     * @throws SQLException
     */


//  public  void ssoLogin2() throws SQLException{
//	  if(this.autoLogin()){
//		  return;
//	  }
//	  else{
//		  this.ssoLogin();
//	  }
//  }
    // public String _username;
    public String _password;

    public void ssoLogin() throws SQLException {

        logger.info("hover index is " + this.getParameter("hoverindex"));
        if (!this.getParameter("hoverindex").equals(""))
            this.setAttribute("hoverindex", this.getParameter("hoverindex"));
        else
            this.setAttribute("hoverindex", "1");

        if (!this.getParameter("loginUrl").equals("")) {
            // ActionHandler.s
            _nextUrl = this.getParameter("loginUrl");
        } else
            _nextUrl = "/login.jsp";


        Cookie cookie = new Cookie("JSESSIONID", _request.getSession().getId());
        _response.addCookie(cookie);

        // this.PAGE_SCHEMA="hna";
        this.deCrypt();

        BizObject user = this.getBizObjectFromMap("employee");
        String gotoUrl = this.getParameter("gotoUrl");
        // logger.info("goto url is "+this.getParameter("gotoUrl"));

        if (user != null) {
            if (gotoUrl.equals("")) {
                gotoUrl = user.getString("gotourl");
            } else
                user.set("gotourl", gotoUrl);

            this.setAttribute("user", user);

        } else
            return;

        String code = SystemKit.getCacheParamById("system_core", "code");
        if (!code.equals("0") && !this.verifyCode().equals("ok")) {
            // throw new LoginException("验证码不正确~~");
            _tipInfo = "验证码不正确~~";
            return;
        }


        logger.info("next url is " + _nextUrl);

        user.set("loginname", this.getParameter("username"));
        user.set("password", _password);

        user.set("appid", APP_MY);
        String ip = getRemoteIP();
        user.set("login_ip", ip);
        // this.setLoginInfo(user, APP_MY);
        // System.out.println(ipSeeker.getAddress("192.168.199.1"));
        String result = loginImpl(user);

        // logger.info("user  is "+user);

        logger.info("~~~~~~~~~~~~~~~~~~~~~ result is " + result);

        // user.set("cookieid", sign);
        if (!result.equals(Employee.VALID)) {
            _tipInfo = result;
            // _nextUrl="/suecard.template";
            return;
        } else
            _nextUrl = "/basic.HomeAH.index";
        // user.refresh();
        logger.info("next url is " + _nextUrl);
        // String gotoURL = _request.getParameter("gotoUrl");

        // Map<String,String> idMap = new HashMap();
        // idMap.put("cookieid",newID);

        // String secret =new SampleAppSecretManager().getSecret("00001");
        // String sign=RopUtils.sign(idMap, secret);
        String url = this.getParameter("gotoUrl");

        if (url.equals("")) { // 是给自己用的

            logger.info("loginname length ......................................................................... "
                    + this.getCurUser().getString("loginname").length());

            if (this.getCurUser().getString("logincolumn").toLowerCase()
                    .equals("identifier")) { // 身份证做登录名的要去完善用户名
                _nextUrl = "/basic.UserCenter.showOne?from=login";
            }
            // if(this.getCurUser().getString("loginname").length()==18){
            //
            // }

        } else if (!url.equals("")) {
        }

    }

    /**
     * 读取客户编号
     *
     * @return
     */
    private static String parseClientInfo() {
        String cinfo = readCookie("client_info");
        if (cinfo.equals("")) {
            String clientid = UidGenerator.getUUId();// 客户编号
            writeCookie("client_info", clientid, 60 * 60 * 24 * 365);
            return clientid;
        } else
            return cinfo;
    }

    /**
     * 写客户信息
     */
    private static void writeCookie(String cookiename, String info, int expirytime) {


        // 写cookie
        Cookie namecookie = new Cookie(cookiename, info);
        // 生命周期
        namecookie.setMaxAge(expirytime);
        // 设置哪个域名写cookie
        // namecookie.setDomain("my.bhecard.com");

        ActionHandler.currentActionHandler()._response.addCookie(namecookie);
        logger.info("write cookie " + namecookie + ":" + info);
    }

    /**
     * 取客户信息 cookie
     *
     * @param cookiename
     */
    private static String readCookie(String cookiename) {

        String name = "";

        // 读cookie
        Cookie[] cookies = ActionHandler.currentRequest().getCookies();
        if (cookies != null) {

            for (int i = 0; i < cookies.length; i++) {

                Cookie c = cookies[i];
                // logger.info(" cookie  "+i+"  "+c.getName()+"  :  "+c.getValue());
                if (c.getName().equalsIgnoreCase(cookiename)) {
                    name = c.getValue();
                }

            }
        }
        return name;

    }

    // private void setLoginInfo(BizObject user,String appid){
    //
    // }
    @Ajax
    public String ajaxLogin2() throws SQLException, IOException {

        //this.deCrypt();
        String code = SystemKit.getCacheParamById("system_core", "code");

//		if (!code.equals("0") && !this.verifyCode().equals("ok")) {
//			// throw new LoginException("验证码不正确~~");
//			_tipInfo = "验证码不正确~~";
//			// return Employee.ERROR_VALIDCODE;
//			return "[{respCode:'0001', respMsg:'验证码不正确'}]";
//		}
        _nextUrl = "/basic.UserActionHandler.showUserCenter";
        BizObject user = new BizObject("employee");
        // BizObject user = new BizObject("employee");
        user.set("loginname", this.getParameter("username"));
        user.set("password", this.getParameter("password"));

        user.set("appid", APP_MY);
        String ip = getRemoteIP();
        user.set("login_ip", ip);
        String i = loginImpl(user);
        logger.info("return login " + i);
        return i + "";

//		if (i.equals("1")) {
//			return "1";
//		} else {
//			return "[{respCode:'0001', respMsg:'用户名及密码错误！'}]";
//		}

    }

    /**
     * 记录登陆日志
     *
     * @param user
     * @throws SQLException
     */
    public static void loglogin(BizObject user) throws SQLException {

        BizObject loginlog = new BizObject("loginlog");
        // logger.info(loginlog.is)
        loginlog.set("userid", user.getId());
        loginlog.set("username", user.getString("showname"));
        // String ip= user.getString("login_ip");
        //String ip = ActionHandler.currentActionHandler().getRemoteIP();
        //user.set("login_ip", ip);

        loginlog.set("login_ip", user.getString("login_ip"));
        loginlog.set("appid", user.getString("appid"));
        String clientid = parseClientInfo();
        loginlog.set("clientid", clientid);
        loginlog.set("login_time", new Date());
        user.set("clientid", clientid);
        loginlog.set("refer",
                ActionHandler.currentRequest().getHeader("refer"));

        loginlog.set("agent",
                ActionHandler.currentRequest().getHeader("user-agent"));

        if (DBPool.getInstance().isMongoReady()) {

            //if (_mongodb == null)
            MongoDB mongodb = DBPool.getMongoDB("paxlog_login");
            mongodb.insert(loginlog);
            logger.info("insert to erplog " + loginlog
                    + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + mongodb);

        } else {
            logger.info("mongo not ready ");
        }

//
//		if (SingletonStructure.getInstance().existTable("loginlog")) {
//
//
//			ActionHandler.currentSession().add(loginlog);
//
//
//		}
    }

    /**
     * 写入自动登录信息
     *
     * @param emp
     * @throws SQLException
     * @throws Exception
     */

    private void writeAutoLogin(BizObject emp) throws SQLException {

        //String clientid=parseClientInfo();
        logger.info("autologin parameter : " + this.getParameter("autologin"));
        //如果不是自动登录，那么退出方法
        if (!this.getParameter("autologin").equals("1"))
            return;
        String uuid = UidGenerator.getUUId();
        long expirytime = System.currentTimeMillis() + 1000 * _expirytime; //一周的有效期

        emp.set("clientid", this.parseClientInfo());
        emp.set("autologin", uuid);
        emp.set("expirytime", expirytime);

        this.getJdo().update(emp);
        String atl_token = emp.getId() + "|" + uuid + "|" + emp.getString("clientid");
        //Crypto.des
        String key = SystemKit.getParamById("system_core", "autologin_key");
        if (key == null) throw new ControllableException("请设置自动登录的秘钥");
        String c;
        try {
            c = TripleDES.encryptThreeDESECB(atl_token, key);
            this.writeCookie("atl_token", c, _expirytime);
        } catch (Exception e) {
            logger.error("error", e);
            e.printStackTrace();
        }


        //System.out.print(this);
    }


    /**
     * 自动登录
     *
     * @throws SQLException
     * @throws Exception
     */
    public boolean cookieLogin() throws SQLException {
        String desalt = readCookie("atl_token");

        logger.info("des alt " + desalt);
        String key = SystemKit.getParamById("system_core", "autologin_key");
        if (key == null) throw new ControllableException("请设置自动登录的秘钥");
        //String c = DES.encrypto(atl_token, key);

        if (desalt.equals("")) {
            return false;
        }
        String alt;
        try {
            alt = TripleDES.decryptThreeDESECB(desalt, key);
            logger.info("alt is " + alt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error", e);
            return false;
        }

        if (alt.equals("")) {
            return false;
        }


        String uids[] = alt.split("\\|");
        logger.info("alt size is " + uids.length);
        if (uids.length != 3) {
            LoginAH.writeCookie("atl_token", "", _expirytime);
            return false;
        }


        String userid = uids[0];
        BizObject emp = new BizObject("employee");
        emp.setID(userid);
        emp.refresh();

        long t = (emp.getLongValue("expirytime") - System.currentTimeMillis()) / 1000;
        logger.info("离自动登录失效还剩下  " + t + "秒钟");
        if (emp.getString("autologin").equals(uids[1]) && emp.getString("clientid").equals(uids[2]) && System.currentTimeMillis() < emp.getLongValue("expirytime")) {

            LoginAH.writeSession(_request, emp);
            return true;


        } else {
            LoginAH.writeCookie("atl_token", "", _expirytime);
            return false;
        }

        //	return false;
        //}

    }

    /**
     * 这里的user2参数是作为出参用的
     *
     * @param user2
     * @return
     * @throws SQLException
     */
    public String loginImpl(BizObject user2) throws SQLException {

        // logger.info("url is 1");
        // System.out.println("in Login");

        String result = "0";

        QueryFactory userQuery = new QueryFactory("employee");
        // BizObject user = new BizObject();
        // // logger.info("url is 2");
        // user.set("loginname", userId);
        // user.set("Password", password);
        String url = null;
        String msg = null;
        // System.out.println("loginname " + userId);
        // System.out.println("password " + password);

        // _request.setAttribute("domainuser", domainuser);

        BizObject user = user2;
        result = Employee.isValidUserDes(user);
        if (result.equals(Employee.VALID)) {
            // if (Employee.isValidUser(user)) {
            // _nextUrl = "/basic.UserActionHandler.showUserCenter?userid="
            // + user.getId();
            // this.
            // String cookieid=user.getString("cookieid");
            // user = (BizObject) userQuery.query("loginname",
            // user.getString("loginname")).get(0);
            // user.set("cookieid", cookieid);
            // Employee.getRank(user);
            // user2.setID(user.getId());
            // user2.du
            // logger.info("user footstep is "+user.getString("footstep"));
            String ip = ActionHandler.currentActionHandler().getRemoteIP();
            user.set("login_ip", ip);

            result = writeSession(_request, user);
            loglogin(user);
            this.writeAutoLogin(user);

        } else {
            logger.info("result is " + result);
            if (result.equals(Employee.ERROR_APPLY)) {
                _tipInfo = "请点击发送到您邮箱的地址来激活帐号";
            } else if (result.equals(Employee.ERROR_FIRED)) {
                _tipInfo = "该用户已经注销";
            } else
                _tipInfo = result;
            // 用户名或密码错误，应提示重新登录
            // _nextUrl = "/ssologin.template";

            // logger.info(_tipInfo);
            // System.out.println(msg);
            // logger.info("url is " + url);
            // result = Employee.ERROR_NOVALID;
        }
        _request.setAttribute("errMsg", msg);
        return result;
        // _request.getRequestDispatcher(url).forward(_request, _response);

    }

    private static HttpSession getNewSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        //System.out.println(session.getId());
        if (session != null) {
            session.invalidate();
        }
        HttpSession session1 = req.getSession(true);
        return session1;

    }

    public static String writeSession(HttpServletRequest req, BizObject user) throws SQLException {
        logger.info("curuser is " + user);
        if (user == null) {
            return Employee.ERROR_NOBUND;// 未绑定erp
        } else if (user.getString("scrap").equals("0")) {
            return Employee.ERROR_FIRED;// 该用户已经离职
        } else {


            if (!user.getString("username").equals(""))
                user.set("showname", user.getString("username"));
            else
                user.set("showname",
                        user.getString(user.getString("logincolumn")));
            user.setNoEditable();
            logger.info("curuser editable is " + user.isEditable());
            logger.info("curuser is " + user);
            Employee employee = new Employee(user);
            String[] functions = user.getString("functions").split("\\|");
            List<String> v = new ArrayList();
            for (String func : functions) {
                v.add(func);
            }
            logger.info("functions size is " + v.size());
            employee.setFunctions(v);

//			if (Global.onlineMap.size() >= Global.maxonline) {
//				logger.info("Global.onlineMap is " + Global.onlineMap);
//				return Employee.ERROR_MAX + Global.maxonline;
//			}

            HttpSession session = req.getSession();//getNewSession(req);
            session.setAttribute("employee", employee);

            //Global.onlineMap.put(user.getId(), user);

            session.setAttribute("curuser", user);
            logger.info("curuser is " + user);
            //Global.sessionMap.put(user.getId(), req.getSession());

            return Employee.VALID;
        }

        // return Employee.UNKNOW;
    }

    // 查找用下的网络支付账户
    public BizObject getAccount(String userid) throws SQLException {
        BizObject eacc = new BizObject("EASYPAY_ACCOUNT");
        eacc.set("memberid", userid);
        return QueryFactory.getInstance("EASYPAY_ACCOUNT").getOne(eacc);
    }

    public void p_wz_exit() throws SQLException {

        if (_curuser != null) {

            BizObject acc = this.getAccount(this._curuser.getId());
            if (acc == null)
                return;
            String email = acc.getString("EMAIL");
            Map map1 = new HashMap();
            map1.put("email", email);


        }

    }

    public void wz_exit() throws SQLException, IOException, ServletException {
        this.exit();
        String url = SystemKit
                .getCacheParamById("account", "account_home_page");
        this.redirectTo(url);

    }

    /**
     * 退出重新登陆
     *
     * @throws SQLException
     */
    public void exit() throws SQLException {

        if (_curuser != null) {
            logger.info("_curuser.iseditable " + _curuser.isEditable());

            Global.exit(_curuser.getId(), "");
            // Global.onlineMap.remove(_curuser.getId());
            // Global.SSoCenter.remove(_curuser.getId());
            System.out.println(_curuser.getString("username") + "已经退出！");

        }

        _request.getSession().removeAttribute("user");
        _request.getSession().removeAttribute("employee");
        _request.getSession().removeAttribute("module");

        // 当前用户(已转换成BizObject对象,以供内部资源网页面使用)

        _request.getSession().removeAttribute("curuser");
        _request.getSession().removeAttribute("userInfo");

        _request.getSession().invalidate(); // 彻底销毁session


        this._dispatchType = this.DISPATCH_TYPE_REDIRECT;
        String contextPath = _request.getContextPath();
        _nextUrl = contextPath;

    }

    /**
     * 退出重新登陆
     */
    public void center() {
        this._nextUrl = "/template/basic/center.template";
    }

    // 验证用户邮箱并添加或者修改用户邮箱
    public void activemail() throws SQLException {

        BizObject validatecode = new BizObject("validatecode");
        validatecode.set("activecode", this.getParameter("activecode"));
        List<BizObject> v = QueryFactory.getInstance("validatecode").query(
                validatecode);
        String msg = "";

        String server2 = SystemKit.getParamById("server2", "serverurl2");// 国民旅游网登录地址
        String server = SystemKit.getParamById("system_core", "www_url");// 服务器地址
        if (StringUtils.isBlank(server)) {
            msg = msg + "服务器地址未设置\n";
        } else {

            if (v.size() == 1) {
                BizObject user = v.get(0).getBizObj("userid");
                if (!user.getString("cacheemail").equals("")) {
                    user.set("email", user.getString("cacheemail"));// 更新邮箱
                    user.set("cacheemail", "");// 删除激活码
                    this.getJdo().update(user);
                    msg = "您已成功更新邮箱";
                }
                this.setAttribute("loginname", user.getString("loginname"));
                this.setAttribute("flag", 1);

            } else {
                msg = "无效的激活码";
            }

        }

        this.setAttribute("msg", msg);

        this._request.setAttribute("server", server);
        this._request.setAttribute("server2", server2);
        this._nextUrl = "/template/real/updateEmail.template";

    }

    public void deCrypt() {

        // String local_network = null;
        // String local_disk =null;
        // String local_nic =null;
        // String network=null;
        // String disk=null;
        // String nic=null;
        // String backpage=null;

        String mcrypt_key_1 = (String) _request.getSession(false).getAttribute(
                "mcrypt_key");
        String username1 = this.getParameter("username").trim();
        String password1 = this.getParameter("password").trim();

        // _request.set
        // backpage=_request.getParameter("backpage");
        // local_network =
        // _request.getParameter("local_network").trim();//加密后的客户端网卡和MAC信息;
        // local_disk =
        // _request.getParameter("local_disk").trim();//获取加密后的客户端硬盘序列号;
        // local_nic =
        // _request.getParameter("local_nic").trim();//获取加密后的客户端cpuid号;

        // if(mcrypt_key_1 ==null || mcrypt_key_1.equals("") ||
        // username1.equals("") || username1 ==null || password1.equals("") ||
        // password1 ==null )
        // {
        // throw new LoginException("用户名或密码不可以为空，请重新登录!");
        // //System.out.print("<script>alert('用户名或密码不可以为空，请重新登录!');window.location.href='./login.template';</script>");
        // // return;
        // }

        if (password1.equals("") || password1.equals("undefined")) {
            _password = this.getParameter("password2");
        } else {
            // logger.info("password 1 is   "+password1);
            //	_password = AESWithJCE.getResult(mcrypt_key_1, password1);// 调用解密接口。mcrypt_key_1为获取的32位随机数，password1为密码的密文；
        }

        // logger.info("password is "+_password);

        // _username=AESWithJCE.getResult(mcrypt_key_1,username1);//调用解密接口。mcrypt_key_1为获取的32位随机数，username1为用户名密文；

        // if(local_network!=null)
        // network=AESWithJCE.getResult(mcrypt_key_1,local_network);//调用解密接口.获取网卡信息;
        // if(local_disk!=null)
        // disk=AESWithJCE.getResult(mcrypt_key_1,local_disk);//调用解密接口.获取硬盘序列号信息;
        // if(local_nic!=null)
        // nic=AESWithJCE.getResult(mcrypt_key_1,local_nic);//调用解密接口.获取cpuid号信息;
        //
        // this._request.getSession(false).invalidate();//清除session;

    }

    public void version() {
        BizObject b = GlobalAH.status();
        this.setAttribute("obj", b);
        this._nextUrl = "/template/basic/version.template";
    }

    @Ajax
    public String ajaxLogin() throws SQLException, IOException {

        //this.deCrypt();
        String code = SystemKit.getCacheParamById("system_core", "need_verifycode");

        if (code != null && !code.equals("0") && !this.verifyCode().equals("ok")) {
            // throw new LoginException("验证码不正确~~");
            _tipInfo = "验证码不正确~~";
            // return Employee.ERROR_VALIDCODE;
            return "{\"respCode\":\"0002\", \"respMsg\":\"验证码不正确\"}";
        }

        _nextUrl = "/basic.UserActionHandler.showUserCenter";
        BizObject user = new BizObject("employee");
        // BizObject user = new BizObject("employee");
        user.set("loginname", this.getParameter("employee$loginname"));
        user.set("password", this.getParameter("employee$password"));

        user.set("appid", APP_MY);
        String ip = getRemoteIP();
        user.set("login_ip", ip);
        String i = loginImpl(user);
        logger.info("return login " + i);
        //return i+"";

        if (i.equals("1")) {
            return "{\"respCode\":\"0000\", \"respMsg\":\"正常！\"}";
        } else {
            return "{\"respCode\":\"0001\", \"respMsg\":\"用户名或密码错误！\"}";
        }

    }

    public void connect() throws SQLException {
        //	try {
        //_request.setAttribute("loc", _request.getLocale());
        //_request.getLocale().

        _nextUrl = "/login.jsp";
        /**
         * 如果是 域用户，并且存在erp帐号
         */
        if (_request.getSession(false) != null && _request.getSession(false).getAttribute("curuser") != null) {
            _nextUrl = "/basic.HomeAH.index";
            logger.info("port is " + _request.getServerPort());
            return;

        }

        //如果设置了自动登录
//				if(cookieLogin()){
//					_nextUrl="/basic.HomeAH.index";
//					return;
//				}

        //如果未登录
        _request.getSession().invalidate(); // 彻底销毁sessionsession.invalidate();


    }


    /**
     * 修改密码
     *
     * @throws ServletException
     * @throws IOException
     */

    public void modifyPwd() throws ServletException, SQLException {
        this.pushLocation("密码修改", "/basic.UserActionHandler.modifyPwd");
        BizObject userBiz = this.getBizObjectFromMap("EMPLOYEE");

        String method = "open.PlatformService.modifyPassword";
        Map m = new HashMap();
        logger.info("chinese is " + this.getParameter("chinese"));
        //m.put("tokne", token);
        m.put("userid", this.getParameter("userid"));
        m.put("password", this.getParameter("password"));
        m.put("newpassword", this.getParameter("newpassword"));


        BizObject emp = ServiceClient.getInstance().execute(method, m);
        logger.info("emp is " + emp);
        if (emp.getString("respcode").equals("0000")) {
            //emp.setIDColumn("userid");
            logger.info("emp is " + emp);
            this._tipInfo = "密码修改成功！";
            this._nextUrl = "/template/basic/msg.template";
        } else
            throw new ControllableException("错误！" + emp.getString("respmsg"));
    }

    /**
     * 查看连接数
     *
     * @throws ServletException
     * @throws SQLException
     */

    public void viewSnapConn() throws ServletException, SQLException {
        //int i = DBPool..getOpensession();
        this._request.setAttribute("maxopen", DBPool.getInstance().getMaxopen());
        this._request.setAttribute("Opensession", DBPool.getInstance().getMaxopen());
        this._request.setAttribute("cons", DBPool.getInstance().getSnapConlist());
        this._request.setAttribute("activenum", DBPool.getInstance()
                .getNumActive());
        this._request
                .setAttribute("idlenum", DBPool.getInstance().getNumIdle());
        this._request.setAttribute("evictionnum", DBPool.getInstance()
                .getNumTestsPerEvictionRun());
        this._nextUrl = "/template/basic/opensessionView.template";
    }

    /**
     * 查看连接数
     *
     * @throws ServletException
     * @throws SQLException
     */

    public void viewClick() throws ServletException, SQLException {

        //int i = DBPool..getOpensession();
        //Global.clickMap.s
        //Collections.sort(list);
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(Global.clickMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

//		List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(Global.clickMap.entrySet());
//        Collections.sort(entryList, new MapValueComparator());
        logger.info("map " + Global.useTimeMap);
        logger.info("map " + Global.clickMap);
        List v = new ArrayList();
        for (Map.Entry<String, Integer> m : entryList) {
            BizObject b = new BizObject();
            b.set("method", m.getKey());
            b.set("clicks", m.getValue());
            if (Global.useTimeMap.containsKey(m.getKey()))
                b.set("usetime_aver", Global.useTimeMap.get(m.getKey()) / m.getValue());
            else
                b.set("usetime_aver", 0);
            v.add(b);//)
        }
        this._request.setAttribute("list", v);
        this._request.setAttribute("clicks", Global.clicks);
        this._nextUrl = "/template/basic/clickView.template";

    }

    //比较器类
    public class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {
//	    public int compare(Entry<String, String> me1, Entry<String, String> me2) {
//	        return me1.getValue().compareTo(me2.getValue());
//	    }

        @Override
        public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }

    }

    public void insertTerminal() throws Exception {
        RSAEncrypt rsaept = new RSAEncrypt();
        rsaept.genKeyPair();
        BizObject obj = new BizObject("terminal");
        obj.set("code", 123456);
        obj = obj.getQF().getOne(obj);

        obj.set("server_pri_key", rsaept.getPrivateKeyString());
        obj.set("server_pub_key", rsaept.getPublicKeyString());
        rsaept.genKeyPair();
        obj.set("client_pri_key", rsaept.getPrivateKeyString());
        obj.set("client_pub_key", rsaept.getPublicKeyString());
        //BizObject terminal = new BizObject("terminal");
        //terminal.set("", o);
        this.getJdo().update(obj);
    }

    @Ajax
    public String app_store_test() {
        return "test success....  AppAH store";
    }

    @Ajax
    public String test() {

        HttpServletRequest request = this._request;
        System.out.println("request = " + request);

        String contentType = request.getContentType();
        System.out.println("contentType = " + contentType);

        System.out.println("_________________");
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            Object o = headerNames.nextElement();
            String header = request.getHeader(o.toString());
            System.out.println("headerNames : " + o + " = " + header);
        }
        System.out.println("_________________");

        String content = request.getParameter("content");
        System.out.println("content =" + content);

        BizObject ret = new BizObject();
        ret.set("tid", "0000");

        return ReturnCode.successData(ret).toString();
    }
}