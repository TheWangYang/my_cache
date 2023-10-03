package com.github.thewangyang.my_cache.api;



//定义拦截器接口
//实现：耗时统计+监听器
public interface ICacheInterceptor<K, V> {

    //方法执行之前
    void before(ICacheInterceptorContext<K, V> context);

    //方法执行之后
    void after(ICacheInterceptorContext<K, V> context);

}
