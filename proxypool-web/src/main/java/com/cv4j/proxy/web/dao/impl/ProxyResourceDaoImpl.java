package com.cv4j.proxy.web.dao.impl;

import com.cv4j.proxy.web.config.Constant;
import com.cv4j.proxy.web.dao.CommonDao;
import com.cv4j.proxy.web.dao.ProxyResourceDao;
import com.cv4j.proxy.web.dto.ProxyResource;
import com.cv4j.proxy.web.dto.ResourcePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ProxyResourceDaoImpl implements ProxyResourceDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommonDao commonDao;

    @Override
    public List<ProxyResource> findAllProxyResource() {
        return mongoTemplate.findAll(ProxyResource.class, Constant.COL_NAME_PROXY_RESOURCE);
    }

    @Override
    public void saveProxyResource(ProxyResource proxyResource) {
        if(proxyResource.getResId() == null || proxyResource.getResId() == 0) {
            //insert
            proxyResource.setResId(commonDao.getNextSequence(Constant.COL_NAME_PROXY_RESOURCE).getSequence());
//            proxyResource.setAddTime(JodaUtils.getCurrentTime());
            proxyResource.setAddTime(new Date().getTime());
        }
        proxyResource.setModTime(new Date().getTime());
        mongoTemplate.save(proxyResource, Constant.COL_NAME_PROXY_RESOURCE);
    }

    @Override
    public void saveResourcePlan(ResourcePlan resourcePlan) {
        if(resourcePlan.getAddTime() == 0) {
            //insert
            resourcePlan.setAddTime(new Date().getTime());
        }
        resourcePlan.setModTime(new Date().getTime());
        mongoTemplate.save(resourcePlan, Constant.COL_NAME_RESOURCE_PLAN);
    }

    @Override
    public List<ResourcePlan> findAllResourcePlan() {
        return mongoTemplate.findAll(ResourcePlan.class, Constant.COL_NAME_RESOURCE_PLAN);
    }

    @Override
    public void deleteResourcePlan(ResourcePlan resourcePlan) {
        mongoTemplate.remove(resourcePlan, Constant.COL_NAME_RESOURCE_PLAN);
    }

}
