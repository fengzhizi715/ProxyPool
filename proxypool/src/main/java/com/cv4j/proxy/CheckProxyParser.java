package com.cv4j.proxy;

import com.cv4j.proxy.site.Data4uProxyListPageParser;
import com.cv4j.proxy.site.GoubanjiaProxyListPageParser;
import com.cv4j.proxy.site.M66ipProxyListPageParser;
import com.cv4j.proxy.site.MogumiaoProxyListPageParser;

import java.util.HashMap;
import java.util.Map;

public class CheckProxyParser {

    public static void main(String[] args) {
        System.out.println("Start...");

        Map<String, Class> proxyMap = new HashMap<>();
//        proxyMap.put("http://www.xicidaili.com/nn/1.html", XicidailiProxyListPageParser.class);
//        proxyMap.put("https://proxy.mimvp.com/", MimvpProxyListPageParser.class);
//        proxyMap.put("http://www.mogumiao.com/web", MogumiaoProxyListPageParser.class);
//        proxyMap.put("http://www.goubanjia.com/", GoubanjiaProxyListPageParser.class);
//        proxyMap.put("http://m.66ip.cn/2.html", M66ipProxyListPageParser.class);
        proxyMap.put("http://www.data5u.com/", Data4uProxyListPageParser.class);


        ProxyPool.proxyMap = proxyMap;

        ProxyManager proxyManager = ProxyManager.get();
        proxyManager.start();

        System.out.println("End...");
    }
}
