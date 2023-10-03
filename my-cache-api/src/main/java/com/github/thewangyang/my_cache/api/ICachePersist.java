package com.github.thewangyang.my_cache.api;


import java.util.concurrent.TimeUnit;

//定义持久化接口
//持久化缓存接口
public interface ICachePersist<K, V> {
    //持久化缓存信息
    void persist(final ICache<K, V> cache);

    //延迟时间
    long delay();

    //时间间隔
    long period();

    //时间单位
    TimeUnit timeUnit();

}
