package pax.actionhandler.pos;

import com.mpush.api.Constants;
import com.mpush.api.push.*;
import com.mpush.api.spi.common.ServiceDiscoveryFactory;
import com.mpush.api.srd.ServiceDiscovery;
import com.mpush.api.srd.ServiceListener;
import com.mpush.api.srd.ServiceNames;
import com.mpush.api.srd.ServiceNode;
import com.mpush.common.user.UserManager;
import com.mpush.tools.Jsons;
import com.mpush.tools.common.Strings;
import com.sun.net.httpserver.HttpExchange;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class pushservice extends PosOpenService {

	//public static final long expire_minute=10;  //过期时间（分钟）

	//static Logger logger = Logger.getLogger(pushservice.class);

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	private final PushSender pushSender = PushSender.create();
	private final AtomicInteger idSeq = new AtomicInteger();

	public void start() {
		pushSender.start();
	}

	public void stop() {
		pushSender.stop();
	}



	private byte[] readBody() throws IOException {
		_request.setCharacterEncoding("UTF-8");
		InputStream in = _request.getInputStream();//.get.getRequestBody();
		int length = _request.getContentLength();//.getRequestHeaders().getFirst("content-length");

		if (length!=0) {
			byte[] buffer = new byte[length];
			in.read(buffer);
			in.close();
			return buffer;
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			in.close();
			return out.toByteArray();
		}
	}
	@SuppressWarnings("unchecked")

	public void push() throws IOException {
		String body = new String(readBody(), Constants.UTF_8);
		Map<String, Object> params = Jsons.fromJson(body, Map.class);

		sendPush(params);

		byte[] data = "服务已经开始推送,请注意查收消息".getBytes(Constants.UTF_8);
		_response.addHeader("Content-Type","text/plain; charset=utf-8");
		_response.addIntHeader("content-length",200);

		//_response.sendResponseHeaders(200, data.length);//200, content-length
		//OutputStream out = httpExchange.getResponseBody();
		OutputStream out = _response.getOutputStream();
		out.write(data);
		out.close();
	}



	private void sendPush(Map<String, Object> params) {
		String userId = (String) params.get("userId");
		String hello = (String) params.get("hello");
		Boolean broadcast = (Boolean) params.get("broadcast");
		String condition = (String) params.get("condition");


		NotificationDO notificationDO = new NotificationDO();
		notificationDO.content = "MPush开源推送，" + hello;
		notificationDO.title = "MPUSH推送";
		notificationDO.nid = idSeq.get() % 2 + 1;
		notificationDO.ticker = "你有一条新的消息,请注意查收";
		PushMsg pushMsg = PushMsg.build(MsgType.NOTIFICATION_AND_MESSAGE, Jsons.toJson(notificationDO));
		pushMsg.setMsgId("msg_" + idSeq.incrementAndGet());

		pushSender.send(PushContext
				.build(pushMsg)
				.setUserId(Strings.isBlank(userId) ? null : userId)
				.setBroadcast(broadcast != null && broadcast)
				.setCondition(Strings.isBlank(condition) ? null : condition)
				.setCallback(new PushCallback() {
					@Override
					public void onResult(PushResult result) {
						logger.error(result.toString());
					}
				})
		);
	}

	public static final class NotificationDO {
		public String msgId;
		public String title;
		public String content;
		public Integer nid; //主要用于聚合通知，非必填
		public Byte flags; //特性字段。 0x01:声音  0x02:震动  0x03:闪灯
		public String largeIcon; // 大图标
		public String ticker; //和title一样
		public Integer number;
		public Map<String, String> extras;
	}

	public pushservice(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		//super._isPosOpenService = true;
		//super._isOpenService =true;

	}


}
