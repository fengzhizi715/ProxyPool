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
 * https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-1
 */
@Slf4j
public class ProxyListPlusProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div[id=page] table[class=bg] tbody tr:gt(1)");
        List<Proxy> proxyList = new ArrayList<>();
        for (Element element : elements){
            String ip = element.select("td:eq(1)").first().text();
            String port  = element.select("td:eq(2)").first().text();
            String isAnonymous = element.select("td:eq(3)").first().text();
            String type = element.select("td:eq(6)").first().text() == "yes" ? "https":"http";
            if(!anonymousFlag || isAnonymous.contains("匿") || isAnonymous.contains("anonymous")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
