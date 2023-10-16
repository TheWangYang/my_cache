package com.github.thewangyang.my_cache.core.support.load;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheLoad;

// 无策略加载缓存对象
public class CacheLoadNone<K, V> implements ICacheLoad<K, V> {

    @Override
    public void load(ICache<K, V> cache) {
        // to-do...
        // nothing...
    }
}
