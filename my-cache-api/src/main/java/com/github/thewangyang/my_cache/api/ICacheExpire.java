package com.github.thewangyang.my_cache.api;


import java.util.Collection;

//定义的缓存过期处理接口
public interface ICacheExpire<K, V> {

    //指定过期信息
    // return void
    void expire(final K key, final long expireAt);

    //惰性删除中需要处理的keys
    void refreshExpire(final Collection<K> keyList);

    //定义过期时间函数
    //根据参数key返回其过期时间
    //如果key不存在，则返回null
    Long expireTime(final K key);

}
