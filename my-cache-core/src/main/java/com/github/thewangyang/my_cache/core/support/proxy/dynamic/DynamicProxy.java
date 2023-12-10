package com.github.thewangyang.my_cache.core.support.proxy.dynamic;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.core.support.proxy.ICacheProxy;
import com.github.thewangyang.my_cache.core.support.proxy.bs.CacheProxyBs;
import com.github.thewangyang.my_cache.core.support.proxy.bs.CacheProxyBsContext;
import com.github.thewangyang.my_cache.core.support.proxy.bs.ICacheProxyBsContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


// 实现动态代理类
public class DynamicProxy implements ICacheProxy, InvocationHandler {

    //定义被代理的对象
    private final ICache target;

    public DynamicProxy(ICache target){
        this.target = target;
    }

    @Override
    @SuppressWarnings("all")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //得到对象
        ICacheProxyBsContext context = CacheProxyBsContext.newInstance().method(method).params(args).target(target);

        return CacheProxyBs.newInstance().context(context).execute();
    }

    // 定义proxy方法
    public Object proxy(){
        InvocationHandler handler = new DynamicProxy(target);

        return Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                target.getClass().getInterfaces(), handler);
    }
}
