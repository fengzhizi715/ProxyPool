package com.cv4j.proxy.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by tony on 2017/10/19.
 */
@Getter
@Setter
public class Page {

    private String url;
    private int statusCode;//响应状态码
    private String html;//response content

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return url.equals(page.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
