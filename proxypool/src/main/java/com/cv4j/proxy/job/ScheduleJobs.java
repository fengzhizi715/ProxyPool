package com.cv4j.proxy.job;

import com.cv4j.proxy.ProxyManager;
import com.cv4j.proxy.ProxyPool;
import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import com.safframework.tony.common.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tony on 2017/11/22.
 */
@Component
public class ScheduleJobs {

    @Autowired
    ProxyDao proxyDao;

    @Autowired
    ProxyManager proxyManager;

    /**
     * 每两个小时跑一次任务
     */
    @Scheduled(cron = "0 0 */2 * * * ? ")
    public void cronJob() {
        System.out.println("[CronJob Execute]");

        proxyDao.deleteAll();

        proxyManager.start();

        CopyOnWriteArrayList<Proxy> list = ProxyPool.proxyList;

        if (Preconditions.isNotBlank(list)) {

            for (Proxy p:list) {

                proxyDao.saveProxy(p);
            }
        }
    }
}
