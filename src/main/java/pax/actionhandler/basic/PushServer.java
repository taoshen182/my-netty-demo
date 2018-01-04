package pax.actionhandler.basic;

import com.mpush.bootstrap.Main;
import com.mpush.bootstrap.ServerLauncher;
import com.mpush.core.server.ConnectionServer;
import com.mpush.netty.server.NettyTCPServer;
import com.mpush.tools.log.Logs;
import pax.service.push.AllocService;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xieke on 2017/3/28.
 */
public class PushServer extends ActionHandler{
    public PushServer(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Ajax
    public String start(){


        if(ConnectionServer.I().isRunning()){
            return "already started";
        }
        //NettyTCPServer.State.Created, NettyTCPServer.State.Initialized
        System.setProperty("io.netty.leakDetection.level", "PARANOID");
        System.setProperty("io.netty.noKeySetOptimization", "false");
        Main.main(null);
        return "start success";

    }

    @Ajax
    public String test(){
        return "test";
    }

    @Ajax
    public String stop(){

        if(!ConnectionServer.I().isRunning()){
            return "already stopped";
        }

        ServerLauncher launcher = new ServerLauncher();
        launcher.stop();
        return "stop success";
    }

    @Ajax
    public String servers(){

        return AllocService.getInstance().servers();
    }

    @Ajax
    public String onlines(){

        return AllocService.getInstance().onlines();
    }


}
