package com.github.thewangyang.my_cache.core.support.struct.lru;


import com.github.thewangyang.my_cache.api.ICacheEntry;

//定义的ILruMap接口
public interface ILruMap<K, V> {

    //定义接口方法
    //移除最老的元素
    ICacheEntry<K, V> removeEldest();

    //更新key的信息
    void updateKey(final K key);

    //移除对应的key信息
    void removeKey(final K key);

    //判断是否为空
    boolean isEmpty();

    //是否包含元素key
    boolean contains(final K key);

}
