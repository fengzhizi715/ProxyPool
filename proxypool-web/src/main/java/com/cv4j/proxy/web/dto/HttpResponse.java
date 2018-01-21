package com.cv4j.proxy.web.dto;

import lombok.Data;

/**
 * Created by tony on 2018/1/21.
 */
@Data
public class HttpResponse<T> {

    private int code;

    private String message;

    private T data;
}
