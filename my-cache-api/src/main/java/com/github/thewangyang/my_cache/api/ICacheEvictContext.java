package com.github.thewangyang.my_cache.api;


//定义的的缓存删除明细上下文函数
public interface ICacheEvictContext<K, V> {

    //新加的key
    K key();

    //cache实现
    ICache<K, V> cache();

    //获取缓存大小
    int size();

}
