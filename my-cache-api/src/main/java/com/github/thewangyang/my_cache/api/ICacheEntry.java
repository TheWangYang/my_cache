package com.github.thewangyang.my_cache.api;


//定义的被删除的缓存详情
public interface ICacheEntry<K, V> {

    //定义key函数
    K key();

    //定义value函数
    V value();

}
