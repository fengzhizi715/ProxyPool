package com.cv4j.proxy.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultProxyDataDTO {

    private int code;

    private String message;

    private List<ProxyDataDTO> data;

}
