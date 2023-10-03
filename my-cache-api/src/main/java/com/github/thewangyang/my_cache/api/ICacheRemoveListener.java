package com.github.thewangyang.my_cache.api;


//定义删除监听器接口
public interface ICacheRemoveListener<K, V> {
    //定义监听函数
    //参数为待删除监听器的上下文函数
    void listen(ICacheRemoveListenerContext<K, V> context);
}
