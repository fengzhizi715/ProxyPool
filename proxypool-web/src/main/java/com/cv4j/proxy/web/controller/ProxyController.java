package com.cv4j.proxy.web.controller;

import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.http.HttpManager;
import com.cv4j.proxy.web.aop.annotation.WebLog;
import com.cv4j.proxy.web.dao.ProxyDao;
import com.cv4j.proxy.web.domain.ProxyData;
import com.cv4j.proxy.web.dto.PageResult;
import com.cv4j.proxy.web.dto.QueryProxy;
import com.cv4j.proxy.web.dto.QueryProxyDTO;
import com.cv4j.proxy.web.dto.ResultMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProxyController {

    @Autowired
    private ProxyDao proxyDao;

    @WebLog
    @RequestMapping(value="/proxyController/doValidateProxy")
    @ResponseBody
    public ProxyData doValidateProxy(String id, String proxyType, String proxyIp, Integer proxyPort) {

        HttpHost httpHost = new HttpHost(proxyIp, proxyPort, proxyType);
        ProxyData proxyDate = new ProxyData();

        if(HttpManager.get().checkProxy(httpHost)) {
            proxyDate.setProxyType(proxyType);
            proxyDate.setProxyAddress(proxyIp);
            proxyDate.setProxyPort(proxyPort);
            proxyDao.updateProxyById(id);            //更新最后验证时间
        } else {
            proxyDao.deleteProxyById(id);            //物理删除数据
        }

        return proxyDate;
    }

    @WebLog
    @RequestMapping(value="/proxyController/queryProxy")
    @ResponseBody
    public Map<String,Object> queryProxy(String proxyType, String proxyIp, Integer minPort, Integer maxPort, String sort, String order, Integer page, Integer rows) {

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

        List<ProxyData> resultAll = proxyDao.findProxyByCond(queryProxyDTO,true);
        List<ProxyData> resultPage = proxyDao.findProxyByCond(queryProxyDTO,false);

        resultMap.put("total",resultAll!=null?resultAll.size():0);
        resultMap.put("rows",resultPage);

        return resultMap;
    }

    @WebLog
    @RequestMapping(value="/proxyController/proxys/{type}/{ip}/{minport}/{maxport}", method= RequestMethod.GET)
    @ResponseBody
    public PageResult<ProxyData> proxys(@PathVariable String type, @PathVariable String ip, @PathVariable Integer minport, @PathVariable Integer maxport, Integer page, Integer limit) {
        log.info("proxy info:"+type+","+ip+","+minport+","+maxport);

        QueryProxyDTO queryProxyDTO = new QueryProxyDTO();
        queryProxyDTO.setType(type);
        queryProxyDTO.setIp(".".equals(ip) ? "all" : ip);
        queryProxyDTO.setMinPort(minport);
        queryProxyDTO.setMaxPort(maxport);
        queryProxyDTO.setPage(page);
        queryProxyDTO.setRows(limit);

        List<ProxyData> resultAll = proxyDao.findProxyByCond(queryProxyDTO,true);
        List<ProxyData> resultPage = proxyDao.findProxyByCond(queryProxyDTO,false);

        PageResult<ProxyData> pageResult = new PageResult<>();
        pageResult.setData(resultPage);
        pageResult.setCount(resultAll.size());

        return pageResult;
    }

    @WebLog
    @RequestMapping(value="/proxyController/checkproxy/{type}/{ip}/{port}")
    @ResponseBody
    public ResultMap checkproxy(@PathVariable String type, @PathVariable String ip, @PathVariable Integer port) {
        log.info("checkproxy:"+type+"://"+ip+":"+port);
        HttpHost httpHost = new HttpHost(ip, port, type);
        if(HttpManager.get().checkProxy(httpHost)){
            return ResultMap.ok();
        } else {
            return ResultMap.failure();
        }
    }

}