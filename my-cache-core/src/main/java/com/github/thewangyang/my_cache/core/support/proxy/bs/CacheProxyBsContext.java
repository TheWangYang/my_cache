package com.github.thewangyang.my_cache.core.support.proxy.bs;

import com.github.thewangyang.my_cache.annotation.CacheInterceptor;
import com.github.thewangyang.my_cache.api.ICache;

import java.lang.reflect.Method;


// cache中proxy bs context接口的实现类
public class CacheProxyBsContext implements ICacheProxyBsContext{

    //定义目标
    private ICache target;

    //定义入参
    private Object[] params;


    // 定义方法
    private Method method;


    //定义拦截器
    private CacheInterceptor cacheInterceptor;


    //新建对象
    public static CacheProxyBsContext newInstance(){
        return new CacheProxyBsContext();
    }

    @Override
    public ICache target() {
        return this.target;
    }

    @Override
    public CacheProxyBsContext target(ICache target) {
        this.target = target;
        return this;
    }

    @Override
    public Object[] params() {
        return this.params;
    }


    public CacheProxyBsContext params(Object[] params){
        this.params = params;
        return this;
    }

    @Override
    public Method method() {
        return this.method;
    }

    @Override
    public Object process() throws Throwable {
        return this.method.invoke(this.target, this.params);
    }

    @Override
    public CacheInterceptor interceptor() {
        return this.cacheInterceptor;
    }

    // 额外定义的方法
    public CacheProxyBsContext method(Method method){
        this.method = method;
        this.cacheInterceptor = method.getAnnotation(CacheInterceptor.class);
        return this;
    }


}
