package com.cv4j.proxy.web.job;

import com.cv4j.proxy.ProxyManager;
import com.cv4j.proxy.ProxyPool;
import com.cv4j.proxy.dao.LogDao;
import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.domain.dto.JobLogDTO;
import com.safframework.tony.common.utils.JodaUtils;
import com.safframework.tony.common.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tony on 2017/11/22.
 */
@Component
@Slf4j
public class ScheduleJobs {

    public static boolean IS_JOB_RUNNING = false;

    @Autowired
    ProxyDao proxyDao;

    @Autowired
    LogDao logDao;

    @Autowired
    ProxyManager proxyManager;

    /**
     * 每隔几个小时跑一次任务
     */
    @Scheduled(cron="${cronJob.schedule}")
    public void cronJob() {

        if(IS_JOB_RUNNING) return;

        log.info("Job Start...");

        IS_JOB_RUNNING = true;

        JobLogDTO jobLogDTO = new JobLogDTO();
        jobLogDTO.setJobName("ScheduleJobs.cronJob");
        jobLogDTO.setStartTime(JodaUtils.formatDateTime(new Date()));

        // 跑任务之前先清空proxyList中的数据
        ProxyPool.proxyList.clear();

        proxyManager.start();

        CopyOnWriteArrayList<Proxy> list = ProxyPool.proxyList;

        if (Preconditions.isNotBlank(list)) {
            // 先删除旧的数据
            proxyDao.deleteAll();
            log.info("Job after deleteAll");

            // 然后再进行插入新的proxy

            List<String> ipList = new ArrayList<String>();
            for (Proxy p:list) {
                proxyDao.saveProxy(p);
                ipList.add(p.getIp());
                log.info("Job saveProxy = "+p.getProxyStr());
            }

            jobLogDTO.setIpList(ipList);
            jobLogDTO.setResultDesc(String.format("成功保存了%s条代理IP数据", list.size()));
            jobLogDTO.setEndTime(JodaUtils.formatDateTime(new Date()));
            logDao.saveJobLog(jobLogDTO);

        } else {
            log.info("proxyList is empty...");
        }

        IS_JOB_RUNNING = false;
        ProxyPool.proxyList.clear();

        log.info("Job End...");
    }
}
