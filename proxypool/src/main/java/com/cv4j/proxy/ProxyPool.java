package com.cv4j.proxy;

import com.cv4j.proxy.domain.Proxy;
import com.safframework.tony.common.utils.Preconditions;

import java.util.HashMap;
import java.util.List;
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

    public static void addProxy(Proxy proxy) {

        if (Preconditions.isNotBlank(proxy)) {
            proxyList.add(proxy);
        }
    }

    public static void addProxyList(List<Proxy> proxies) {

        if (Preconditions.isNotBlank(proxies)) {
            proxyList.addAll(proxies);
        }
    }
}
