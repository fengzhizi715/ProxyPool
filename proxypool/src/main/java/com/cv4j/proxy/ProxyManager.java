package com.cv4j.proxy;

import com.cv4j.proxy.domain.Page;
import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.http.HttpManager;
import com.cv4j.proxy.task.ProxyPageCallable;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.reactivestreams.Publisher;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by tony on 2017/10/25.
 */
@Slf4j
public class ProxyManager {

    private ProxyManager() {
    }

    public static ProxyManager get() {
        return ProxyManager.Holder.PROXY_HTTP_CLIENT;
    }

    private static class Holder {
        private static final ProxyManager PROXY_HTTP_CLIENT = new ProxyManager();
    }

    /**
     * 抓取代理，成功的代理存放到ProxyPool中
     */
    public void start() {

        Flowable.fromIterable(ProxyPool.proxyMap.keySet())
                .parallel()
                .map(new Function<String, List<Proxy>>() {

                    @Override
                    public List<Proxy> apply(String s) throws Exception {

                        try {
                            return new ProxyPageCallable(s).call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                })
                .flatMap(new Function<List<Proxy>, Publisher<Proxy>>() {
                    @Override
                    public Publisher<Proxy> apply(List<Proxy> proxies) throws Exception {

                        if (proxies == null) return null;

                        List<Proxy> result = proxies
                                .stream()
                                .parallel()
                                .filter(new Predicate<Proxy>() {
                            @Override
                            public boolean test(Proxy proxy) {

                                HttpHost httpHost = new HttpHost(proxy.getIp(), proxy.getPort());
                                return HttpManager.get().checkProxy(httpHost);
                            }
                        }).collect(Collectors.toList());

                        return Flowable.fromIterable(result);
                    }
                })
                .sequential()
                .subscribe(new Consumer<Proxy>() {
                    @Override
                    public void accept(Proxy proxy) throws Exception {

                        ProxyPool.proxyList.add(proxy);
                    }
                });
    }

    public Page getWebPage(String url) throws IOException {
        return getWebPage(url, "UTF-8");
    }

    public Page getWebPage(String url, String charset) throws IOException {
        Page page = new Page();
        CloseableHttpResponse response = HttpManager.get().getResponse(url);
        if (response!=null) {
            page.setStatusCode(response.getStatusLine().getStatusCode());
            page.setUrl(url);
            try {
                if(page.getStatusCode() == 200){
                    page.setHtml(EntityUtils.toString(response.getEntity(), charset));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return page;
    }
}
