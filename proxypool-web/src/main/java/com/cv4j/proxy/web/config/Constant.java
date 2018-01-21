package com.cv4j.proxy.web.config;

import com.cv4j.proxy.site.xicidaili.XicidailiProxyListPageParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tony on 2017/12/27.
 */
public class Constant {

    public final static String COL_NAME_JOB_LOG = "Job_Log";

    public final static String COL_NAME_SYS_SEQUENCE = "Sys_Sequence";

    public final static String COL_NAME_PROXY = "Proxy";

    public final static String COL_NAME_PROXY_RESOURCE = "Proxy_Resource";

    public final static String COL_NAME_RESOURCE_PLAN = "Resource_Plan";

    public final static Map<String, Class> proxyMap = new HashMap<>();

    static {
        int pages = 8;
        for (int i = 1; i <= pages; i++) {
            proxyMap.put("http://www.xicidaili.com/nn/" + i + ".html", XicidailiProxyListPageParser.class);
        }
    }
}
