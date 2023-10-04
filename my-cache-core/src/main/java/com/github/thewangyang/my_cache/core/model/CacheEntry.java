package com.github.thewangyang.my_cache.core.model;

import com.github.thewangyang.my_cache.api.ICacheEntry;

/**
 * 实现ICacheEntry接口
 * key value 的明细信息
 * @author thewangyang
 * @since 0.0.1
 * @param <K> key
 * @param <V> value
 */
public class CacheEntry<K,V> implements ICacheEntry<K,V> {

    //定义K类型的key变量
    private final K key;

    //定义V类型的value变量
    private final V value;

    //定义新建元素构造函数
    public static <K, V> CacheEntry<K, V> of(final K key, final V value){
        return new CacheEntry<>(key, value);
    }

    //定义包含两个参数的构造函数
    public CacheEntry(K key, V value){
        this.key = key;
        this.value = value;
    }

    //实现接口中的函数
    @Override
    public K key(){
        return key;
    }


    @Override
    public V value(){
        return value;
    }

    //重写其中的toString()方法
    @Override
    public String toString(){
        return "EvictEntry{" + "key=" + key + ", value=" + value + "}";
    }

}
