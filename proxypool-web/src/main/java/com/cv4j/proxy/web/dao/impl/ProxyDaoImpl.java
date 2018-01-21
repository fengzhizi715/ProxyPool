package com.cv4j.proxy.web.dao.impl;

import com.cv4j.proxy.web.config.Constant;
import com.cv4j.proxy.web.dao.ProxyDao;
import com.cv4j.proxy.web.domain.ProxyData;
import com.cv4j.proxy.web.domain.ResourcePlan;
import com.cv4j.proxy.web.dto.ProxyDataDTO;
import com.cv4j.proxy.web.dto.QueryProxyDTO;
import com.mongodb.WriteResult;
import com.safframework.tony.common.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by tony on 2017/11/16.
 */
@Component
@Slf4j
public class ProxyDaoImpl implements ProxyDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean saveProxy(ProxyData proxyData) {
        proxyData.setLastSuccessfulTime(new Date().getTime());
        mongoTemplate.save(proxyData, Constant.COL_NAME_PROXY);
        return Preconditions.isNotBlank(proxyData.getId());
    }

    @Override
    public List<ProxyData> findProxyByCond(QueryProxyDTO queryProxyDTO, boolean isGetAll) {
        Query query = new Query();
        if(Preconditions.isNotBlank(queryProxyDTO.getType()) && !"all".equals(queryProxyDTO.getType())) {
            query.addCriteria(Criteria.where("proxyType").is(queryProxyDTO.getType()));
        }
        if(Preconditions.isNotBlank(queryProxyDTO.getIp())) {
            query.addCriteria(Criteria.where("proxyAddress").regex(".*?"+queryProxyDTO.getIp()+".*"));
        }
        if(queryProxyDTO.getMinPort() != null) {
            query.addCriteria(Criteria.where("proxyPort").gte(queryProxyDTO.getMinPort()).lte(queryProxyDTO.getMaxPort()));
        }
        if(!isGetAll) {
            if(Preconditions.isNotBlank(queryProxyDTO.getSort())) {
                if("asc".equals(queryProxyDTO.getOrder())) {
                    query.with(new Sort(Sort.Direction.ASC, queryProxyDTO.getSort()));
                } else {
                    query.with(new Sort(Sort.Direction.DESC, queryProxyDTO.getSort()));
                }
            } else {
                query.with(new Sort(Sort.Direction.DESC, "lastSuccessfulTime"));
                query.with(new Sort(Sort.Direction.ASC, "proxyPort"));
            }
            int skip = (queryProxyDTO.getPage() - 1) * queryProxyDTO.getRows();
            query.skip(skip);
            query.limit(queryProxyDTO.getRows());
        }
        return mongoTemplate.find(query, ProxyData.class, Constant.COL_NAME_PROXY);
    }

    @Override
    public List<ProxyDataDTO> findLimitProxy(int count) {
        Query query = new Query();
        query.limit(count);
        query.with(new Sort(Sort.Direction.DESC, "lastSuccessfulTime"));
        query.with(new Sort(Sort.Direction.ASC, "proxyPort"));
        return mongoTemplate.find(query, ProxyDataDTO.class,Constant.COL_NAME_PROXY);
    }

    @Override
    public boolean updateProxyById(String id) {

        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("lastSuccessfulTime", new Date().getTime());        //最近一次验证成功的时间
        WriteResult writeResult = mongoTemplate.updateFirst(query, update, ProxyData.class,Constant.COL_NAME_PROXY);
        return writeResult!=null && writeResult.getN() > 0;
    }

    @Override
    public boolean deleteProxyById(String id) {

        Query query = new Query(Criteria.where("id").is(id));
        WriteResult writeResult = mongoTemplate.remove(query, ProxyData.class, Constant.COL_NAME_PROXY);
        return writeResult!=null && writeResult.getN() > 0;
    }

    @Override
    public void deleteAll() {
        mongoTemplate.dropCollection(Constant.COL_NAME_PROXY);
    }

    @Override
    public Map<String, Class> getProxyMap() {

        Map<String, Class> proxyMap = new HashMap<>();
        List<ResourcePlan> list = mongoTemplate.findAll(ResourcePlan.class, Constant.COL_NAME_RESOURCE_PLAN);

        if (Preconditions.isNotBlank(list)) {

            for(ResourcePlan plan : list) {


                if(plan.getProxyResource().getPageCount() == 1) {
                    //如果pageCount=1，代表是单页面的资源
                    String key = plan.getProxyResource().getWebUrl();
                    try {
                        if(!proxyMap.containsKey(key)) {
                            proxyMap.put(key, Class.forName(plan.getProxyResource().getParser()));
                        }
                    } catch(ClassNotFoundException e) {
                        log.info("ClassNotFoundException = "+e.getMessage());
                    }
                } else {
                    for(int i=plan.getStartPageNum(); i<=plan.getEndPageNum(); i++) {

                        String key = plan.getProxyResource().getPrefix() + i + plan.getProxyResource().getSuffix();
                        try {
                            if(!proxyMap.containsKey(key)) {
                                proxyMap.put(key, Class.forName(plan.getProxyResource().getParser()));
                            }
                        } catch(ClassNotFoundException e) {
                            log.info("ClassNotFoundException = "+e.getMessage());
                        }
                    }
                }
            }
        }

        return proxyMap;
    }

    @Override
    public List<ProxyData> takeRandomTenProxy() {

        List<ProxyData> list = mongoTemplate.findAll(ProxyData.class);

        if (Preconditions.isNotBlank(list)) {

            Collections.shuffle(list);
            return list.size()>10?list.subList(0,10):list;
        } else {

            return new ArrayList<>();
        }
    }
}
