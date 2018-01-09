package com.cv4j.proxy.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProxyData {

    private int code;

    private String message;

    private List<ResultProxy> data;

}
