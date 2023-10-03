package com.github.thewangyang.my_cache.api;


//定义淘汰策略
//也就是元素的删除策略
public interface ICacheEvict<K, V> {

    //定义删除策略
    //被删除key-value的明细
    ICacheEntry<K, V> evict(final ICacheEvictContext<K, V> context);


    //更新key值
    void updateKey(final K key);

    //删除key信息
    void removeKey(final K key);

}
