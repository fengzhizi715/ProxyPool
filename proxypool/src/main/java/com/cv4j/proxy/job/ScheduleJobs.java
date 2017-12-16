package com.cv4j.proxy.job;

import com.cv4j.proxy.ProxyManager;
import com.cv4j.proxy.ProxyPool;
import com.cv4j.proxy.config.Constant;
import com.cv4j.proxy.dao.ProxyDao;
import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.domain.dto.JobLogDTO;
import com.safframework.tony.common.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    ProxyManager proxyManager;

    /**
     * 每隔几个小时跑一次任务
     */
    @Scheduled(cron="${cronJob.schedule}")
    public void cronJob() {
        if(IS_JOB_RUNNING) return;
        log.info("Job Start...");
        JobLogDTO jobLogDTO = new JobLogDTO();
        jobLogDTO.setJobName("ScheduleJobs.cronJob");
        jobLogDTO.setStartTime(Constant.getCurrentDateString());
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
            int count=0;
            List<String> ipList = new ArrayList<String>();
            for (Proxy p:list) {
                proxyDao.saveProxy(p);
                count++;
                ipList.add(p.getIp());
                log.info("Job saveProxy = "+p.getType()+"://"+p.getIp()+":"+p.getPort());
            }
            jobLogDTO.setIpList(ipList);
            jobLogDTO.setResultDesc("成功保存了"+count+"条代理IP数据");
            jobLogDTO.setEndTime(Constant.getCurrentDateString());
            proxyDao.saveJobLog(jobLogDTO);

        } else {
            log.info("proxyList is empty...");
        }

        log.info("Job End...");
        IS_JOB_RUNNING = false;
        ProxyPool.proxyList.clear();
    }
}
