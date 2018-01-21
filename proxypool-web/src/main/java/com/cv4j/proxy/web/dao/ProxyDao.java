package com.cv4j.proxy.web.dao;

import com.cv4j.proxy.web.domain.ProxyData;
import com.cv4j.proxy.web.dto.ProxyDataDTO;
import com.cv4j.proxy.web.dto.QueryProxyDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by tony on 2017/11/16.
 */
public interface ProxyDao {

    boolean saveProxy(ProxyData proxyData);

    List<ProxyData> findProxyByCond(QueryProxyDTO queryProxyDTO, boolean isGetAll);

    List<ProxyDataDTO> findLimitProxy(int count);

    boolean updateProxyById(String id);

    boolean deleteProxyById(String id);

    void deleteAll();

    Map<String, Class> getProxyMap();

    List<ProxyData> takeRandomTenProxy();
}
