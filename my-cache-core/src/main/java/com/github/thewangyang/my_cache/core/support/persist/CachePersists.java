package com.github.thewangyang.my_cache.core.support.persist;


import com.github.thewangyang.my_cache.api.ICachePersist;

// 创建缓存持久化策略类
public final class CachePersists {

    private CachePersists(){

    }

    // 创建无操作持久化策略类
    public static <K, V> ICachePersist<K, V> nonePersist(){
        return new CachePersistNone<>();
    }

    // 创建dbJson持久化策略类
    public static <K, V> ICachePersist<K, V> dbJsonPersist(final String dbPath){
        return new CachePersistDbJson<>(dbPath);
    }

    // 创建AOF持久化策略类
    public static <K, V> ICachePersist<K, V> aofPersist(final String dbPath){
        return new CachePersistAof<>(dbPath);
    }

}
