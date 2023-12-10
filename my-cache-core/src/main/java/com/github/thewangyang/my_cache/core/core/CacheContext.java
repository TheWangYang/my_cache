package com.github.thewangyang.my_cache.core.core;


import com.github.thewangyang.my_cache.api.ICacheContext;
import com.github.thewangyang.my_cache.api.ICacheEvict;

import java.util.Map;

// 定义上下文类
public class CacheContext<K,V> implements ICacheContext<K,V> {

    // 定义map对象
    private Map<K,V> map;

    // 定义大小限制
    private int size;

    // 定义驱除策略
    private ICacheEvict<K,V> evict;

    // 定义
    @Override
    public Map<K, V> map() {
        return this.map;
    }

    public CacheContext<K,V> map(Map<K,V> map){
        this.map = map;
        return this;
    }

    @Override
    public int size() {
        return this.size;
    }

    public CacheContext<K,V> size(int size){
        this.size = size;
        return this;
    }

    @Override
    public ICacheEvict<K, V> cacheEvict() {
        return this.evict;
    }

    public CacheContext<K,V> cacheEvict(ICacheEvict<K,V> evict){
        this.evict = evict;
        return this;
    }
}
