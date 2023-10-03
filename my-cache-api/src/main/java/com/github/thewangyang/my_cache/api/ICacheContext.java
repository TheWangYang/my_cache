package com.github.thewangyang.my_cache.api;


import java.util.Map;

//定义缓存的上下文接口
public interface ICacheContext<K, V> {

    //存储的map信息
    Map<K, V> map();

    //定义缓存大小限制
    int size();

    //删除策略
    ICacheEvict<K, V> cacheEvict();

}
