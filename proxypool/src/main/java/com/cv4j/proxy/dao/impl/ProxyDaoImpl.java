package com.cv4j.proxy.dao.impl;

import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.domain.dto.QueryProxyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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
    public List<Proxy> findProxyByCond(QueryProxyDTO queryProxyDTO) {
        Query query = new Query();
        if(queryProxyDTO.getType() != null && !"".equals(queryProxyDTO.getType())) {
            query.addCriteria(Criteria.where("type").gte(queryProxyDTO.getType()));
        }
        if(queryProxyDTO.getIp() != null && !"".equals(queryProxyDTO.getIp())) {
            query.addCriteria(Criteria.where("ip").regex(".*?"+queryProxyDTO.getIp()+".*"));
        }
        if(queryProxyDTO.getMinPort() != null) {
            query.addCriteria(Criteria.where("port").gte(queryProxyDTO.getMinPort()).lte(queryProxyDTO.getMaxPort()));
        }
        if(queryProxyDTO.getSort() != null) {
            if("asc".equals(queryProxyDTO.getOrder())) {
                query.with(new Sort(Sort.Direction.ASC, queryProxyDTO.getSort()));
            } else {
                query.with(new Sort(Sort.Direction.DESC, queryProxyDTO.getSort()));
            }
        } else {
            query.with(new Sort(Sort.Direction.ASC, "port"));
        }
        return mongoTemplate.find(query, Proxy.class,"Proxy");
    }

    @Override
    public void updateProxyById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("lastSuccessfulTime", new Date().getTime());        //最近一次验证成功的时间
        mongoTemplate.findAndModify(query, update, Proxy.class,"Proxy");
    }

    @Override
    public void deleteProxyById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Proxy.class, "Proxy");
    }
}
