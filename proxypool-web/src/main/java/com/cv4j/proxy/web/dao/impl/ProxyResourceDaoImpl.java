package com.cv4j.proxy.web.dao.impl;

import com.cv4j.proxy.web.config.Constant;
import com.cv4j.proxy.web.dao.CommonDao;
import com.cv4j.proxy.web.dao.ProxyResourceDao;
import com.cv4j.proxy.web.domain.ProxyResource;
import com.cv4j.proxy.web.domain.ResourcePlan;
import com.mongodb.WriteResult;
import com.safframework.tony.common.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "modTime"));

        return mongoTemplate.find(query, ProxyResource.class, Constant.COL_NAME_PROXY_RESOURCE);
    }

    @Override
    public boolean saveProxyResource(ProxyResource proxyResource) {
        boolean result = false;
        if(Preconditions.isBlank(proxyResource.getResId())) {            //insert
            proxyResource.setResId(commonDao.getNextSequence(Constant.COL_NAME_PROXY_RESOURCE).getSequence());
            proxyResource.setAddTime(new Date().getTime());
            proxyResource.setModTime(new Date().getTime());
            mongoTemplate.save(proxyResource, Constant.COL_NAME_PROXY_RESOURCE);

            result = Preconditions.isNotBlank(proxyResource.getId());
        } else {                                                        //update
            Query query = new Query().addCriteria(Criteria.where("resId").is(proxyResource.getResId()));
            Update update = new Update();
            update.set("webName", proxyResource.getWebName());
            update.set("webUrl", proxyResource.getWebUrl());
            update.set("pageCount", proxyResource.getPageCount());
            update.set("prefix", proxyResource.getPrefix());
            update.set("suffix", proxyResource.getSuffix());
            update.set("parser", proxyResource.getParser());
            update.set("modTime", new Date().getTime());

            WriteResult writeResult = mongoTemplate.updateFirst(query, update, Constant.COL_NAME_PROXY_RESOURCE);

            result = writeResult!=null && writeResult.getN() > 0;
        }
        return result;
    }

    @Override
    public boolean saveResourcePlan(ResourcePlan resourcePlan) {
        boolean result = false;
        if(resourcePlan.getAddTime() == 0) { //insert
            resourcePlan.setAddTime(new Date().getTime());
            resourcePlan.setModTime(new Date().getTime());

            mongoTemplate.save(resourcePlan, Constant.COL_NAME_RESOURCE_PLAN);
            result = Preconditions.isNotBlank(resourcePlan.getId());

        } else {                            //update
            Query query = new Query().addCriteria(Criteria.where("_id").is(resourcePlan.getId()));
            Update update = new Update();
            update.set("startPageNum", resourcePlan.getStartPageNum());
            update.set("endPageNum", resourcePlan.getEndPageNum());
            update.set("modTime", new Date().getTime());

            WriteResult writeResult = mongoTemplate.updateFirst(query, update, Constant.COL_NAME_RESOURCE_PLAN);
            result = writeResult!=null && writeResult.getN() > 0;
        }

        return result;
    }

    @Override
    public List<ResourcePlan> findAllResourcePlan() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "modTime"));
        return mongoTemplate.find(query, ResourcePlan.class, Constant.COL_NAME_RESOURCE_PLAN);
    }

    @Override
    public boolean deleteResourcePlan(ResourcePlan resourcePlan) {

        WriteResult writeResult = mongoTemplate.remove(resourcePlan, Constant.COL_NAME_RESOURCE_PLAN);

        return writeResult!=null && writeResult.getN() > 0;
    }

}
