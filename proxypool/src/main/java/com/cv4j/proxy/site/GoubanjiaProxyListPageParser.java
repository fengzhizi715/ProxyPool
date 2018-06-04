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
 * http://www.goubanjia.com/
 */
@Slf4j
public class GoubanjiaProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("section[id=services] table tbody tr");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip_port = element.select("td:eq(0)").first().text(); //ip和port 用多个控件的值拼装为一个字符串
            String isAnonymous  = element.select("td:eq(1)").first().text();
            String type = element.select("td:eq(2)").first().text();
//            if(!anonymousFlag || isAnonymous.contains("匿")){
//                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
//            }
        }
        return proxyList;
    }
}
