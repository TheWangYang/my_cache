package com.github.thewangyang.my_cache.api;


import java.lang.reflect.Method;

//定义拦截器的上下文接口
public interface ICacheInterceptorContext<K, V> {

    // 缓存信息
    ICache<K, V> cache();

    // 执行的方法信息
    Method method();

    // 获得执行的参数
    Object[] params();

    // 方法执行的结果
    Object[] result();

    // 方法的开始时间
    long startMills();

    // 方法的结束时间
    long endMills();

}
