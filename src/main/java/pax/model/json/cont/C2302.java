package pax.model.json.cont;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import pax.util.JsonUtils;

/**
 * Created by fable on 15-3-2.
 */

@JsonInclude(Include.NON_NULL)

public class C2302 {
//    "appname": "发生的",
//            "appvercode": "109",
//            "range":0,
//            "md5": "JdVa0oOqQAr0ZMdtcTwHrQ=="
    private String appname;
    private long appvercode;
    private long range;
    private String md5;
    private String sha2;
    private String filename;

    public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public long getAppvercode() {
        return appvercode;
    }

    public void setAppvercode(long appvercode) {
        this.appvercode = appvercode;
    }

    public long getRange() {
        return range;
    }

    public void setRange(long range) {
        this.range = range;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
