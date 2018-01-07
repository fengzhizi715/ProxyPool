package com.cv4j.proxy.web.dao;

import com.cv4j.proxy.web.dto.ProxyResource;
import com.cv4j.proxy.web.dto.ResourcePlan;

import java.util.List;

public interface ProxyResourceDao {

    List<ProxyResource> findAllProxyResource();

    boolean saveProxyResource(ProxyResource proxyResource);

    boolean saveResourcePlan(ResourcePlan resourcePlan);

    List<ResourcePlan> findAllResourcePlan();

    boolean deleteResourcePlan(ResourcePlan resourcePlan);

}
