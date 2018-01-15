package com.cv4j.proxy.web.job;

import com.cv4j.proxy.ProxyManager;
import com.cv4j.proxy.ProxyPool;
import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.web.config.Constant;
import com.cv4j.proxy.web.dao.CommonDao;
import com.cv4j.proxy.web.dao.ProxyDao;
import com.cv4j.proxy.web.domain.JobLog;
import com.safframework.tony.common.utils.JodaUtils;
import com.safframework.tony.common.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    ProxyManager proxyManager;

    /**
     * 每隔几个小时跑一次任务
     */
    @Scheduled(cron="${cronJob.schedule}")
    public void cronJob() {

        checkRunningStat();

        log.info("Job Start...");

        ProxyPool.proxyMap = proxyDao.getProxyMap();

        if(Preconditions.isBlank(ProxyPool.proxyMap)) {
            log.info("proxyDao.getProxyMap() is empty");
            ProxyPool.proxyMap = Constant.proxyMap;
        }

        JobLog jobLog = new JobLog();
        jobLog.setJobName("ScheduleJobs.cronJob");
        jobLog.setStartTime(JodaUtils.formatDateTime(new Date()));

        // 跑任务之前先清空proxyList中的数据
        ProxyPool.proxyList.clear();

        ProxyPool.addProxyList(proxyDao.takeRandomTenProxy());  // 从数据库中选取10个代理作为种子代理，遇到http 503时使用代理来抓数据

        proxyManager.start();

        CopyOnWriteArrayList<Proxy> list = ProxyPool.proxyList;

        if (Preconditions.isNotBlank(list)) {

            // list的数量<=15时，不删除原先的数据
            if (list.size()>15) {

                // 先删除旧的数据
                proxyDao.deleteAll();
                log.info("Job after deleteAll");
            }

            // 然后再进行插入新的proxy
            for (Proxy p:list) {
                proxyDao.saveProxy(p);
                log.info("Job saveProxy = "+p.getProxyStr());
            }

            jobLog.setResultDesc(String.format("成功保存了%s条代理IP数据", list.size()));
            jobLog.setEndTime(JodaUtils.formatDateTime(new Date()));
            commonDao.saveJobLog(jobLog);

        } else {
            log.info("proxyList is empty...");
        }

        stop();

        log.info("Job End...");
    }

    private void checkRunningStat() {
        while (true) {

            int statNow = getJobStatus();
            if (statNow == JOB_STATUS_RUNNING) {
                throw new IllegalStateException("Job is already running!");
            }

            if (stat.compareAndSet(statNow, JOB_STATUS_RUNNING)) {
                break;
            }
        }
    }

    public int  getJobStatus() {

        return stat.get();
    }

    public void stop() {

        stat.compareAndSet(JOB_STATUS_RUNNING, JOB_STATUS_STOPPED);
    }
}
