package com.cv4j.proxy.site.ip181;


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
 * Created by tony on 2017/10/19.
 */
@Slf4j
public class Ip181ProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<Proxy> parse(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.select("table tr:gt(0)");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String isAnonymous = element.select("td:eq(2)").first().text();
            String type = element.select("td:eq(3)").first().text();   //可能是 HTTP,HTTPS
            type = type.split(",").length > 1 ? type.split(",")[0] : type;
            log.debug("parse result = "+type+"://"+ip+":"+port+"  "+isAnonymous);
//            if(!anonymousFlag || isAnonymous.contains("匿")){  //TODO 编码问题不解决的话，isAnonymous是乱码
                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
//            }
        }
        return proxyList;
    }
}
