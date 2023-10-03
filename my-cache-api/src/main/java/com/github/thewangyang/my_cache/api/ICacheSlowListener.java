package com.github.thewangyang.my_cache.api;


//定义慢日志监听类列表
public interface ICacheSlowListener {

    //定义监听函数
    //参数为慢日志监听上下文类
    void listen(final ICacheSlowListenerContext context);

    //定义慢日志的阈值
    long slowerThanMills();
}
