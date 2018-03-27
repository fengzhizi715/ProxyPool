package com.cv4j.proxy.web.dto;

import lombok.Data;

/**
 * 用于查询代理信息的条件对象
 */
@Data
public class QueryProxyDTO {

    private String ip = "";
    private String type = "all";      //http、https
    private Integer minPort = 0;  //最小端口值
    private Integer maxPort = 65535;  //最大端口值
    private String sort = "";      //排序字段
    private String order = "asc";     //排序模式
    private Integer page;     //这次显示第几页
    private Integer rows;     //这页显示多少行数据
}