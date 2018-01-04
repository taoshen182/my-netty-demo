package pax.model;

import org.apache.log4j.Logger;
import pax.util.RSA;
import sand.actionhandler.open.OpenError;
import sand.actionhandler.open.OpenServiceException;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;


/**
 * Created by fable on 15-2-11.
 */
public class RsaKey  implements Serializable{ 
    private PublicKey clientpub;
   // private PublicKey root_clientpub;
    private PrivateKey serverpri;
    //private PrivateKey root_serverpri;
    //private PrivateKey clientpri;

    static Logger logger = Logger.getLogger(RsaKey.class);

    public PublicKey getPub() {
        return clientpub;
    }
    public RSAPublicKey getRsaPub() {
        return (RSAPublicKey)clientpub;
    }

    public void setPub(PublicKey clientpub) {
        this.clientpub = clientpub;
    }

    public PrivateKey getPri() {
        return serverpri;
    }
    public RSAPrivateKey getRsaPri() {
        return (RSAPrivateKey)serverpri;
    }

    public void setPri(PrivateKey serverpri) {
        this.serverpri = serverpri;
    }

  //  public PrivateKey getClientpri() {
      //  return clientpri;
   // }

   // public void setClientpri(PrivateKey clientpri) {
       // this.clientpri = clientpri;
  //  }
    
//    PrivateKey serverPri;
//    PublicKey clientPub;
    public void loadKey(String server_pri,String client_pub){
        try {
			clientpub = RSA.Str2PublicKey(client_pub);
			serverpri = RSA.Str2PrivateKey(server_pri);
        } catch (Exception e) {
            logger.error("error:",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new CmdException(Context.errcode_load_key);
			throw new OpenServiceException(OpenError.ERROR_LOADKEY);
		}
        
    	
    }

    public void loadPriKey(String server_pri){
        try {
			//clientpub = RSA.Str2PublicKey(client_pub);
			serverpri = RSA.Str2PrivateKey(server_pri);
        } catch (Exception e) {
			// TODO Auto-generated catch block
            logger.error("error:",e);
			e.printStackTrace();
			throw new OpenServiceException(OpenError.ERROR_LOADKEY);
		}
        
    	
    }

    public void loadPriKey(byte[] server_pri){
        try {
            //clientpub = RSA.Str2PublicKey(client_pub);
            serverpri = RSA.Bytes2PrivateKey(server_pri);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("error:",e);
            e.printStackTrace();
            throw new OpenServiceException(OpenError.ERROR_LOADKEY);
        }


    }
    public void loadPubKey(byte[] client_pub){
        try {
            clientpub = RSA.Bytes2PublicKey(client_pub);
            //serverpri = RSA.Str2PrivateKey(server_pri);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new OpenServiceException(OpenError.ERROR_LOADKEY);
        }


    }
    public void loadPubKey(String client_pub){
        try {
			clientpub = RSA.Str2PublicKey(client_pub);
			//serverpri = RSA.Str2PrivateKey(server_pri);
        } catch (Exception e) {
            logger.error("when load client_pub "+client_pub,e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OpenServiceException(OpenError.ERROR_LOADKEY);
		}
        
    	
    }
//    public void loadClientPriKey(String client_pri){
//        try {
//			clientpri = RSA.Str2PrivateKey(client_pri);
//			//serverpri = RSA.Str2PrivateKey(server_pri);
//        } catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new OpenServiceException(OpenError.ERROR_LOADKEY);
//		}
//
//
//    }

}
