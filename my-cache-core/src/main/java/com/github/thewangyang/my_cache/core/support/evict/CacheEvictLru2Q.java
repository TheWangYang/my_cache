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
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


//定义使用Queue队列实现的LRU
//最近最少使用策略
public class CacheEvictLru2Q<K, V> extends AbstractCacheEvict<K, V>{

    private final Log log = LogFactory.getLog(CacheEvictLru2Q.class);

    //队列大小限制
    private static final int QUEUE_LIMIT_SIZE = 1024;

    //首次访问的队列
    private Queue<K> firstQueue;

    //定义头结点
    private DoubleListNode<K, V> head;

    //定义尾结点
    private DoubleListNode<K, V> tail;


    //定义indexMap，保存key-node的对应关系
    private Map<K, DoubleListNode<K, V>> indexMap;


    //构造函数
    public CacheEvictLru2Q(){
        this.firstQueue = new LinkedList<>();
        this.indexMap = new HashMap<>();

        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();

        this.head.next(tail);
        this.tail.pre(this.head);
    }


    //重写淘汰策略
    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        final ICache<K, V> cache = context.cache();

        if(cache.size() >= context.size()){
            //执行淘汰策略，删除队尾元素
            K evictKey = null;

            //优先移除firstQueue中队尾元素
            if(!firstQueue.isEmpty()){
                //得到首次访问的队列中的队尾元素
                evictKey = firstQueue.remove();
            }else{//队列为空
                //获取尾巴结点的前一个元素
                DoubleListNode<K, V> tailPre = tail.pre();
                //判断tailPre是否为this.head
                if(tailPre == this.head){
                    log.error("当前列表为空，无法进行删除");
                    throw new CacheRuntimeException("不可删除头结点!");
                }
                evictKey = tailPre.key();
            }

            //执行移除操作
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        return result;
    }


    //重写updateKey()函数
    //如果在indexMap中，那么先删除再插入
    //如果不在indexMap中而在firstQueue中，那么先删除，再插入
    @Override
    public void updateKey(K key) {
        DoubleListNode<K, V> node = indexMap.get(key);
        if(ObjectUtil.isNotNull(node) || firstQueue.contains(key)){
            //删除key
            this.removeKey(key);

            //加入到LRU中
            this.addToLruMapHead(key);
            return;
        }

        //加入到queue队尾
        firstQueue.add(key);
    }

    //写addToLruMapHead()函数
    private void addToLruMapHead(final K key){
        //新元素插入到头部
        DoubleListNode<K, V> newNode = new DoubleListNode<>();
        newNode.key(key);

        //得到head的下一个元素
        DoubleListNode<K, V> headNextNode = this.head.next();
        this.head.next(newNode);
        newNode.pre(this.head);
        headNextNode.pre(newNode);
        newNode.next(headNextNode);

        //插入到indexMap中
        indexMap.put(key, newNode);
    }


    //重写removeKey()函数
    @Override
    public void removeKey(final K key) {
        //获得map中的元素
        DoubleListNode<K, V> node = indexMap.get(key);
        if(ObjectUtil.isNotNull(node)){
            //删除结点
            DoubleListNode<K, V> pre = node.pre();
            DoubleListNode<K, V> next = node.next();

            //删除node结点
            pre.next(next);
            next.pre(pre);

            //删除indexMap中对应的信息
            indexMap.remove(key);
        }else{
            //如果不存在直接删除队列中的元素
            firstQueue.remove(key);
        }

    }
}
