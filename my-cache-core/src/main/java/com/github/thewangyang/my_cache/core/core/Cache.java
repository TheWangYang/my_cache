package com.github.thewangyang.my_cache.core.core;

import com.github.thewangyang.my_cache.api.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

// 实现Cache核心类
public class Cache<K, V> implements ICache<K, V> {


    @Override
    public ICache<K, V> expire(K key, long timeInMills) {
        return null;
    }

    @Override
    public ICache<K, V> expireAt(K key, long timeInMills) {
        return null;
    }

    @Override
    public ICacheExpire<K, V> expire() {
        return null;
    }

    @Override
    public List<ICacheRemoveListener<K, V>> removeListeners() {
        return null;
    }

    @Override
    public List<ICacheSlowListener> slowListeners() {
        return null;
    }

    @Override
    public ICacheLoad<K, V> load() {
        return null;
    }

    @Override
    public ICachePersist<K, V> persist() {
        return null;
    }

    @Override
    public ICacheEvict<K, V> evict() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
