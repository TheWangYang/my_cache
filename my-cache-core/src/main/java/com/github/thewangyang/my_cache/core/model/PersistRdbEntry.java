package com.github.thewangyang.my_cache.core.model;


//定义持久明细类
public class PersistRdbEntry<K, V> {
    public K key;
    public V value;

    //定义过期时间
    public long expire;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }


}
