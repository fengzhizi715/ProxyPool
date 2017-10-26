# ProxyPool


获取可用代理池的库，从网上抓取的代理很多都是不可用的。这个库先用爬虫抓取代理，再做一些检查是否可用，可用的话就存放到ProxyPool中。

该库可以给网络爬虫使用，底层用了RxJava实现并行操作。


# 使用方法：

```java
ProxyManager.get().start();
```

可用的代理都存放在ProxyPool.proxyList中。