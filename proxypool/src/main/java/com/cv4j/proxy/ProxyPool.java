package com.cv4j.proxy;

import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.site.ip181.Ip181ProxyListPageParser;
import com.cv4j.proxy.site.ip66.Ip66ProxyListPageParser;
import com.cv4j.proxy.site.ipway.IpwayProxyListPageParser;
import com.cv4j.proxy.site.mimiip.MimiipProxyListPageParser;
import com.cv4j.proxy.site.xicidaili.XicidailiProxyListPageParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 代理池
 * Created by tony on 2017/10/19.
 */
public class ProxyPool {

    public static CopyOnWriteArrayList<Proxy> proxyList = new CopyOnWriteArrayList<>();
    public static Map<String, Class> proxyMap = new HashMap<>();
    private static AtomicInteger index = new AtomicInteger();

    /**
     * 采用round robin算法来获取Proxy
     * @return
     */
    public static Proxy getProxy(){

        Proxy result = null;

        if (proxyList.size() > 0) {

            if (index.get() > proxyList.size()-1) {
                index.set(0);
            }

            result = proxyList.get(index.get());
            index.incrementAndGet();
        }

        return result;
    }
}
