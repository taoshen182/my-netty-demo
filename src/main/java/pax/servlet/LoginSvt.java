package pax.servlet;

import org.apache.log4j.Logger;
import sand.depot.business.system.Employee;
import tool.dao.BizObject;
import tool.dao.JDO;
import tool.dao.QueryFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

//import tool.uda.task.inter.TaskClassService;

/**
 * 
 * Title: 登录控制器 <br>
 * Description: 登录控制器 <br>
 * Copyright: Copyright (c) 2003 <br>
 * Company: SAND <br>
 * Time:2003/07/22 10:00 <br>
 * 
 * @author wang.yc
 * @version 1.0
 */
public class LoginSvt extends HttpServlet

{
	private Logger logger;
	//private TaskClassService taskClassService;

	/**
	 * 初始化测试开关
	 * 
	 * @throws ServletException
	 */
	public void init() throws ServletException {
		logger = Logger.getLogger("LoginSvt.logger");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		//System.out.println("in doget");
		this.doPost(request, response);
	}
	/**
	 * 处理登录，保存用户信息至session
	 * 
	 * @param request
	 *            包含登录的用户名和密码
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = null;
		String msg = null;

	//	System.out.println("in loginsvt");
		try {
			//request.setAttribute("loc", request.getLocale());
			response.sendRedirect("basic.LoginAH.connect");
		} catch (Exception e) {
			logger.info(JDO.getStackTrace(e));
			e.printStackTrace();

		} 
		
	}

	/**
	 * 
	 * 首页
	 * 接入地址
	 */
	public void connect(){
		
	}
	/**
	 * 域用户直接登录
	 * 
	 * @return
	 */
	public void domainLogin(HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			ServletException, IOException {

//		String username = request.getRemoteUser();
//		// username.startsWith(prefix, toffset);
//		username = username
//				.substring(username.indexOf("\\")+1, username.length());
//		System.out.println("username " + username);
//		QueryFactory userQuery = new QueryFactory("employee");
//		BizObject user = new BizObject("employee");
//		user.set("fieldname", username);
//		user = (BizObject) userQuery.getOne(user);
		
		System.out.println("in domainLogin");
		BizObject user = getRemoteUser(request);
		
		

		if (user == null) {
			request.setAttribute("errMsg", "域用户 " + request.getRemoteUser()
					+ " 还未绑定 erp 帐号，\n请输入你的 erp 用户名及密码作绑定");
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);

		} else {
			JDO jdo = new JDO(user);
			Employee employee = new Employee(user, jdo);
			employee.refreshById(user.getID());

			// 释放employee占用的数据库连接
			employee.releaseConnection();

			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("employee", employee);
			
			
			
			
			
			
//			employee.canDo(funId)

			System.out.println("set session "+employee.getEmployee().getId());
			request.getRequestDispatcher("main.jsp").forward(request, response);
		}

		// return false;

	}

	public BizObject getRemoteUser(HttpServletRequest request) throws SQLException{
		String username = request.getRemoteUser();
		// username.startsWith(prefix, toffset);
		if (username==null)
			username="";
		username = username
				.substring(username.indexOf("\\")+1, username.length());
		System.out.println("request domain username " + username);
		QueryFactory userQuery = new QueryFactory("employee");
		BizObject user = new BizObject("employee");
		user.set("fieldname", username);
		user = (BizObject) userQuery.getOne(user);
		return user;

	}
	
	public BizObject getRemoteUser(String  userid) throws SQLException{
		String username = userid;
		// username.startsWith(prefix, toffset);

		System.out.println("request domain username " + username);
		QueryFactory userQuery = new QueryFactory("employee");
		BizObject user = new BizObject("employee");
		user.set("fieldname", username);
		user = (BizObject) userQuery.getOne(user);
		return user;

	}	
	public boolean login2(HttpServletRequest request,
			HttpServletResponse response) {
		return true;
	}

