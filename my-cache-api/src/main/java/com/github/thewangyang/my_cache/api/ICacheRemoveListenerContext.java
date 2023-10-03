package com.github.thewangyang.my_cache.api;

//定义删除监听器上下文函数
public interface ICacheRemoveListenerContext<K, V> {
    //清空的key
    K key();

    //清空的key对应的值
    V value();

    //删除的key-value的类型
    String type();
}
