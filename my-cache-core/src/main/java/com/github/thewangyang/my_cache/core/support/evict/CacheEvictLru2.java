package com.github.thewangyang.my_cache.core.support.evict;


//定义淘汰策略-最近最少使用
//在CacheEvictLru.java的基础上进行扩充

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;
import com.github.thewangyang.my_cache.core.support.struct.lru.ILruMap;
import com.github.thewangyang.my_cache.core.support.struct.lru.impl.LruMapCircleList;

public class CacheEvictLru2<K, V> extends AbstractCacheEvict<K, V> {

    //定义log对象
    private final Log log = LogFactory.getLog(CacheEvictLru2.class);

    //定义第1次访问的LRU
    private final ILruMap<K, V> firstLruMap;

    //定义第2次访问的lru
    private final ILruMap<K, V> secondLruMap;

    //添加构造函数
    public CacheEvictLru2(){
        this.firstLruMap = new L
    }

    //定义
    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }
}
