package com.github.thewangyang.my_cache.core.support.proxy.none;


import com.github.thewangyang.my_cache.core.support.proxy.ICacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 代理类
// 没有代理的类的实现
public class NoneProxy implements ICacheProxy, InvocationHandler {

    // 创建代理对象
    private final Object target;

    public NoneProxy(Object target){
        this.target = target;
    }

    @Override
    public Object proxy() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }

}
