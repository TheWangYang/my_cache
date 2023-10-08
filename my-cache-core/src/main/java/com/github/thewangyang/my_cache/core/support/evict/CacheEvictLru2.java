package com.github.thewangyang.my_cache.core.support.evict;




import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;
import com.github.thewangyang.my_cache.core.model.CacheEntry;
import com.github.thewangyang.my_cache.core.support.struct.lru.ILruMap;
import com.github.thewangyang.my_cache.core.support.struct.lru.impl.LruMapDoubleList;



//定义淘汰策略-最近最少使用
//在CacheEvictLru.java的基础上进行扩充
public class CacheEvictLru2<K, V> extends AbstractCacheEvict<K, V> {

    //定义log对象
    private final Log log = LogFactory.getLog(CacheEvictLru2.class);

    //定义首次访问的LRU
    private final ILruMap<K, V> firstLruMap;

    //定义多次访问的lru
    private final ILruMap<K, V> secondLruMap;

    //重写默认构造函数
    public CacheEvictLru2(){
        //创建双向链表LruMap对象
        this.firstLruMap = new LruMapDoubleList<>();
        this.secondLruMap = new LruMapDoubleList<>();
    }


    //重写淘汰策略函数
    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {

        //定义需要返回的CacheEntry结果对象
        ICacheEntry<K, V> result = null;

        //得到保存的cache对象
        final ICache<K, V> cache = context.cache();

        if(cache.size() >= context.size()){
            //创建将淘汰的null空的entry对象
            ICacheEntry<K, V> evictEntry = null;

            //首先从firstMap中移除元素
            if(!firstLruMap.isEmpty()){
                evictEntry = this.firstLruMap.removeEldest();
                log.debug("从 firstLruMap 中淘汰数据：{}", evictEntry);
            }else{
                evictEntry = this.secondLruMap.removeEldest();
                log.debug("从 secondLruMap 中淘汰数据：{}", evictEntry);
            }

            //然后执行缓存移除操作
            final K evictKey = evictEntry.key();
            V evictValue = cache.remove(evictKey);
            result = CacheEntry.of(evictKey, evictValue);
        }

        return result;
    }


    //重写updateKey函数
    @Override
    public void updateKey(K key) {

        //判断是否在firstMap或secondMap中出现
        if(firstLruMap.contains(key) || secondLruMap.contains(key)){
            //删除key
            this.removeKey(key);
            //加入到secondLruMap中
            secondLruMap.updateKey(key);

            log.debug("key: {} 多次访问，加入到 secondLruMap 中", key);
        }else{//不在两个lruMap中
            firstLruMap.updateKey(key);
            log.debug("key: {} 首次访问，加入到 firstLruMap 中", key);
        }

    }

    //重写removeKey()函数
    @Override
    public void removeKey(K key) {
        //多次访问map删除逻辑
        if(secondLruMap.contains(key)){
            secondLruMap.removeKey(key);
            log.debug("key: {} 从 secondLruMap 中移除", key);
        }else{
            firstLruMap.removeKey(key);
            log.debug("key: {} 从 firstLruMap 中移除", key);
        }
    }
}
