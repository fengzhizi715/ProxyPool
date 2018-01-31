package com.cv4j.proxy.web.job;

import com.cv4j.proxy.ProxyManager;
import com.cv4j.proxy.ProxyPool;
import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.web.config.Constant;
import com.cv4j.proxy.web.dao.CommonDao;
import com.cv4j.proxy.web.dao.ProxyDao;
import com.cv4j.proxy.web.domain.JobLog;
import com.cv4j.proxy.web.domain.ProxyData;
import com.safframework.tony.common.utils.JodaUtils;
import com.safframework.tony.common.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tony on 2017/11/22.
 */
@Component
@Slf4j
public class ScheduleJobs {


    public final static int JOB_STATUS_INIT = 0;
    public final static int JOB_STATUS_RUNNING = 1;
    public final static int JOB_STATUS_STOPPED = 2;

    protected AtomicInteger stat = new AtomicInteger(JOB_STATUS_INIT);

    @Autowired
    ProxyDao proxyDao;

    @Autowired
    CommonDao commonDao;

    @Autowired
    CacheManager cacheManager;

    ProxyManager proxyManager = ProxyManager.get();

    /**
     * 每隔几个小时跑一次任务
     */
    @Scheduled(cron="${cronJob.schedule}")
    public void cronJob() {

        //1.检查job的状态
        checkRunningStat();

        log.info("Job Start...");

        //2.获取目标网页的Url
        ProxyPool.proxyMap = proxyDao.getProxyMap();

        //3.如果数据库里没取到，用默认内置的
        if(Preconditions.isBlank(ProxyPool.proxyMap)) {
            log.info("Job proxyDao.getProxyMap() is empty");
            ProxyPool.proxyMap = Constant.proxyMap;
        }

        //4.每次跑job先清空缓存中的内容
        if (cacheManager.getCache("proxys")!=null) {

            cacheManager.getCache("proxys").clear();
        }

        //5.创建一个日志对象，用于存储job的每次工作记录
        JobLog jobLog = new JobLog();
        jobLog.setJobName("ScheduleJobs.cronJob");
        jobLog.setStartTime(JodaUtils.formatDateTime(new Date()));

        //6.跑任务之前先清空proxyList中上一次job留下的proxy数据，
        ProxyPool.proxyList.clear();

        //7.从数据库中选取10个代理作为种子代理，遇到http 503时使用代理来抓数据
        ProxyPool.addProxyList(getProxyList(proxyDao.takeRandomTenProxy()));
        log.info("Job ProxyPool.proxyList size = "+ProxyPool.proxyList.size());
        //8.正式开始，爬代理数据
        proxyManager.start();

        //9.爬完以后，把数据转换为ProxyData并存到数据库
        CopyOnWriteArrayList<ProxyData> list = getProxyDataList(ProxyPool.proxyList);
        log.info("Job ProxyData list size = "+list.size());
        if (Preconditions.isNotBlank(list)) {

            // 10. list的数量<=15时，不删除数据库里的老数据
            if (list.size()>15) {
                proxyDao.deleteAll();
                log.info("Job after deleteAll");
            }

            //11. 然后再进行插入新的proxy
            for (ProxyData p:list) {
                proxyDao.saveProxy(p);
            }
            log.info("Job save count = "+list.size());

            jobLog.setResultDesc(String.format("success save count = %s", list.size()));
            jobLog.setEndTime(JodaUtils.formatDateTime(new Date()));
            commonDao.saveJobLog(jobLog);

        } else {
            log.info("Job proxyList is empty...");
        }

        //12. 设置job状态为停止
        stop();

        log.info("Job End...");
    }

    private void checkRunningStat() {
        while (true) {

            int statNow = getJobStatus();

            //如果已经在运行了，就抛出异常，结束循环
            if (statNow == JOB_STATUS_RUNNING) {
                throw new IllegalStateException("Job is already running!");
            }

            //如果还没在运行，就设置为运行状态，结束循环
            if (stat.compareAndSet(statNow, JOB_STATUS_RUNNING)) {
                break;
            }
        }
    }

    public int  getJobStatus() {

        return stat.get();
    }

    public void stop() {
        //状态从JOB_STATUS_RUNNING更新为JOB_STATUS_STOPPED，代表停止job
        stat.compareAndSet(JOB_STATUS_RUNNING, JOB_STATUS_STOPPED);
    }

    private List<Proxy> getProxyList(List<ProxyData> list) {
        List<Proxy> resultList = new ArrayList<>();

        Proxy proxy = null;
        for(ProxyData proxyData : list) {
            proxy = new Proxy();
            proxy.setType(proxyData.getProxyType());
            proxy.setIp(proxyData.getProxyAddress());
            proxy.setPort(proxyData.getProxyPort());

            resultList.add(proxy);
        }

        return resultList;
    }

    private CopyOnWriteArrayList<ProxyData> getProxyDataList(List<Proxy> list) {
        CopyOnWriteArrayList<ProxyData> resultList = new CopyOnWriteArrayList<>();

        ProxyData proxyData = null;
        for(Proxy proxy : list) {
            proxyData = new ProxyData();
            proxyData.setProxyType(proxy.getType());
            proxyData.setProxyAddress(proxy.getIp());
            proxyData.setProxyPort(proxy.getPort());

            resultList.add(proxyData);
        }

        return resultList;
    }
}
