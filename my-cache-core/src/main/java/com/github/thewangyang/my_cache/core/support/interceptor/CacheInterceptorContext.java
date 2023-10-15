package com.github.thewangyang.my_cache.core.support.interceptor;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheInterceptorContext;
import sun.misc.Cache;

import java.lang.reflect.Method;

// 定义拦截器上下文Context类
public class CacheInterceptorContext<K, V> implements ICacheInterceptorContext<K, V> {

    // 定义Cache对象
    private ICache<K, V> cache;

    // 定义执行的方法信息
    private Method methodName;


    // 定义执行的参数
    private Object[] params;

    // 定义方法执行的结果
    private Object result;


    // 定义开始时间
    private long startMills;


    // 定义结束时间
    private long endMills;


    // 定义静态创建CacheInterceptorContext对象的方法
    public static <K, V> CacheInterceptorContext<K, V> newInstance(){
        return new CacheInterceptorContext<>();
    }


    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    public CacheInterceptorContext<K, V> cache(ICache<K, V> cache){
        this.cache = cache;
        return this;
    }

    @Override
    public Method method() {
        return methodName;
    }

    public CacheInterceptorContext<K, V> method(Method methodName){
        this.methodName = methodName;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheInterceptorContext<K, V> params(Object[] params){
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public CacheInterceptorContext<K, V> params(Object result){
        this.result = result;
        return this;
    }

    @Override
    public long startMills() {
        return 0;
    }

    public CacheInterceptorContext<K, V> startMills(long startMills){
        this.startMills = startMills;
        return this;
    }


    @Override
    public long endMills() {
        return 0;
    }

    public CacheInterceptorContext<K, V> endMills(long endMills){
        this.endMills = endMills;
        return this;
    }

}
