package com.cv4j.proxy.domain.dto;


import com.cv4j.proxy.domain.Proxy;

/**
 * 用于查询代理信息的条件对象
 */
public class QueryProxyDTO extends Proxy {

    private Integer minPort;  //最小端口值
    private Integer maxPort;  //最大端口值
    private String sort;      //排序字段
    private String order;     //排序模式

    public Integer getMinPort() {
        return minPort;
    }

    public void setMinPort(Integer minPort) {
        this.minPort = minPort;
    }

    public Integer getMaxPort() {
        return maxPort;
    }

    public void setMaxPort(Integer maxPort) {
        this.maxPort = maxPort;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}