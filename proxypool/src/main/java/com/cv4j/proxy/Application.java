package com.cv4j.proxy;

import com.cv4j.proxy.domain.Proxy;
import com.safframework.tony.common.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cv4j.proxy.dao.ProxyDao;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tony on 2017/11/16.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application implements CommandLineRunner{

    @Autowired
    ProxyDao proxyDao;

    @Autowired
    ProxyManager proxyManager;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        proxyManager.start();
//
//        CopyOnWriteArrayList<Proxy> list = ProxyPool.proxyList;
//
//        if (Preconditions.isNotBlank(list)) {
//
//            for (Proxy p:list) {
//
//                proxyDao.saveProxy(p);
//            }
//        }
    }
}
