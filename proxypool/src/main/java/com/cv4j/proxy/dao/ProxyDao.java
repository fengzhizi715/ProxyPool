package com.cv4j.proxy.dao;

import com.cv4j.proxy.domain.Proxy;

/**
 * Created by tony on 2017/11/16.
 */
public interface ProxyDao {

    void saveProxy(Proxy proxy);

    Proxy findProxyByIp(String ip);

    void deleteProxyById(Long id);
}
