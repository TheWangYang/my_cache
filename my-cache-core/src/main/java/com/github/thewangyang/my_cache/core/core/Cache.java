package com.github.thewangyang.my_cache.core.core;


import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.thewangyang.my_cache.annotation.CacheInterceptor;
import com.github.thewangyang.my_cache.api.*;
import com.github.thewangyang.my_cache.core.constant.enums.CacheRemoveType;
import com.github.thewangyang.my_cache.core.exception.CacheRuntimeException;
import com.github.thewangyang.my_cache.core.support.evict.CacheEvictContext;
import com.github.thewangyang.my_cache.core.support.expire.CacheExpire;
import com.github.thewangyang.my_cache.core.support.listener.remove.CacheRemoveListenerContext;
import com.github.thewangyang.my_cache.core.support.persist.InnerCachePersist;
import com.github.thewangyang.my_cache.core.support.proxy.CacheProxy;

import java.util.*;

// 实现Cache核心类
public class Cache<K, V> implements ICache<K, V> {

    // 定义map对象
    private Map<K,V> map;


    // 定义size大小限制
    private int sizeLimit;

    // 定义驱除策略
    private ICacheEvict<K,V> evict;

    // 定义过期策略
    private ICacheExpire<K,V> expire;

    // 删除监听器类
    private List<ICacheRemoveListener<K,V>> removeListeners;


    // 慢日志监听类
    private List<ICacheSlowListener> slowListeners;

    // 加载类
    private ICacheLoad<K,V> load;

    // 持久化类
    private ICachePersist<K,V> persist;

    // 定义map实现
    public Cache<K,V> map(Map<K,V> map){
        this.map = map;
        return this;
    }


    /**
     * 设置大小限制
     * @param sizeLimit 大小限制
     * @return this
     */
    public Cache<K, V> sizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }


    /**
     * 设置驱除策略
     * @param cacheEvict 驱除策略
     * @return this
     * @since 0.0.8
     */
    public Cache<K, V> evict(ICacheEvict<K, V> cacheEvict) {
        this.evict = cacheEvict;
        return this;
    }

    @Override
    public ICacheEvict<K, V> evict() {
        return this.evict;
    }


    //设置持久化策略
    public void persist(ICachePersist<K,V> persist){
        this.persist = persist;
    }

    // 获得持久化策略
    @Override
    public ICachePersist<K, V> persist() {
        return this.persist;
    }


    @Override
    public List<ICacheRemoveListener<K, V>> removeListeners() {
        return this.removeListeners;
    }

    public Cache<K,V> removeListeners(List<ICacheRemoveListener<K,V>> removeListeners){
        this.removeListeners = removeListeners;
        return this;
    }

    @Override
    public List<ICacheSlowListener> slowListeners() {
        return this.slowListeners;
    }

    public Cache<K,V> slowListeners(List<ICacheSlowListener> slowListeners){
        this.slowListeners = slowListeners;
        return this;
    }


    // 设置load
    public Cache<K,V> load(ICacheLoad<K,V> load){
        this.load = load;
        return this;
    }

    // 获得load对象
    @Override
    public ICacheLoad<K, V> load() {
        return this.load;
    }


    // 设置初始化方法
    public void init(){
        this.expire = new CacheExpire<>(this);
        this.load.load(this);

        if(this.persist != null){
            new InnerCachePersist<>(this, persist);
        }
    }


    // 获得过期时间
    @Override
    @CacheInterceptor
    public ICache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;

        // 使用代理待用
        Cache<K,V> cacheProxy = (Cache<K, V>) CacheProxy.getProxy(this);
        return cacheProxy.expireAt(key, expireTime);
    }


    // 指定过期时间
    @Override
    @ CacheInterceptor(aof = true)
    public ICache<K, V> expireAt(K key, long timeInMills) {
        this.expire.expire(key, timeInMills);
        return this;
    }

    @Override
    @CacheInterceptor
    public ICacheExpire<K, V> expire() {
        return this.expire;
    }

    @Override
    @CacheInterceptor(refresh = true)
    public int size() {
        return this.map.size();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    @CacheInterceptor(refresh = true, evict = true)
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    @CacheInterceptor(evict = true)
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        // 刷新所有过期的信息
        K genericKey = (K) key;
        this.expire.refreshExpire(Collections.singletonList(genericKey));
        return this.map.get(key);
    }

    @Override
    @CacheInterceptor(aof = true, evict = true)
    public V put(K key, V value) {
        // 尝试驱除
        CacheEvictContext<K,V> cacheEvictContext = new CacheEvictContext<>();
        // 设置context上下文
        cacheEvictContext.key(key).size(this.sizeLimit).cache(this);

        // 定义CacheEntry
        ICacheEntry<K,V> cacheEntry = evict.evict(cacheEvictContext);

        // 添加拦截器调用
        if(ObjectUtil.isNotNull(cacheEntry)){
            // 获得淘汰监听器
            ICacheRemoveListenerContext<K,V> removeListenerContext =
                    CacheRemoveListenerContext.<K,V>newInstance().key(cacheEntry.key()).value(cacheEntry.value())
                    .type(CacheRemoveType.EVICT.code());


            // 执行监听器
            for(ICacheRemoveListener<K,V> listener : cacheEvictContext.cache().removeListeners()) {
                listener.listen(removeListenerContext);
            }
        }


        // 判断驱除之后的信息
        if(isSizeLimit()) {
            throw new CacheRuntimeException("当前队列已满，数据添加失败！");
        }

        // 执行添加
        return this.map.put(key, value);
    }

    /**
     * 是否已经达到大小最大的限制
     * @return 是否限制
     * @since 0.0.2
     */
    private boolean isSizeLimit() {
        final int currentSize = this.size();
        return currentSize >= this.sizeLimit;
    }



    @Override
    @CacheInterceptor(aof = true, evict = true)
    public V remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    @CacheInterceptor(aof = true)
    public void putAll(Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override
    @CacheInterceptor(refresh = true, aof = true)
    public void clear() {
        this.map.clear();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Collection<V> values() {
        return this.map.values();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Set<Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }


}
