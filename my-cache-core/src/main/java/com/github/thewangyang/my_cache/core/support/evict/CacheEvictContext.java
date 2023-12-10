package com.github.thewangyang.my_cache.core.support.evict;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;

//移除元素的上下文类
public class CacheEvictContext<K, V> implements ICacheEvictContext<K, V> {

    //新加的key
    private K key;

    //创建cache对象
    private ICache<K, V> cache;

    //最大的大小
    private int size;

    //返回移除淘汰类对象的方法
    public CacheEvictContext<K, V> key(K key){
        this.key = key;
        return this;
    }

    //得到key
    @Override
    public K key() {
        return key;
    }

    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    public CacheEvictContext<K, V> cache(ICache<K,V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    //根据size得到新的CacheEvictContext对象
    public CacheEvictContext<K, V> size(int size){
        this.size = size;
        return this;
    }
}
