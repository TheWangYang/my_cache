package com.github.thewangyang.my_cache.core.support.evict;


import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;

//定义无策略淘汰类
public class CacheEvictNone<K, V> extends AbstractCacheEvict<K, V>{

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }
}
