package pax.model;

import org.apache.log4j.Logger;
import pax.util.Context;

import javax.crypto.SecretKey;
import java.io.Serializable;

/**
 * Created by fable on 15-2-11.
 */
public class SessionVar implements Serializable{
	private static final long serialVersionUID = -5726274638698732257L; 
    //    private String encryptsn;
    private String sn;
    
    //private int deploymentid;
    //private int jobid;
   // private int id;
    private String ip;   
    private SecretKey key;
    private String token;
    private long empireTime;
    
    static Logger logger = Logger.getLogger(SessionVar.class);
    public SessionVar(){
    	//this.setEmpireTime(System.currentTimeMillis()+openservice.expire_minute*60*1000);
    	long empired_time=System.currentTimeMillis()+Context.expire_time;
    	logger.info("set empired_time "+System.currentTimeMillis()+" + "+Context.expire_time+" = "+empired_time);
    	this.setEmpireTime(empired_time);
    }
    public long getEmpireTime() {
		return empireTime;
	}

	public void setEmpireTime(long empireTime) {
		this.empireTime = empireTime;
	}

	private String signSecret;
    public String getSignSecret() {
		return signSecret;
	}

	public void setSignSecret(String signSecret) {
		this.signSecret = signSecret;
	}
	private String clientPubKey;

	public String getClientPubKey() {
		return clientPubKey;
	}

	public void setClientPubKey(String clientPubKey) {
		this.clientPubKey = clientPubKey;
	}

//	private RsaKey rsaKey;
//	
//	public RsaKey getRsaKey() {
//		return rsaKey;
//	}
//
//	public void setRsaKey(RsaKey rsaKey) {
//		this.rsaKey = rsaKey;
//	//	throw new CmdException(Context.errcode_system);
//	}

	//    private String encrypttoken;
    //    private int userid;


//    @Override
//    public String toString() {
//        return JsonUtils.toJSon(this);
//    }

    @Override
    public String toString() {
        return "SessionVar{" +
//                "idcode='" + idcode + '\'' +
//                ", encryptidcode='" + encryptidcode + '\'' +
                ", key=" + key +
                ", token='" + token + '\'' +
                ", sn='" + sn + '\'' +   
                ", ip='" + ip + '\'' +
                '}';
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

//    public int getDeploymentid() {
//        return deploymentid;
//    }
//
//    public void setDeploymentid(int deploymentid) {
//        this.deploymentid = deploymentid;
//    }
//
//    public int getJobid() {
//        return jobid;
//    }
//
//    public void setJobid(int jobid) {
//        this.jobid = jobid;
//    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
