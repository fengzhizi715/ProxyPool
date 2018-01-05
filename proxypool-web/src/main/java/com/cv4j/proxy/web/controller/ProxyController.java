package com.cv4j.proxy.web.controller;

import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.http.HttpManager;
import com.cv4j.proxy.web.dao.ProxyDao;
import com.cv4j.proxy.web.dto.QueryProxyDTO;
import com.cv4j.proxy.web.dto.ResultProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProxyController {

    @Autowired
    private ProxyDao proxyDao;

    @RequestMapping(value="/proxyController/doValidateProxy")
    @ResponseBody
    public Proxy doValidateProxy(String id, String proxyType, String proxyIp, Integer proxyPort) {
        log.info("doValidateProxy id="+id+",proxyType="+proxyType+",proxyIp="+proxyIp+",proxyPort="+proxyPort);
        HttpHost httpHost = new HttpHost(proxyIp, proxyPort, proxyType);
        Proxy proxy = new Proxy();

        if(HttpManager.get().checkProxy(httpHost)) {
            proxy.setType(proxyType);
            proxy.setIp(proxyIp);
            proxy.setPort(proxyPort);
            proxyDao.updateProxyById(id);            //更新最后验证时间
        } else {
            proxyDao.deleteProxyById(id);            //物理删除数据
        }

        return proxy;
    }

    @RequestMapping(value="/proxyController/queryProxy")
    @ResponseBody
    public Map<String,Object> queryProxy(String proxyType, String proxyIp, Integer minPort, Integer maxPort, String sort, String order, Integer page, Integer rows) {
        log.info("queryProxy proxyType="+proxyType+",proxyIp="+proxyIp+",minPort="+minPort+",maxPort="+maxPort+",sort="+sort+",order="+order+",page="+page+",rows="+rows);
        Map<String,Object> resultMap = new HashMap<>();

        QueryProxyDTO queryProxyDTO = new QueryProxyDTO();
        queryProxyDTO.setType(proxyType);
        queryProxyDTO.setIp(proxyIp);
        queryProxyDTO.setMinPort(minPort == null ? 0 : minPort);
        queryProxyDTO.setMaxPort(maxPort == null ? 65535 : maxPort);
        queryProxyDTO.setSort(sort);
        queryProxyDTO.setOrder(order);
        queryProxyDTO.setPage(page <=0 ? 1 : page);
        queryProxyDTO.setRows(rows == 10 ? 15 : rows);

        List<Proxy> resultAll = proxyDao.findProxyByCond(queryProxyDTO,true);
        List<Proxy> resultPage = proxyDao.findProxyByCond(queryProxyDTO,false);

        resultMap.put("total",resultAll!=null?resultAll.size():0);
        resultMap.put("rows",resultPage);

        return resultMap;
    }

    @RequestMapping(value="/getAllResultProxy")
    @ResponseBody
    public List<ResultProxy> getAllResultProxy() {
        log.info("getAllResultProxy");
        List<ResultProxy> result = proxyDao.findAllProxy();
        return result!=null?result:new ArrayList<>();
    }

}