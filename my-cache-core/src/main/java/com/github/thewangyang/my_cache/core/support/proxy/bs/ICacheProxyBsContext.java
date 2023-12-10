package com.github.thewangyang.my_cache.core.support.proxy.bs;


import com.github.thewangyang.my_cache.annotation.CacheInterceptor;
import com.github.thewangyang.my_cache.api.ICache;

import java.lang.reflect.Method;

//定义cache bs proxy context上下文接口
public interface ICacheProxyBsContext {

    // 定义拦截器信息
    CacheInterceptor interceptor();

    //获取代理对象信息
    ICache target();

    //目标对象
    ICacheProxyBsContext target(final ICache target);


    //获得参数信息
    Object[] params();


    //获得方法信息
    Method method();

    //方法执行
    Object process() throws Throwable;

}