	/**
	 * 绑定erp帐号
	 * 检查用户身份，合法保存用户信息至Session("userinfo")；否则转页至相关信息页
	 * 
	 * @param request
	 *            包含登录的用户名和密码
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		//logger.info("url is 1");
		System.out.println("in Login");
		//System.out.println("remote user is " + request.getRemoteUser());
	//	String username = request.getRemoteUser();
//		if (username==null)
//			username="";
//		username = username
//		.substring(username.indexOf("\\")+1, username.length());

		
		
		String userId = request.getParameter("userId");
		String password = request.getParameter("userPasswd");
		String domainuser = request.getParameter("domainuser");
		
		
		BizObject remoteuser = getRemoteUser(domainuser); //已经绑定的域用户
		
		QueryFactory userQuery = new QueryFactory("employee");
		BizObject user = new BizObject();
		//logger.info("url is 2");
		user.set("UserId", userId);
		user.set("Password", password);
		String url = null;
		String msg = null;
		System.out.println("userid "+userId);
		System.out.println("password "+password);
		
		request.setAttribute("domainuser", domainuser);
		//logger.info("url is 3" + url);
		
		if (domainuser==null||domainuser.equals("")){
			url = "domainlogin.jsp";
			msg = "请先登录域";

		}
		else
		if (Employee.isValidUserDes(user)==Employee.VALID) {
			
			// if (Employee.isValidUser(user)) {

			user = (BizObject) userQuery.getByID(userId);
			
			//如果该用户已经绑定了其他域帐号，
			//System.out.println("fieldname is "+user.getString("fieldname"));
			//System.out.println("username "+username); 
			
			//username =="" 说明没有过滤器
			if (!user.getString("fieldname").equals("")&&!user.getString("fieldname").equals(domainuser)){
				url = "index.jsp";
				//System.out.println(user.getString("fieldname")+" 已经绑定了其他用户");
				msg = "该 erp 帐号已经绑定了其他域用户";
				logger.info("url is " + url);
				
			}
			else{//第一次绑定
				
				
				url = request.getParameter("nextURL"); // 登录成功转用户首页
				if (url == null || url.equals("")) {
					// url = "menu.htm";
					url = "main.jsp";
				}
				
				logger.info("user is " + user);
				JDO jdo = new JDO(user);
				
				/**
				 * 原绑定的取消绑定，这里应该不会到
				 */
				if (remoteuser!=null){
					remoteuser.set("fieldname","");
					jdo.update(remoteuser);
				}
					
				
				//绑定 域用户
				user.set("fieldname", domainuser);
				jdo.update(user);
				
				Employee employee = new Employee(user, jdo);
				employee.refreshById(user.getID());
				//logger.info("url is 6" + url);
				// 释放employee占用的数据库连接
				employee.releaseConnection();
				// logger.debug(employee.getRoles());

				// p_Request.getSession().setAttribute("userInfo",user);
				// p_Request.getSession().setAttribute("user", user);
				// p_Request.getSession().setAttribute("employee", employee);

				request.getSession().setAttribute("user", user);
				request.getSession().setAttribute("employee", employee);
				System.out.println("set session");
				
			}

		} else {
			// 用户名或密码错误，应提示重新登录
			url = "index.jsp";
			msg = "用户名或密码错误";
			
			System.out.println(msg);
			logger.info("url is " + url);
		}
		request.setAttribute("errMsg", msg);
		request.getRequestDispatcher(url).forward(request, response);


	}
//	public static void main(String args[]){
//		try{
//	        UniAddress dc = UniAddress.getByName("172.16.1.1");
//	        
//	        jcifs.smb.NtlmPasswordAuthentication auth = new jcifs.smb.NtlmPasswordAuthentication("172.16.1.1", "xieke" ,"qjdble");
//	        jcifs.smb.SmbSession.logon( dc, auth );
//			
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			
//		}
//	
//	}
//	
//	public TaskClassService getTaskClassService() {
//		if(taskClassService == null) 
//			taskClassService = (TaskClassService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("uda.taskClassService");
//		return taskClassService;
//	}
//
//	public void setTaskClassService(TaskClassService taskClassService) {
//		this.taskClassService = taskClassService;
//	}
}