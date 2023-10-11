package com.github.thewangyang.my_cache.core.support.persist;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICachePersist;

import java.util.concurrent.TimeUnit;

// 创建CachePersistAdapter适配器类
// 实现ICachePersist接口
// 缓存持久化-适配器模式
public class CachePersistAdaptor<K, V> implements ICachePersist<K, V> {

    @Override
    public void persist(ICache<K, V> cache) {

    }

    @Override
    public long delay() {
        return this.period();
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        // 返回时间单元秒数
        return TimeUnit.SECONDS;
    }

}
