package pax.model.json.cont;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import pax.util.JsonUtils;

import java.util.List;

@JsonInclude(Include.NON_NULL)

public class C1301 {
    List<ApkJson> apks;
    List<Msg> msgs;

    @Override
    public String toString() {
        return JsonUtils.toJSon(this);
    }

    public List<ApkJson> getApks() {
        return apks;
    }

    public void setApks(List<ApkJson> apks) {
        this.apks = apks;
    }

    public List<Msg> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<Msg> msgs) {
        this.msgs = msgs;
    }

}
