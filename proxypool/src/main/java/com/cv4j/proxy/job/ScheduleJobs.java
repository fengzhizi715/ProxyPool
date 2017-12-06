package com.cv4j.proxy.job;

import com.cv4j.proxy.ProxyManager;
import com.cv4j.proxy.ProxyPool;
import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import com.safframework.tony.common.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    ProxyManager proxyManager;

    /**
     * 每隔几个小时跑一次任务
     */
    @Scheduled(cron="${cronJob.schedule}")
    public void cronJob() {
        if(IS_JOB_RUNNING) return;
        log.info("Job Start...");
        IS_JOB_RUNNING = true;

        // 跑任务之前先清空proxyList中的数据
        ProxyPool.proxyList.clear();

        proxyManager.start();

        CopyOnWriteArrayList<Proxy> list = ProxyPool.proxyList;

        if (Preconditions.isNotBlank(list)) {
            // 先删除旧的数据
            proxyDao.deleteAll();
            log.info("Job after deleteAll");

            // 然后再进行插入新的proxy
            log.info("Job before saveProxy");
            for (Proxy p:list) {
                proxyDao.saveProxy(p);
                log.info("saveProxy p="+p.getType()+"://"+p.getIp()+":"+p.getPort());
            }
        } else {

            log.info("proxyList is empty...");
        }

        log.info("Job End...");
        IS_JOB_RUNNING = false;
    }
}
