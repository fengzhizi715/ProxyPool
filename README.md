# ProxyPool


获取可用代理池的库，从网上抓取的代理很多都是不可用的。这个库先用爬虫抓取代理，再做一些检查是否可用，可用的话就存放到mongodb中。

ProxyPool可以供给网络爬虫使用，ProxyPool由Spring Boot+RxJava2.x+MongoDB搭建


# 使用方法：

可以将该repository clone下来运行，可用的代理会存放到MongoDB中。每隔几小时会抓取一次可用的代理。不过本地需要事先搭建好MongoDB。

本地访问地址：http://localhost:8080/load?pagename=proxy_list

预览效果如下：

![](proxy_list.png)