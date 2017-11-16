package com.cv4j.proxy.dao.impl;

import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Created by tony on 2017/11/16.
 */
@Component
public class ProxyDaoImpl implements ProxyDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveProxy(Proxy proxy) {
        mongoTemplate.save(proxy);
    }

    @Override
    public Proxy findProxyByIp(String ip) {
        Query query=new Query(Criteria.where("ip").is(ip));
        Proxy proxy =  mongoTemplate.findOne(query , Proxy.class);
        return proxy;
    }

    @Override
    public void deleteProxyById(Long id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,Proxy.class);
    }
}
