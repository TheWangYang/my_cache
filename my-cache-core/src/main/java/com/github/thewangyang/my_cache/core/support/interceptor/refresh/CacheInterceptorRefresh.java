package com.github.thewangyang.my_cache.core.support.interceptor.refresh;


import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheInterceptor;
import com.github.thewangyang.my_cache.api.ICacheInterceptorContext;

// 设置refresh的拦截器
public class CacheInterceptorRefresh<K, V> implements ICacheInterceptor<K, V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorRefresh.class);



    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Refresh start");
        final ICache<K, V> cache = context.cache();
        // 先得到CacheExpire对象
        // 然后调用该对象的refreshExpire()函数
        cache.expire().refreshExpire(cache.keySet());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {

    }
}
