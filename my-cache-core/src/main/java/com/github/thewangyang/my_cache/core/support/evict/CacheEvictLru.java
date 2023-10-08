package com.github.thewangyang.my_cache.core.support.evict;


// 引入他人的工具类
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;


//引入自定义的软件包
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;
import com.github.thewangyang.my_cache.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.List;


//最近最少使用淘汰策略类
public class CacheEvictLru<K, V> extends AbstractCacheEvict<K, V>{

    //定义log日志对象
    private final Log log = LogFactory.getLog(CacheEvictLru.class);

    //定义list信息
    //其中保存的为key信息
    private final List<K> list = new LinkedList<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        //实现LRU淘汰策略
        //定义返回result结果对象
        ICacheEntry<K, V> result = null;

        //得到cache对象
        final ICache<K, V> cache = context.cache();

        //超过了限制直接淘汰
        if(cache.size() >= context.size()){
            //得到淘汰的key
            //得到list中的最后一个key
            K evictKey = list.get(list.size() - 1);
            //移除key对应的在cache缓存中的对象
            V evictValue = cache.remove(evictKey);

            //创建结果返回对象
            result = new CacheEntry<>(evictKey, evictValue);
        }

        log.debug("CacheEvictLru return result = {}", result);

        return result;
    }

    //定义更新key的函数
    @Override
    public void updateKey(K key) {
        //移除最后一个key
        this.list.remove(key);
        //将本次使用的key放到最前边 index = 0 的位置
        this.list.add(0, key);
    }


    //重写removeKey方法
    @Override
    public void removeKey(K key) {
        this.list.remove(key);
    }
}
