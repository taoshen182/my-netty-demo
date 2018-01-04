package pax.service.plantask;

/**
 * Created by want on 2017/10/17.
 */
public class PlanTaskContext {
    /**
     * 字段之间以 | 分隔
     */
    public static final String FIELD_SEPARATE_CHAR = "|";

    /**
     * 下载任务通知交易码	Ans6	007001
     */
    public static final String TRADE_CODE_TASK = "007001";
    /**
     * 下载任务完成通知交易码	Ans6	007008
     */
    public static final String TRADE_CODE_TASK_FINISH = "007008";

    /**
     * 渠道id	Ans4	CPOS
     */
    public static final String CHANNEL_CPOS = "CPOS";

    /**
     * 平台标识	Ans8	11111111
     */
    public static final String PLATFORM = "11111111";

    /**
     * 下载内容标志	Ans2	00
     */
    public static final String DOWNLOAD_CONTENT_MARK = "00";
    /**
     * 应用版本	Ans50	000001
     */
    public static final String APP_VER = "000001";

    /**
     * 下载时机标志	Ans4	9001
     */
    public static final String DOWNLOAD_TIME_MARK = "";

    /**
     * 自动重拨间隔	Ans2	60
     */
    public static final String AUTO_REDIAL_INTERVAL = "60";

    /**
     * 未同步到交易系统
     */
    public static final String TASK_UPDATE_NO = "0";
    /**
     * 已经同步到交易系统
     */
    public static final String TASK_UPDATE_YES = "1";

    /**
     * 下载未完成
     */
    public static final String TASK_FINISH_NO = "0";
    /**
     * 下载完成
     */
    public static final String TASK_FINISH_YES = "1";

    /**
     * 交易系统 返回成功
     */
    public static final String RESP_CODE_SUCCESS = "00";

    /**
     * 交易响应报文字段名称
     */
    public class TradeResponseField {
        /**
         * 1	交易码
         */
        public static final String TRADE_CODE = "trade_code";
        /**
         * 2	渠道id
         */
        public static final String CHANNEL_ID = "channel_id";
        /**
         * 3	商户号
         */
        public static final String MERCHANT_NO = "mer_no";
        /**
         * 4	终端号
         */
        public static final String TER_NO = "ter_no";
        /**
         * 5	任务内容ID
         */
        public static final String TASK_ID = "task_id";
        /**
         * 6	响应码
         */
        public static final String RESP_CODE = "resp_code";
    }
}
