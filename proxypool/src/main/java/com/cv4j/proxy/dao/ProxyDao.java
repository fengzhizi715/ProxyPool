package com.cv4j.proxy.dao;

import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.domain.QueryProxyDTO;

import java.util.List;

/**
 * Created by tony on 2017/11/16.
 */
public interface ProxyDao {

    void saveProxy(Proxy proxy);

    List<Proxy> findProxyByCond(QueryProxyDTO queryProxyDTO);

    void updateProxyById(String id);

    void deleteProxyById(String id);
}
