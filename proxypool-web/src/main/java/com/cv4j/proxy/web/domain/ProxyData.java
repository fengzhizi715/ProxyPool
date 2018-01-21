package com.cv4j.proxy.web.domain;

import com.cv4j.proxy.web.config.Constant;
import lombok.Data;
import org.apache.http.HttpHost;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Document(collection = Constant.COL_NAME_PROXY)
@Data
public class ProxyData {

    @Id
    private String id;
    private String proxyType;
    private String proxyAddress;
    private int proxyPort;
    private long lastSuccessfulTime;

    public String getProxyStr() {
        return proxyType + "://" + proxyAddress + ":" + proxyPort;
    }

}
