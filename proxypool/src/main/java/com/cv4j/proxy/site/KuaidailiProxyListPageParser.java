package com.cv4j.proxy.site;

import com.cv4j.proxy.ProxyListPageParser;
import com.cv4j.proxy.domain.Proxy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * https://www.kuaidaili.com/
 */
@Slf4j
public class KuaidailiProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<Proxy> parse(String content) {
        //TODO 网站挂掉，打不开
        return null;
    }
}
