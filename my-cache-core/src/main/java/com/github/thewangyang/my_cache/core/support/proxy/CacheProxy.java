package com.github.thewangyang.my_cache.core.support.proxy;



import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.core.support.proxy.cglib.CglibProxy;
import com.github.thewangyang.my_cache.core.support.proxy.dynamic.DynamicProxy;
import com.github.thewangyang.my_cache.core.support.proxy.none.NoneProxy;

import java.lang.reflect.Proxy;

// 表示代理信息的类
public class CacheProxy {

    private CacheProxy(){}


    //获得对象代理函数
    @SuppressWarnings("all")
    public static <K, V> ICache<K, V> getProxy(final ICache<K,V> cache){
        if(ObjectUtil.isNull(cache)){
            return (ICache<K,V>) new NoneProxy(cache).proxy();
        }

        final Class class_instance = cache.getClass();

        if(class_instance.isInterface() || Proxy.isProxyClass(class_instance)){
            return (ICache<K,V>) new DynamicProxy(cache).proxy();
        }

        return (ICache<K, V>) new CglibProxy(cache).proxy();
    }


}
