package com.github.thewangyang.my_cache.core.support.evict;


import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvict;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;
import com.github.thewangyang.my_cache.core.model.CacheEntry;

import java.util.LinkedHashMap;
import java.util.Map;

//定义使用双向链表和LinkedHashMap类
//实现丢弃策略：最近最少使用
//直接实现了ICacheEvict接口
//使用LinkedHashMap实现该策略的实现类
public class CacheEvictLruLinkedHashMap<K, V> extends LinkedHashMap<K, V> implements ICacheEvict<K, V> {

    //定义log日志
    private final Log log = LogFactory.getLog(CacheEvictLruLinkedHashMap.class);

    //定义是否移除结点元素标识
    //告诉编译器这个变量可能被多线程访问
    private volatile boolean removeFlag = false;

    //定义最旧的一个元素
    //使用transient进行修饰时，表示该变量不会被序列化
    private transient Map.Entry<K, V> eldest = null;

    //构造函数
    public CacheEvictLruLinkedHashMap(){
        //继承自父的构造函数
        super(16, 0.75f, true);
    }

    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        //首先定义返回结果变量result
        ICacheEntry<K, V> result = null;

        //得到上下文中包含的cache对象
        final ICache<K, V> cache = context.cache();

        //判断是否超过了context.size()
        if(cache.size() >= context.size()){
            //设置removeFlag = true
            removeFlag = true;

            //执行put操作
            super.put(context.key(), null);

            //构建被淘汰的元素
            K evictKey = eldest.getKey();
            V evictValue = cache.remove(evictKey);

            result = new CacheEntry<>(evictKey, evictValue);
        }else{
            removeFlag = false;
        }

        return result;
    }


    //重写removeEldestEntry对象
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        //对eldest重新赋值
        this.eldest = eldest;
        return removeFlag;
    }

    @Override
    public void updateKey(K key) {
        super.put(key, null);
    }


    @Override
    public void removeKey(K key) {
        super.remove(key);
    }
}
