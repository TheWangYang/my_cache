package com.github.thewangyang.my_cache.core.support.struct.lru.impl;


import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.core.exception.CacheRuntimeException;
import com.github.thewangyang.my_cache.core.model.CacheEntry;
import com.github.thewangyang.my_cache.core.model.DoubleListNode;
import com.github.thewangyang.my_cache.core.support.struct.lru.ILruMap;

import java.util.HashMap;
import java.util.Map;


//实现的lru双向链表类
public class LruMapDoubleList<K, V> implements ILruMap<K, V> {

    //定义log对象
    private final Log log = LogFactory.getLog(LruMapDoubleList.class);

    //定义头节点
    private DoubleListNode<K, V> head;

    //定义尾巴结点
    private DoubleListNode<K, V> tail;

    //定于索引indexMap
    private Map<K, DoubleListNode<K, V>> indexMap;

    //实现构造函数
    public LruMapDoubleList(){
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();
        this.indexMap = new HashMap<>();

        //head结点的next指针指向tail结点
        this.head.next(this.tail);
        //尾结点的pre指针指向head结点
        this.tail.pre(this.head);
    }


    //移除最老的
    @Override
    public ICacheEntry<K, V> removeEldest() {
        //获得尾巴结点的前一个元素
        DoubleListNode<K, V> tailPreNode = tail.pre();

        //判断是否为head结点，如果为head结点，那么log打印日志：当前列表为空，无法删除
        if(tailPreNode == this.head){
            log.debug("当前列表为空，无法删除");
            throw new CacheRuntimeException("不可删除头结点!");
        }

        //否则执行删除
        K evictKey = tailPreNode.key();
        V evictValue = tailPreNode.value();

        //执行删除
        this.removeKey(evictKey);

        //创建删除的结点
        ICacheEntry<K, V> result = new CacheEntry<>(evictKey, evictValue);

        return result;
    }

    //更新key函数
    @Override
    public void updateKey(K key) {
        //首先执行removeKey()
        this.removeKey(key);

        //然后，获得indexMap中是否有DoubleListNode<K, V>
        DoubleListNode<K, V> newNode = new DoubleListNode<>();
        //给新创建的结点中的key赋值
        newNode.key(key);

        //获得head的next结点
        DoubleListNode<K, V> oldHeadNextNode = this.head.next();
        //将新增加的结点添加到head和原来的this.head.next结点之间
        this.head.next(newNode);
        newNode.pre(this.head);

        oldHeadNextNode.pre(newNode);
        newNode.next(oldHeadNextNode);

        //插入到indexMap中
        indexMap.put(key, newNode);
    }


    //重写移除key函数
    @Override
    public void removeKey(K key) {
        //首先通过indexMap获得结点
        DoubleListNode<K, V> node = indexMap.get(key);

        //判断该结点是否存在
        //不存在直接return
        if(ObjectUtil.isNull(node)){
            return ;
        }

        //如果存在，删除node
        //首先获得node的下一个结点
        DoubleListNode<K, V> nextNode = node.next();
        //然后获得node的上一个结点
        DoubleListNode<K, V> preNode = node.pre();

        //执行删除node
        nextNode.pre(preNode);
        preNode.next(nextNode);

        //删除indexMap中的信息
        indexMap.remove(key);

        log.debug("从 LruMapDoubleList 中移除 key: {}", key);

    }

    @Override
    public boolean isEmpty() {
        return this.indexMap.isEmpty();
    }

    @Override
    public boolean contains(K key) {
        return indexMap.containsKey(key);
    }
}
