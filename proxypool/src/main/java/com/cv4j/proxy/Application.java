package com.cv4j.proxy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by tony on 2017/11/16.
 */
@SpringBootApplication
public class Application implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ProxyManager.get().start();
    }
}
