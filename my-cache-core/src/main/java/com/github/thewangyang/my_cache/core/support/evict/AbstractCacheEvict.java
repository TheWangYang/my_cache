package com.github.thewangyang.my_cache.core.support.evict;


import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvict;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;


//定义的淘汰策略抽象类
public abstract class AbstractCacheEvict<K, V> implements ICacheEvict<K, V> {

    //实现接口中的方法
    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        return doEvict(context);
    }

    //定义doEvict()抽象方法
    protected abstract ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context);

    //实现其他接口方法
    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}
