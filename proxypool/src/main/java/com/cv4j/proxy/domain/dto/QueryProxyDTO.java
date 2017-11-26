package com.cv4j.proxy.domain.dto;


import com.cv4j.proxy.domain.Proxy;
import lombok.Data;

/**
 * 用于查询代理信息的条件对象
 */
@Data
public class QueryProxyDTO extends Proxy {

    private Integer minPort;  //最小端口值
    private Integer maxPort;  //最大端口值
    private String sort;      //排序字段
    private String order;     //排序模式
}