package com.github.thewangyang.my_cache.core.support.load;


import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICacheLoad;

// 不同缓存加载策略的创建类
public class CacheLoads {

    private static final Log log = LogFactory.getLog(CacheLoads.class);

    public CacheLoads(){

    }

    // 创建none类型的缓存加载策略对象
    public static <K, V> ICacheLoad<K, V> noneLoad() {
        return new CacheLoadNone<>();
    }

    // 创建Aof类型的缓存加载策略对象
    public static <K, V> ICacheLoad<K, V> aofLoad(String dbPath) {
        return new CacheLoadAof<>(dbPath);
    }

    // 创建DbJson类型的缓存加载策略对象
    public static <K, V> ICacheLoad<K, V> dbJsonLoad(String dbPath) {
        return new CacheLoadDbJson<>(dbPath);
    }

}
