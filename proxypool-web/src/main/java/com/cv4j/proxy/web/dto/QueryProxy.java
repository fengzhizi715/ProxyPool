package com.cv4j.proxy.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于查询代理信息的条件对象
 */
@Setter
@Getter
public class QueryProxy {

    private String ipType = "all";
    private String ipAddress = "";
    private Integer minPort = 0;
    private Integer maxPort = 65535;
}