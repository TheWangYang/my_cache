package com.github.thewangyang.my_cache.core.support.listener.remove;


import com.github.thewangyang.my_cache.api.ICacheRemoveListenerContext;


// 定义监听器上下文类
// 监听context对象
public class CacheRemoveListenerContext<K, V> implements ICacheRemoveListenerContext<K, V> {

    //定义key
    private K key;

    //定义value
    private V value;

    //定义删除类型
    private String type;

    //新建实例
    public static <K, V> CacheRemoveListenerContext<K, V> newInstance(){
        return new CacheRemoveListenerContext<>();
    }


    //定义获得key的函数
    public CacheRemoveListenerContext<K, V> key(K key){
        this.key = key;
        return this;
    }

    @Override
    public K key() {
        return key;
    }


    public CacheRemoveListenerContext<K, V> value(V value) {
        this.value = value;
        return this;
    }

    @Override
    public V value() {
        return value;
    }


    public CacheRemoveListenerContext<K, V> type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String type() {
        return type;
    }
}
