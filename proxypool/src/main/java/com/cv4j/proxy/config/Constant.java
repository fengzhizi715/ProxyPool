package com.cv4j.proxy.config;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by tony on 2017/10/19.
 */
public class Constant {

    /**
     * 单个ip请求间隔，单位ms
     */
    public final static long TIME_INTERVAL = 1000;

    public static final String COL_NAME_Proxy = "Proxy";

    public static final String COL_NAME_Job_Log = "job_log";

    public final static String[] userAgentArray = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36"
    };

    public static Timestamp getCurrentDateTime() {
        java.util.Date date = new java.util.Date();
        Timestamp timeStamp = new Timestamp(date.getTime());
        return timeStamp;
    }

    public static String getStringFromTimestamp(Timestamp ts) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df1.format(ts);
    }

    public static String getDateString(Date date) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        return df1.format(date);
    }

    public static String getCurrentDateString() {
        return getStringFromTimestamp(getCurrentDateTime());
    }
}
