package com.github.thewangyang.my_cache.core.support.evict;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;
import com.github.thewangyang.my_cache.core.model.CacheEntry;
import com.github.thewangyang.my_cache.core.support.struct.lru.ILruMap;
import com.github.thewangyang.my_cache.core.support.struct.lru.impl.LruMapCircleList;


//定义淘汰策略-clock算法
public class CacheEvictClock<K, V> extends AbstractCacheEvict<K, V>{

    //定义私有的日志变量
    private static final Log log = LogFactory.getLog(CacheEvictClock.class);

    //定义循环链表
    public final ILruMap<K, V> circleList;

    //创建构造函数
    public CacheEvictClock(){
        circleList = new LruMapCircleList<>();
    }

    //实现抽象方法：doEvict()
    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {

        //创建ICacheEntry对象
        ICacheEntry<K, V> result = null;

        //获得缓存对象
        final ICache<K, V> cache = context.cache();

        //判断cache.size()是否超过了context.size
        if(cache.size() >= context.size()){
            //得到淘汰的Entry对象
            ICacheEntry<K, V> evictEntry = circleList.removeEldest();

            //执行缓存移除操作
            final K key = evictEntry.key();

            //得到待删除的value值
            V evictValue = cache.remove(key);

            log.debug("基于 clock 算法淘汰 key：{}, value: {}", key, evictValue);

            //得到结果
            result = new CacheEntry<>(key, evictValue);
        }

        return result;
    }


    //更新信息
    @Override
    public void updateKey(K key) {
        this.circleList.updateKey(key);
    }

    //删除key
    @Override
    public void removeKey(K key) {
        this.removeKey(key);
    }

}
