package com.cv4j.proxy.config;

import com.safframework.tony.common.utils.Preconditions;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tony on 2017/10/19.
 */
public class Constant {

    /**
     * 单个ip请求间隔，单位ms
     */
    public final static long TIME_INTERVAL = 1000;

    private final static String[] defaultUAArray = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36"
    };

    private static List<String> uas = new CopyOnWriteArrayList<>();

    public static void setUas(List<String> uaList) {

        if (Preconditions.isNotBlank(uaList)) {
            uas = uaList;
        }
    }

    public static String getUserAgent() {

        if (Preconditions.isNotBlank(uas)) {

            int index=(int)(Math.random() * uas.size());
            return uas.get(index);
        } else {

            return Constant.defaultUAArray[new Random().nextInt(Constant.defaultUAArray.length)];
        }
    }
}
