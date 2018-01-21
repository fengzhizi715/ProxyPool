package com.cv4j.proxy.domain;

import org.apache.http.HttpHost;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by tony on 2017/10/19.
 */
public class Proxy implements Delayed, Serializable {

    private static final long serialVersionUID = -8788193271053510562L;

    private long timeInterval ;//任务间隔时间,单位ms

    private String id;

    private String ip;
    private int port;
    private String type;      //http、https
    private boolean availableFlag;
    private boolean anonymousFlag;
    private long lastSuccessfulTime;//最近一次请求成功时间
    private long successfulTotalTime;//请求成功总耗时
    private int failureTimes;//请求失败次数
    private int successfulTimes;//请求成功次数
    private double successfulAverageTime;//成功请求平均耗时

    public Proxy() {}

    public Proxy(String ip, int port, long timeInterval) {
        this.ip = ip;
        this.port = port;
        this.type = "http";
        this.timeInterval = timeInterval;
        this.timeInterval = TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    public Proxy(String ip, int port, String type, long timeInterval) {
        this.ip = ip;
        this.port = port;
        this.type = type.toLowerCase();
        this.timeInterval = timeInterval;
        this.timeInterval = TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailableFlag() {
        return availableFlag;
    }

    public void setAvailableFlag(boolean availableFlag) {
        this.availableFlag = availableFlag;
    }

    public boolean isAnonymousFlag() {
        return anonymousFlag;
    }

    public void setAnonymousFlag(boolean anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public long getLastSuccessfulTime() {
        return lastSuccessfulTime;
    }

    public void setLastSuccessfulTime(long lastSuccessfulTime) {
        this.lastSuccessfulTime = lastSuccessfulTime;
    }

    public long getSuccessfulTotalTime() {
        return successfulTotalTime;
    }

    public void setSuccessfulTotalTime(long successfulTotalTime) {
        this.successfulTotalTime = successfulTotalTime;
    }

    public void setTimeInterval(long timeInterval){
        this.timeInterval=TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(timeInterval - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        Proxy element = (Proxy)o;
        if (successfulAverageTime == 0.0d ||element.successfulAverageTime == 0.0d){
            return 0;
        }
        return successfulAverageTime > element.successfulAverageTime ? 1:(successfulAverageTime < element.successfulAverageTime ? -1 : 0);
    }

    public int getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(int failureTimes) {
        this.failureTimes = failureTimes;
    }

    public int getSuccessfulTimes() {
        return successfulTimes;
    }

    public void setSuccessfulTimes(int successfulTimes) {
        this.successfulTimes = successfulTimes;
    }

    public double getSuccessfulAverageTime() {
        return successfulAverageTime;
    }

    public void setSuccessfulAverageTime(double successfulAverageTime) {
        this.successfulAverageTime = successfulAverageTime;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "timeInterval=" + timeInterval +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", availableFlag=" + availableFlag +
                ", anonymousFlag=" + anonymousFlag +
                ", lastSuccessfulTime=" + lastSuccessfulTime +
                ", successfulTotalTime=" + successfulTotalTime +
                ", failureTimes=" + failureTimes +
                ", successfulTimes=" + successfulTimes +
                ", successfulAverageTime=" + successfulAverageTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proxy proxy = (Proxy) o;

        if (port != proxy.port) return false;
        return ip.equals(proxy.ip);

    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port;
        return result;
    }

    public String getProxyStr() {
        return type + "://" + ip + ":" + port;
    }

    /**
     * 将Proxy转换成一个HttpHost对象
     * @return
     */
    public HttpHost toHttpHost() {

        return new HttpHost(ip,port,type);
    }
}
