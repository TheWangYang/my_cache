package com.github.thewangyang.my_cache.core.support.evict;


import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;
import com.github.thewangyang.my_cache.core.exception.CacheRuntimeException;
import com.github.thewangyang.my_cache.core.model.CacheEntry;
import com.github.thewangyang.my_cache.core.model.DoubleListNode;

import java.util.HashMap;
import java.util.Map;

//实现淘汰策略-最近最少使用策略
//使用双端链表实现
public class CacheEvictLruDoubleListMap<K, V> extends AbstractCacheEvict<K, V>{

    private final Log log = LogFactory.getLog(CacheEvictLruDoubleListMap.class);

    //定义头结点
    private DoubleListNode<K, V> head;

    //定义尾结点
    private DoubleListNode<K, V> tail;

    //定义indexMap保存key-node的对应关系
    private Map<K, DoubleListNode<K, V>> indexMap;

    //实现构造函数
    public CacheEvictLruDoubleListMap(){
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();

        this.indexMap = new HashMap<>();
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        ICache<K, V> cache = context.cache();

        if(cache.size() >= context.size()){
            //获得尾巴节点的前一个元素结点
            DoubleListNode<K, V> tailPre = this.tail.pre();

            //判断是否为head结点
            if(tailPre == this.head){
                log.error("当前列表为空，无法进行删除");
                throw new CacheRuntimeException("不可删除头结点!");//扔出异常之后即return
            }

            //获得删除结点的key和value
            K evictKey = tailPre.key();
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }


    //重写放入元素函数
    @Override
    public void updateKey(K key) {
        //不管有没有，直接执行removeKey()函数
        this.removeKey(key);

        //直接插入到head结点之后
        DoubleListNode<K, V> newNode = new DoubleListNode<>();
        newNode.key(key);

        DoubleListNode<K, V> headNextNode = this.head.next();

        this.head.next(newNode);
        newNode.pre(this.head);

        headNextNode.pre(newNode);
        newNode.next(headNextNode);

        //放入到indexMap中
        indexMap.put(key, newNode);
    }


    @Override
    public void removeKey(K key) {
        //首先从indexMap中得到对应的结点，并判断是否存在
        DoubleListNode<K, V> node = this.indexMap.get(key);

        if(ObjectUtil.isNull(node)){
            return ;
        }

        //删除结点
        DoubleListNode<K, V> pre = node.pre();
        DoubleListNode<K, V> next = node.next();

        pre.next(next);
        next.pre(pre);

        //移除indexMap中的key对应的结点
        this.indexMap.remove(key);

    }
}
