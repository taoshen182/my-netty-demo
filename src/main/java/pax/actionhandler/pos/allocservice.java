package pax.actionhandler.pos;

import org.apache.log4j.Logger;
import pax.service.push.AllocService;
import pax.util.JsonUtils;
import sand.actionhandler.open.ServiceClient;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import tool.dao.BizObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@AccessControl("no")
public class allocservice extends ActionHandler {

	//public static final long expire_minute=10;  //过期时间（分钟）

	static Logger logger = Logger.getLogger(allocservice.class);


	public allocservice(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		//super._isPosOpenService = true;
		//super._isOpenService =true;
		//ßorg.apache.curator.framework.api.ProtectACLCreateModePathAndBytesable.CreateBuilder  cb=null;
//		cb.creatingParentsIfNeeded();
//		cb.creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath()
//				.forPath(key, value.getBytes(Constants.UTF_8));

	}

	@Ajax
	public String servers(){
        String server = AllocService.getInstance().servers();
        logger.info("长连接地址为："+server);
		return server;

	}

	@Ajax
	public String server(){
		return AllocService.getInstance().server();

	}


}
