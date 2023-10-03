package com.github.thewangyang.my_cache.api;

//定义加载信息接口
//也就是缓存接口
public interface ICacheLoad<K, V> {

    //加载缓存信息
    void load(final ICache<K, V> cache);

}
