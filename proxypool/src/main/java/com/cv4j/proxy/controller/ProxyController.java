package com.cv4j.proxy.controller;

import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.domain.dto.QueryProxyDTO;
import com.cv4j.proxy.domain.dto.ResultProxy;
import com.cv4j.proxy.http.HttpManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class ProxyController {

    @Autowired
    private ProxyDao proxyDao;

    @RequestMapping(value="/load")
    public String loadPage(String pagename) {
        log.info("loadPage, pagename="+pagename);
        return pagename;
    }

    @RequestMapping(value="proxyController/doValidateProxy")
    @ResponseBody
    public Proxy doValidateProxy(String id, String proxyType, String proxyIp, Integer proxyPort) {
        log.info("doValidateProxy id="+id+",proxyType="+proxyType+",proxyIp="+proxyIp+",proxyPort="+proxyPort);
        HttpHost httpHost = new HttpHost(proxyIp, proxyPort, proxyType);
        boolean validateResult = HttpManager.get().checkProxy(httpHost);
        Proxy proxy = new Proxy();
        if(validateResult) {
            proxy.setType(proxyType);
            proxy.setIp(proxyIp);
            proxy.setPort(proxyPort);
            proxyDao.updateProxyById(id);            //更新最后验证时间
        } else {
            proxyDao.deleteProxyById(id);            //物理删除数据
        }

        return proxy;
    }

    @RequestMapping(value="proxyController/queryProxy")
    @ResponseBody
    public List<Proxy> queryProxy(String proxyType, String proxyIp, Integer minPort, Integer maxPort, String sort, String order) {
        log.info("queryProxy proxyType="+proxyType+",proxyIp="+proxyIp+",minPort="+minPort+",maxPort="+maxPort+",sort="+sort+",order="+order);
        QueryProxyDTO queryProxyDTO = new QueryProxyDTO();
        queryProxyDTO.setType(proxyType);
        queryProxyDTO.setIp(proxyIp);
        queryProxyDTO.setMinPort(minPort == null ? 0 : minPort);
        queryProxyDTO.setMaxPort(maxPort == null ? 65535 : maxPort);
        queryProxyDTO.setSort(sort);
        queryProxyDTO.setOrder(order);

        List<Proxy> result = proxyDao.findProxyByCond(queryProxyDTO);
        if(result == null) {
            log.info("queryProxy, result = null");
            return null;
        } else{
            log.info("queryProxy, result = "+result.size());
            return result;
        }
    }

    @RequestMapping(value="/getAllResultProxy")
    @ResponseBody
    public List<ResultProxy> getAllResultProxy() {
        log.info("getAllResultProxy");
        List<ResultProxy> result = proxyDao.findAllProxy();
        if(result == null) {
            log.info("getAllResultProxy, result = null");
            return null;
        } else{
            log.info("getAllResultProxy, result = "+result.size());
            return result;
        }
    }

}