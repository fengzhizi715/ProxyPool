# ProxyPool

[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
 [ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/proxypool/images/download.svg) ](https://bintray.com/fengzhizi715/maven/proxypool/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


获取可用代理池的库，从网上抓取的代理很多都是不可用的。这个库先用爬虫抓取代理，再做一些检查是否可用，可用的话就存放到mongodb中。

ProxyPool可以供给网络爬虫使用，ProxyPool由Spring Boot+RxJava2.x+MongoDB搭建


# 使用方法：
## 单独使用代理抓取逻辑，无任何界面，可以在任何项目中使用

对于Java项目如果使用gradle构建，由于默认不是使用jcenter，需要在相应module的build.gradle中配置

```groovy
repositories {
    mavenCentral()
    jcenter()
}
```

Gradle:

```groovy
compile 'com.cv4j.proxy:proxypool:1.1.0'
```


## 也可以将该repository clone独立运行，带web界面

可用的代理会存放到MongoDB中。每隔几小时会抓取一次可用的代理。不过本地需要事先搭建好MongoDB。

本地访问地址：http://localhost:8080/proxypool/load?pagename=proxy_list

线上环境地址：http://47.97.7.119:8080/proxypool/load?pagename=proxy_list

预览效果如下：

![](proxy_list.png)


另外，还提供了一个接口，返回代理池中所有的Proxy

本地访问地址：http://localhost:8080/proxypool/getAllResultProxy

线上环境地址：http://47.97.7.119:8080/proxypool/getAllResultProxy