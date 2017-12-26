package com.cv4j.proxy.web.controller;

import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.domain.dto.QueryProxyDTO;
import com.cv4j.proxy.domain.dto.ResultProxy;
import com.cv4j.proxy.http.HttpManager;
import com.cv4j.proxy.web.job.ScheduleJobs;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProxyController {

    @Autowired
    private ProxyDao proxyDao;

    @Autowired
    private ScheduleJobs scheduleJobs;

    @RequestMapping(value="/load")
    public String load(String pagename) {
        log.info("load, pagename="+pagename);
        return pagename;
    }

    @RequestMapping(value="/mload")
    public String mload(String pagename) {
        log.info("mload, pagename="+pagename);
        return pagename;
    }

    @RequestMapping(value="/proxyController/doValidateProxy")
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
        if(resultAll == null) {
            log.info("queryProxy, resultAll = null");
        } else{
            log.info("queryProxy, resultAll = "+resultAll.size());
        }
        resultMap.put("total",resultAll.size());
        resultMap.put("rows",resultPage);

        return resultMap;
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

    @RequestMapping(value="/startJob")
    @ResponseBody
    public void startJob(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        log.info("manual startJob");
        try {
            httpServletResponse.setContentType("text/plain; charset=utf-8");
            ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
            if(ScheduleJobs.IS_JOB_RUNNING) {
                responseOutputStream.write("Job正在运行。。。".getBytes("utf-8"));
                responseOutputStream.flush();
                responseOutputStream.close();
            } else {
                log.info("scheduleJobs.cronJob() start by controller...");
                scheduleJobs.cronJob();
            }
        } catch (Exception e) {
            log.info("startJob exception e="+e.getMessage());
        }
    }

}