package com.cv4j.proxy.job;

import com.cv4j.proxy.ProxyManager;
import com.cv4j.proxy.ProxyPool;
import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import com.safframework.tony.common.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
     * 每六个小时跑一次任务
     */
    @Scheduled(cron="${cronJob.schedule}")
    public void cronJob() {
        System.out.println("Job Start...");

        proxyManager.start();

        CopyOnWriteArrayList<Proxy> list = ProxyPool.proxyList;

        // 先删除旧的数据
        proxyDao.deleteAll();

        // 然后再进行插入新的proxy
        if (Preconditions.isNotBlank(list)) {

            for (Proxy p:list) {

                proxyDao.saveProxy(p);
            }
        }

        System.out.println("Job End...");
    }
}
