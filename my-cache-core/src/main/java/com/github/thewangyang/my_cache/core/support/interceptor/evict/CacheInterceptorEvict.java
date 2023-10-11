package com.github.thewangyang.my_cache.core.support.interceptor.evict;


import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICacheEvict;
import com.github.thewangyang.my_cache.api.ICacheExpire;
import com.github.thewangyang.my_cache.api.ICacheInterceptor;
import com.github.thewangyang.my_cache.api.ICacheInterceptorContext;

import java.lang.reflect.Method;

// 定义去除策略拦截器类
// 实现拦截器接口
public class CacheInterceptorEvict<K, V> implements ICacheInterceptor<K, V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorEvict.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("去除拦截器开始");
    }

    @Override
    @SuppressWarnings("all")
    public void after(ICacheInterceptorContext<K, V> context) {
        // 定义cacheEvict对象
        // 得到cache中元素的淘汰策略
        ICacheEvict<K, V> cacheEvict = context.cache().evict();

        // 得到方法名称
        Method method = context.method();
        // 得到context.params参数的第一个元素
        final K key = (K) context.params()[0];

        // 查看方法名称是否为remove
        if("remove".equals(method.getName())){
            cacheEvict.removeKey(key);
        }else{
            // 如果不是删除，那么直接更新
            cacheEvict.updateKey(key);
        }

    }
}
