package com.github.thewangyang.my_cache.core.bs;


import com.github.thewangyang.my_cache.api.*;
import com.github.thewangyang.my_cache.core.core.Cache;
import com.github.thewangyang.my_cache.core.support.evict.CacheEvicts;
import com.github.thewangyang.my_cache.core.support.listener.remove.CacheRemoveListeners;
import com.github.thewangyang.my_cache.core.support.listener.slow.CacheSlowListeners;
import com.github.thewangyang.my_cache.core.support.load.CacheLoads;
import com.github.thewangyang.my_cache.core.support.persist.CachePersists;

import com.github.houbb.heaven.util.common.ArgUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 缓存引导类
public class CacheBs<K,V> {

    private CacheBs(){

    }

    // 创建对象实例
    public static <K,V> CacheBs<K,V> newInstance(){
        return new CacheBs<>();
    }

    // map实现
    private Map<K,V> map = new HashMap<>();


    // 设置大小限制
    private int size = Integer.MAX_VALUE;

    // 设置驱除策略
    private ICacheEvict<K,V> evict = CacheEvicts.fifo();

    // 删除监听类
    private final List<ICacheRemoveListener<K,V>> removeListeners = CacheRemoveListeners.defaults();


    // 慢操作监听类
    private final List<ICacheSlowListener> slowListeners = CacheSlowListeners.none();

    // 定义加载策略
    private ICacheLoad<K,V> load = CacheLoads.noneLoad();


    // 定义持久化策略实现
    private ICachePersist<K,V> persist = CachePersists.nonePersist();


    // map实现
    public CacheBs<K,V> map (Map<K,V> map){
        ArgUtil.notNull(map, "map");


        this.map = map;
        return this;
    }

    // 设置size信息
    public CacheBs<K,V> size(int size){
        ArgUtil.notNegative(size, "size");

        this.size = size;
        return this;
    }

    // 设置取出策略
    public CacheBs<K,V> evict(ICacheEvict<K,V> evict){
        this.evict = evict;
        return this;
    }

    // 设置加载策略
    public CacheBs<K, V> load(ICacheLoad<K, V> load) {
        ArgUtil.notNull(load, "load");

        this.load = load;
        return this;
    }

    /**
     * 添加删除监听器
     * @param removeListener 监听器
     * @return this
     * @since 0.0.6
     */
    public CacheBs<K, V> addRemoveListener(ICacheRemoveListener<K,V> removeListener) {
        ArgUtil.notNull(removeListener, "removeListener");

        this.removeListeners.add(removeListener);
        return this;
    }

    /**
     * 添加慢日志监听器
     * @param slowListener 监听器
     * @return this
     * @since 0.0.9
     */
    public CacheBs<K, V> addSlowListener(ICacheSlowListener slowListener) {
        ArgUtil.notNull(slowListener, "slowListener");

        this.slowListeners.add(slowListener);
        return this;
    }

    /**
     * 设置持久化策略
     * @param persist 持久化
     * @return this
     * @since 0.0.8
     */
    public CacheBs<K, V> persist(ICachePersist<K, V> persist) {
        this.persist = persist;
        return this;
    }


    // build方法
    public ICache<K,V> build(){

        Cache<K,V> cache = new Cache<>();
        cache.map(map);
        cache.evict(evict);
        cache.sizeLimit(size);
        cache.removeListeners(removeListeners);
        cache.load(load);
        cache.persist(persist);
        cache.slowListeners(slowListeners);

        return cache;
    }


}
