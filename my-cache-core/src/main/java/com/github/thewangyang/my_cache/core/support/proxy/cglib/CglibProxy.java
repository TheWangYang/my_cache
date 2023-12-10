package com.github.thewangyang.my_cache.core.support.proxy.cglib;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.core.support.proxy.ICacheProxy;
import com.github.thewangyang.my_cache.core.support.proxy.bs.CacheProxyBs;
import com.github.thewangyang.my_cache.core.support.proxy.bs.CacheProxyBsContext;
import com.github.thewangyang.my_cache.core.support.proxy.bs.ICacheProxyBsContext;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// 创建CGLIB代理类
public class CglibProxy implements ICacheProxy, MethodInterceptor {

    // 创建被代理类
    private final ICache target;

    public CglibProxy(ICache target){
        this.target = target;
    }

    @Override
    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        // 通过字节码技术创建目标对象类的子类实例作为代理
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {

        ICacheProxyBsContext context = CacheProxyBsContext.newInstance().method(method).params(params).target(target);

        return CacheProxyBs.newInstance().context(context).execute();
    }

    //

}
