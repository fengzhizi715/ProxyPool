package com.cv4j.proxy.web.controller;

import com.cv4j.proxy.web.dao.ProxyResourceDao;
import com.cv4j.proxy.web.domain.ProxyResource;
import com.cv4j.proxy.web.domain.ResourcePlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ProxyResourceController {

    @Autowired
    private ProxyResourceDao proxyResourceDao;

    @RequestMapping(value="/proxyResourceController/getAllResource")
    @ResponseBody
    public List<ProxyResource> getAllResource() {
        log.info("getAllResource");
        List<ProxyResource> result = proxyResourceDao.findAllProxyResource();
        return result!=null?result:new ArrayList<>();
    }

    @RequestMapping(value="/proxyResourceController/doSaveResource", method= RequestMethod.POST)
    @ResponseBody
    public boolean doSaveResource(@RequestBody ProxyResource proxyResource) {
        boolean result = false;
        if(proxyResource != null) {
            log.info("doSaveResource getSuffix="+proxyResource.getSuffix());
            result = proxyResourceDao.saveProxyResource(proxyResource);
        }

        return result;
    }

    @RequestMapping(value="/proxyResourceController/doSaveResourcePlan", method= RequestMethod.POST)
    @ResponseBody
    public boolean doSaveResourcePlan(@RequestBody ResourcePlan resourcePlan) {
        boolean result = false;
        if(resourcePlan != null) {
            log.info("doSaveResourcePlan refResId="+resourcePlan.getProxyResource().getResId());
            result = proxyResourceDao.saveResourcePlan(resourcePlan);
        }
        return result;
    }

    @RequestMapping(value="/proxyResourceController/getAllResourcePlan")
    @ResponseBody
    public List<ResourcePlan> getAllResourcePlan() {
        log.info("getAllResourcePlan");
        List<ResourcePlan> result = proxyResourceDao.findAllResourcePlan();
        return result!=null?result:new ArrayList<>();
    }

    @RequestMapping(value="/proxyResourceController/doDeleteResourcePlan", method= RequestMethod.POST)
    @ResponseBody
    public boolean doDeleteResourcePlan(@RequestBody ResourcePlan resourcePlan) {
        boolean result = false;
        if(resourcePlan != null) {
            log.info("doDeleteResourcePlan id="+resourcePlan.getProxyResource().getId());
            result = proxyResourceDao.deleteResourcePlan(resourcePlan);
        }
        return result;
    }

}