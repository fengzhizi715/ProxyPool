package com.cv4j.proxy.web.dao;

import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.web.dto.QueryProxyDTO;
import com.cv4j.proxy.web.dto.ResultProxy;

import java.util.List;
import java.util.Map;

/**
 * Created by tony on 2017/11/16.
 */
public interface ProxyDao {

    void saveProxy(Proxy proxy);

    List<Proxy> findProxyByCond(QueryProxyDTO queryProxyDTO, boolean isGetAll);

    List<ResultProxy> findAllProxy();

    void updateProxyById(String id);

    void deleteProxyById(String id);

    void deleteAll();

    Map<String, Class> getProxyMap();
}
