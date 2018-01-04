package pax.netty.util;

import java.util.List;

/**
 * @author : wangtao
 * @date : 2017/12/1 20:10
 */

public class TestGson {

    /**
     * timeLength : 334
     * code : 4
     * video : {"createUtc":0,"serial":0,"sourceType":3,"filePath":"","id":"c74de8a42ccf4ddcbfff66a878b17f85","isUpload":0,"title":"","categoryId":"100","channelId":"","parentId":"100","fileServer":"","status":0}
     * postUrl : http://139.196.243.11:8083/images/video/b14ff0da895444c6a4d31fed63052969_960_540.jpg
     * originalFile : /home/media/ftp/ovp/upload/000ba26e-f589-435f-9f98-e2a3bac8590d/39768592-d1d1-444b-a181-ea55b0434699.mp4
     * output : [{"path":"/home/media/ftp/ovp/output/000ba26e-f589-435f-9f98-e2a3bac8590d/e62353a4-0ba4-46d6-9147-656219e48cd7_1300.ts","bitrate":1300,"filesize":"222410","ftp":"ftp://yoongooftp:yoongoo2016@202.107.188.220:21/home/media/ftp/ovp/output/000ba26e-f589-435f-9f98-e2a3bac8590d/f026b338-6a6c-4972-8832-ab2b52585dd7_500.ts"}]
     * message : ok
     * thumbnailUrl : http://139.196.243.11:8083/images/video/f026b3386a6c49728832ab2b52585dd7_320_480.jpg
     * id : c74de8a42ccf4ddcbfff66a878b17f85
     */

    public String timeLength;
    public int code;
    public String video;
    public String postUrl;
    public String originalFile;
    public String message;
    public String thumbnailUrl;
    public String id;
    public List<OutputBean> output;

    public static class OutputBean {
        /**
         * path : /home/media/ftp/ovp/output/000ba26e-f589-435f-9f98-e2a3bac8590d/e62353a4-0ba4-46d6-9147-656219e48cd7_1300.ts
         * bitrate : 1300
         * filesize : 222410
         * ftp : ftp://yoongooftp:yoongoo2016@202.107.188.220:21/home/media/ftp/ovp/output/000ba26e-f589-435f-9f98-e2a3bac8590d/f026b338-6a6c-4972-8832-ab2b52585dd7_500.ts
         */

        public String path;
        public int bitrate;
        public String filesize;
        public String ftp;
    }
}
