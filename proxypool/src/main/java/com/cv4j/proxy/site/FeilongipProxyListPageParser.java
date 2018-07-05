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
 * http://www.feilongip.com/
 */
@Slf4j
public class FeilongipProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div[id=j-tab-newprd] table tbody tr");
        List<Proxy> proxyList = new ArrayList<>();
        for (Element element : elements){
            String ip_port = element.select("td:eq(1)").first().text();
            String ip = ip_port.split(":")[0];
            String port  = ip_port.split(":")[1];

            String isAnonymous = element.select("td:eq(3)").first().text();
            String type = element.select("td:eq(4)").first().text();
            if(!anonymousFlag || isAnonymous.contains("匿") || isAnonymous.contains("anonymous")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
