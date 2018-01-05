package com.cv4j.proxy.web.dao;

import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.web.dto.ProxyResource;
import com.cv4j.proxy.web.dto.ResourcePlan;

import java.util.List;

public interface ProxyResourceDao {

    List<ProxyResource> findAllProxyResource();

    void saveProxyResource(ProxyResource proxyResource);

    void saveResourcePlan(ResourcePlan resourcePlan);

    List<ResourcePlan> findAllResourcePlan();

    void deleteResourcePlan(ResourcePlan resourcePlan);

}
