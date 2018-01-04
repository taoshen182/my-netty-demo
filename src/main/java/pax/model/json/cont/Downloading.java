package pax.model.json.cont;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class Downloading {
    private String appname;
    private long appvercode;
    private String md5;

    @Override
    public String toString() {
        return "Downloading{" +
                "appname='" + appname + '\'' +
                ", appvercode=" + appvercode +
                ", md5='" + md5 + '\'' +
                '}';
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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
