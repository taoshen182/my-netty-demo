/*
 * Created on 2005-4-21
 *
 * To change the template for this generated file go to  
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package pax.servlet;


import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;

import sand.basic.Global;

import javax.servlet.ServletContextEvent;

//import sand.basic.GlobalConfig;
//import sand.comm.socket.EchoServer;
//import sand.depot.jni.janitor.*;
//import sand.depot.job.ImportJob;
//import sand.depot.job.TestShedule;


public class ContextListener extends  sand.depot.servlet.system.ContextListener {

	// org.apache.jsp.receiveBill.pactDetailList_jsp
	// org.apache.jsp.productionPlan.pptList3_jsp
	

	private java.util.Timer timer = null;

	// static Logger logger = Logger.getLogger(ContextListener.class);
	char[] a = { 0x12, 0x22, 0x98 };

	static Logger logger = Logger.getLogger(ContextListener.class);

	// String _isJanitorServer = "no";
	// String _system_config_file = "";

	DataSource ds = null;
	DataSource ds2 = null;

	//TestShedule testsched = new TestShedule();

	// 系统默认配置文件所在位置
	// static String filePath;

	public void init(ServletContextEvent event) {
		super.init(event);
		//Global.add(ReturnCode.ERROR_NOEXIST_TUNNEL);
	}


	

	/*
	 * @see javax.servlet.servletcontextlistener#contextinitialized(javax.servlet.servletcontextevent)
	 */
	public void contextInitialized(ServletContextEvent event){
		super.contextInitialized(event);
	}

	/*
	 * @see javax.servlet.servletcontextlistener#contextdestroyed(javax.servlet.servletcontextevent)
	 */
	public void contextDestroyed(ServletContextEvent event){
		super.contextDestroyed(event);
	}

}
