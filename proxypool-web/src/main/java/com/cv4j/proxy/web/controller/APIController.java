package com.cv4j.proxy.web.controller;

import com.cv4j.proxy.web.dao.ProxyDao;
import com.cv4j.proxy.web.dto.ProxyData;
import com.cv4j.proxy.web.dto.ResultProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class APIController {

    @Autowired
    private ProxyDao proxyDao;

    @RequestMapping(value="/proxys/{count}", method = RequestMethod.GET)
    public ProxyData getProxyData(@PathVariable int count) {
        log.info("getResultProxy, count="+count);
        ProxyData proxyData = new ProxyData();

        List<ResultProxy> result = proxyDao.findLimitProxy(count);
        if(result == null) {
            proxyData.setCode(404);
            proxyData.setMessage("无法返回有效数据");
            proxyData.setData(new ArrayList<>());
        } else {
            proxyData.setCode(200);
            proxyData.setMessage("成功返回有效数据");
            proxyData.setData(result);
        }

        return proxyData;
    }

}