
package pax.exception;

public class CmdException extends RuntimeException {
	public int cmd;
	public int code;
	
	public CmdException(int cmd,int code){
		this.cmd=cmd;
		this.code=code;
	}
	
	public CmdException(int code){
		this.cmd=0;
		this.code=code;
	}
		
}
