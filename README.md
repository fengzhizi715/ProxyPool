# ProxyPool

[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
 [ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/proxypool/images/download.svg) ](https://bintray.com/fengzhizi715/maven/proxypool/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


+ ProxyPool的作用：从网络上获取免费可用的IP代理数据。先用爬虫程序抓取代理数据，再检查代理是否可用，可用的话就存放到数据库中。每隔一段时间重复执行这个过程。

+ ProxyPool的技术：Spring Boot+RxJava2.x+MongoDB等，前端：layUI+jquery 等

+ ProxyPool的概述：该项目有两个模块proxypool和proxypool-web，从网络上抓取数据的核心工作由proxypool模块完成，可以在site这个package下新增针对不同网页的解析类。proxypool-web模块是依赖proxypool模块实现的sample模块。

## 1. 使用方法
+ #### 单独使用ProxyPool项目中proxypool模块的抓取逻辑，它无任何界面，可用于任何项目，无侵入性

对于Java工程如果使用gradle构建，由于默认没有使用jcenter()，需要在相应module的build.gradle中配置
```groovy
repositories {
    mavenCentral()
    jcenter()
}
```
Gradle:

```groovy
compile 'com.cv4j.proxy:proxypool:1.1.11'
```

+ ####  clone到本地，运行proxypool-web模块，带界面
准备条件：

1）本地装好MongoDB数据库

2）proxypool-web模块下的application.properties，参考配置如下：
```
spring.data.mongodb.uri=mongodb://localhost:27017/proxypool
spring.data.mongodb.uri=mongodb://username:password@localhost:27017/proxypool （有账号密码）
```
3）创建database和collection
```
database:proxypool
collection:Proxy_Resource、Resource_Plan、Proxy、Job_Log、Sys_Sequence
```
4）collection中的默认数据
Proxy_Resource:
```
{
    "_id" : ObjectId("5a48578737a340d5c48a84af"),
    "_class" : "com.cv4j.proxy.web.dto.ProxyResource",
    "resId" : 1,
    "webName" : "西刺国内高匿代理",
    "webUrl" : "http://www.xicidaili.com/nn/1.html",
    "pageCount" : 100,
    "prefix" : "http://www.xicidaili.com/nn/",
    "suffix" : ".html",
    "parser" : "com.cv4j.proxy.site.xicidaili.XicidailiProxyListPageParser",
    "addTime" : NumberLong(1515114009516),
    "modTime" : NumberLong(1515114009516)
}
```
Sys_Sequence:
```
{
    "_id" : ObjectId("5a4f2baf87ccb25df57b096b"),
    "colName" : "Proxy_Resource",
    "sequence" : 2
}
```

5）运行
按照SpringBoot项目的方式运行程序，访问web的地址如下：
+ 解析资源
http://{host}:{port}/proxypool/resourcelist

+ 依赖资源，当前job的要抓取的目标网页
http://{host}:{port}/proxypool/planlist

+ 查看抓取到本地的代理数据
http://{host}:{port}/proxypool/proxylist (用easyUI框架开发的界面)
http://{host}:{port}/proxypool/proxys   (用layUI框架开发的界面)

+ 获取数据库里当前可用的代理数据
http://{host}:{port}/proxypool/proxys/{count}

6）定时抓取数据的job
proxypool-web模块下目前配置了一个每隔三小时自动运行的job：

```
com.cv4j.proxy.web.job.ScheduleJobs.cronJob()
```

```
application.properties: cronJob.schedule = 0 0 0/3 * * ?s
```

## 2. 免费的在线演示:
 
+ 实时最新的免费代理资源：
http://47.97.7.119:8080/proxypool/proxylist

+ 管理代理资源：
http://47.97.7.119:8080/proxypool/resourcelist

+ Job运行依赖的数据：
http://47.97.7.119:8080/proxypool/planlist

+ 返回Json格式的代理数据：
http://47.97.7.119:8080/proxypool/proxys/{count}
类型：GET,参数：count代表计划获取的代理IP数据条数

## 3. 专业的爬虫
笔者开发的专业的爬虫框架:
[NetDiscovery](https://github.com/fengzhizi715/NetDiscovery)

## 4. 联系方式: 

Wechat：fengzhizi715


> Java与Android技术栈：每周更新推送原创技术文章，欢迎扫描下方的公众号二维码并关注，期待与您的共同成长和进步。

![](https://user-gold-cdn.xitu.io/2018/7/24/164cc729c7c69ac1?w=344&h=344&f=jpeg&s=9082)

License
-------

    Copyright (C) 2017 - present, Tony Shen.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
