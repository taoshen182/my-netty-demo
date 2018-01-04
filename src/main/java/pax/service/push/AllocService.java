package pax.service.push;

import com.mpush.api.spi.common.CacheManagerFactory;
import com.mpush.api.spi.common.ServiceDiscoveryFactory;
import com.mpush.api.srd.ServiceDiscovery;
import com.mpush.api.srd.ServiceListener;
import com.mpush.api.srd.ServiceNames;
import com.mpush.api.srd.ServiceNode;
import com.mpush.common.user.UserManager;
import org.apache.log4j.Logger;
import sand.annotation.Ajax;
import tool.dao.BizObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * Created by xieke on 2017/3/28.
 */
public class AllocService {


        static AllocService allocService = null;// new AllocService();

        private AllocService(){
            this.start();

        }


        public static AllocService getInstance(){
            if(allocService==null)
                allocService = new AllocService();

            return allocService;
        }
        //public static final long expire_minute=10;  //过期时间（分钟）

        static Logger logger = Logger.getLogger(pax.actionhandler.pos.allocservice.class);

        private ScheduledExecutorService scheduledExecutor;
        private List<ServerNode> serverNodes = Collections.emptyList();
        private final ServiceDiscovery discovery = ServiceDiscoveryFactory.create();



        public void start() {
            //CacheManagerFactory.create().init(); //启动缓冲服务
            CacheManagerFactory.create().init(); //启动缓冲服务

            // discovery = ServiceDiscoveryFactory.create();// 启动发现服务
            if(discovery.isRunning()){
                return;
            }
            discovery.syncStart();
            discovery.subscribe(ServiceNames.CONN_SERVER, new ConnServerNodeListener());


//		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
//		scheduledExecutor.scheduleAtFixedRate(this::refresh, 0, 5, TimeUnit.MINUTES);
        }


        public String servers(){

            this.start();
            this.refresh();
            StringBuilder sb = new StringBuilder();
            Iterator<ServerNode> it = serverNodes.iterator();
           // BizObject b = new BizObject();
            //sb.append("{");
            if (it.hasNext()) {
               ServerNode node = it.next();
//               b.set("host",node.host);
//               b.set("port",node.port);
               //sb.append("host");
                sb.append(node.host).append(':').append(node.port);
            }

            while (it.hasNext()) {
                ServerNode node = it.next();
                sb.append(',').append(node.host).append(':').append(node.port);
            }
            return sb.toString();

        }

    public String server(){

        this.start();
        this.refresh();
       // StringBuilder sb = new StringBuilder();
        Iterator<ServerNode> it = serverNodes.iterator();
        BizObject b = new BizObject();
        //sb.append("{");
        if (it.hasNext()) {
            ServerNode node = it.next();
            b.set("host",node.host);
            b.set("port",node.port);
           // sb.append("host")
           // sb.append(node.host).append(':').append(node.port);
        }

//        while (it.hasNext()) {
//            ServerNode node = it.next();
//            sb.append(',').append(node.host).append(':').append(node.port);
//        }
        return b.toString();

    }

    public String onlines(){
        this.start();
        this.refresh();
        StringBuilder sb = new StringBuilder();
        Iterator<ServerNode> it = serverNodes.iterator();
        if (it.hasNext()) {
            ServerNode node = it.next();
            sb.append(node.host).append(':').append(node.port).append('|').append(node.onlineUserNum);
        }

        while (it.hasNext()) {
            ServerNode node = it.next();
            sb.append(',').append(node.host).append(':').append(node.port).append('|').append(node.onlineUserNum);
        }
        return sb.toString();

    }
        /**
         * 从zk中获取可提供服务的机器,并以在线用户量排序
         */
        private void refresh() {
            //1.从缓存中拿取可用的长链接服务器节点
            List<ServiceNode> nodes = discovery.lookup(ServiceNames.CONN_SERVER);
            if (nodes.size() > 0) {
                //2.对serverNodes可以按某种规则排序,以便实现负载均衡,比如:随机,轮询,链接数量等
                this.serverNodes = nodes
                        .stream()
                        .map(this::convert)
                        .sorted(ServerNode::compareTo)
                        .collect(Collectors.toList());
            }
        }
        private long getOnlineUserNum(String publicIP) {
            //String public_ip = node.getAttr(ServiceNames.ATTR_PUBLIC_IP);
            return UserManager.I.getOnlineUserNum(publicIP);
        }

        private ServerNode convert(ServiceNode node) {
            String public_ip = node.getAttr(ServiceNames.ATTR_PUBLIC_IP);
            long onlineUserNum = getOnlineUserNum(public_ip);
            return new ServerNode(public_ip, node.getPort(), onlineUserNum);
        }

        private class ConnServerNodeListener implements ServiceListener {

            @Override
            public void onServiceAdded(String s, ServiceNode serviceNode) {
                refresh();
            }

            @Override
            public void onServiceUpdated(String s, ServiceNode serviceNode) {
                refresh();
            }

            @Override
            public void onServiceRemoved(String s, ServiceNode serviceNode) {
                refresh();
            }
        }

        private  class ServerNode implements Comparable<ServerNode> {
            long onlineUserNum = 0;
            String host;
            int port;

            public ServerNode(String host, int port, long onlineUserNum) {
                this.onlineUserNum = onlineUserNum;
                this.host = host;
                this.port = port;
            }

            @Override
            public int compareTo(ServerNode o) {
                return Long.compare(onlineUserNum, o.onlineUserNum);
            }
        }


    }
