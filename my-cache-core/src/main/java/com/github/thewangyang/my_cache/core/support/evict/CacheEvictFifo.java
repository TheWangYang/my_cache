package com.github.thewangyang.my_cache.core.support.evict;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;
import com.github.thewangyang.my_cache.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.Queue;

//定义CacheEvictFifo类
//实现先进先出-淘汰策略
public class CacheEvictFifo<K, V> extends AbstractCacheEvict<K, V> {

    //定义队列实现FIFO
    public Queue<K> que = new LinkedList<>();

    //重载doEvict()函数
    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        //定义返回result结果变量
        CacheEntry<K, V> result = null;

        //得到cache对象
        final ICache<K, V> cache = context.cache();

        //超过显示移除
        if(cache.size() >= context.size()){
            //得到移除的key
            K evictKey = que.remove();

            //得到最开始的元素
            V evictValue = cache.remove(evictKey);

            //得到返回结构
            result = new CacheEntry<>(evictKey, evictValue);
        }

        //将新加的key放入到que中
        final K key = context.key();
        que.add(key);

        return result;
    }

}
