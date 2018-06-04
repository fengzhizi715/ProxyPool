package com.cv4j.proxy.site;

import com.cv4j.proxy.ProxyListPageParser;
import com.cv4j.proxy.config.Constant;
import com.cv4j.proxy.domain.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.data5u.com/
 */
@Slf4j
public class Data4uProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div[class=wlist] li:eq(1) ul");
        List<Proxy> proxyList = new ArrayList<>();
        int i = 0;
        for (Element element : elements){
            if(i == 0) {
                i++;
                continue;
            }

            String ip = element.select("span:eq(0)").first().text();
            String port  = element.select("span:eq(1) li").first().text();
            String isAnonymous = element.select("span:eq(2) li").first().text();
            String type = element.select("span:eq(3) li").first().text();
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
