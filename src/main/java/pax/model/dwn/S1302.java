package pax.model.dwn;

/**
 * Created by fable on 15-3-10.
 */
public class S1302 {
    private String filepath;
    private String filename;
    private int mystatus;
    private int mycmd;
    private String mytoken;
    private long range;

    @Override
    public String toString() {
        return "S1302{" +
                "filepath='" + filepath + '\'' +
                ", filename='" + filename + '\'' +
                ", mystatus=" + mystatus +
                ", mycmd=" + mycmd +
                ", mytoken='" + mytoken + '\'' +
                ", range=" + range +
                '}';
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getMystatus() {
        return mystatus;
    }

    public void setMystatus(int mystatus) {
        this.mystatus = mystatus;
    }

    public int getMycmd() {
        return mycmd;
    }

    public void setMycmd(int mycmd) {
        this.mycmd = mycmd;
    }

    public String getMytoken() {
        return mytoken;
    }

    public void setMytoken(String mytoken) {
        this.mytoken = mytoken;
    }

    public long getRange() {
        return range;
    }

    public void setRange(long range) {
        this.range = range;
    }
}
