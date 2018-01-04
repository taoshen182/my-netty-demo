package pax.model.json.cont;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import pax.util.JsonUtils;

import java.util.List;

@JsonInclude(Include.NON_NULL)

public class C2301 {
    private List<App> apps;
    private List<Downloading> downloadings;

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    private String datas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNpage() {
        return npage;
    }

    public void setNpage(String npage) {
        this.npage = npage;
    }

    public String getPsize() {
        return psize;
    }

    public void setPsize(String psize) {
        this.psize = psize;
    }

    private String name;
    private String npage;
    private String psize;


    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public List<Downloading> getDownloadings() {
        return downloadings;
    }

    public void setDownloadings(List<Downloading> downloadings) {
        this.downloadings = downloadings;
    }



    @Override
    public String toString() {
        return JsonUtils.toJSon(this);
    }


}
