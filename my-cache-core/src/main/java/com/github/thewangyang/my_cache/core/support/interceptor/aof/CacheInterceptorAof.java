package com.github.thewangyang.my_cache.core.support.interceptor.aof;


import com.alibaba.fastjson.JSON;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheInterceptor;
import com.github.thewangyang.my_cache.api.ICacheInterceptorContext;
import com.github.thewangyang.my_cache.api.ICachePersist;
import com.github.thewangyang.my_cache.core.model.PersistAofEntry;
import com.github.thewangyang.my_cache.core.support.persist.CachePersistAof;


// 用于拦截aof的缓存拦截器
// 持久化到文件的拦截器类
// 持久化到文件的实现类
// 顺序追加模式
public class CacheInterceptorAof<K, V> implements ICacheInterceptor<K, V> {

    // 定义log对象
    private static final Log log = LogFactory.getLog(CacheInterceptorAof.class);

    // 定义
    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("这个是aof持久化拦截器实现类的before()方法");
    }

    // 在持久化类操作之后调用该函数
    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        log.debug("在持久化操作之后调用的CacheInterceptor的after()函数");
        // 得到cache
        ICache<K, V> cache = context.cache();
        // 创建cache的持久化对象
        ICachePersist<K, V> cachePersist = cache.persist();

        // 然后判断cachePersist是否为CachePersistAof对象
        // 如果是CachePersistAof对象
        if(cachePersist instanceof CachePersistAof){
            // 将cachePersist对象转换为CachePersistAof对象
            // 强制转换
            CachePersistAof<K, V> cachePersistAof = (CachePersistAof<K, V>) cachePersist;

            // 获得method name
            String methodName = context.method().getName();

            // 创建PersistEntry对象
            // 调用的是类级别的创建实例对象的静态方法
            PersistAofEntry persistAofEntry = PersistAofEntry.newInstance();
            persistAofEntry.setParams(context.params());
            persistAofEntry.setMethodName(methodName);

            // 将json对象转换为String类型
            String json = JSON.toJSONString(persistAofEntry);

            // 直接持久化
            log.debug("AOF 开始追加文件内容：{}", json);
            // 调用append函数
            cachePersistAof.append(json);
            log.debug("AOF 完成追加文件内容：{}", json);

        }
    }

}
