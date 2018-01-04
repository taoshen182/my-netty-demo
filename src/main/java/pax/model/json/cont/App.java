package pax.model.json.cont;

import pax.util.JsonUtils;

public class App {
	
    private String name;
    private String version;
    private long version_code;
    //private String pkgname;
    private String sha2;
    
    private int maxSize;

    public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public String getSha2() {
		return sha2;
	}

	public void setSha2(String sha2) {
		this.sha2 = sha2;
	}

	@Override
    public String toString() {
        return JsonUtils.toJSon(this);
    }
    
//    @Override
//    public String toString() {
//        return "App [appname=" + appname + ", appver=" + appver
//                + ", appvercode=" + appvercode + ", pkgname=" + pkgname + "]";
//    }

    public String getName() {
        return name;
    }

    public void setName(String appname) {
        this.name = appname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String appver) {
        this.version = appver;
    }

    public long getVersion_code() {
        return version_code;
    }

    public void setVersion_code(long appvercode) {
        this.version_code = appvercode;
    }

//    public String getPkgname() {
//        return pkgname;
//    }
//
//    public void setPkgname(String pkgname) {
//        this.pkgname = pkgname;
//    }

}