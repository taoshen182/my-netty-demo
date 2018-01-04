package pax.model.json.cont;

import com.fasterxml.jackson.annotation.JsonRootName;
import pax.util.JsonUtils;

@JsonRootName("apk")
public class ApkJson {
    private String filename;
    private String appname;
    private String appver;
    private long appvercode;
    private String pkgname;
    private long size;
    private String md5;
    private String sha2;
    public String getSha2() {
		return sha2;
	}

	public void setSha2(String sha2) {
		this.sha2 = sha2;
	}

	private boolean forceinstall;
    private int typo;

    @Override
    public String toString() {
        return JsonUtils.toJSon(this);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppver() {
        return appver;
    }

    public void setAppver(String appver) {
        this.appver = appver;
    }

    public long getAppvercode() {
        return appvercode;
    }

    public void setAppvercode(long appvercode) {
        this.appvercode = appvercode;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public boolean isForceinstall() {
        return forceinstall;
    }

    public void setForceinstall(boolean forceinstall) {
        this.forceinstall = forceinstall;
    }

    public int getTypo() {
        return typo;
    }

    public void setTypo(int typo) {
        this.typo = typo;
    }
}

