package com.github.thewangyang.my_cache.core.support.struct.lru.impl;


import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.core.exception.CacheRuntimeException;
import com.github.thewangyang.my_cache.core.model.CacheEntry;
import com.github.thewangyang.my_cache.core.model.CircleListNode;
import com.github.thewangyang.my_cache.core.support.struct.lru.ILruMap;

import java.util.HashMap;
import java.util.Map;


//实现LruMapCircleList类
//基于循环链表实现LRU方法
//实现的循环链表类
public class LruMapCircleList<K, V> implements ILruMap<K, V> {

    //定义日志变量
    private static final Log log = LogFactory.getLog(LruMapCircleList.class);

    //定义循环头结点
    public CircleListNode<K, V> head;

    //定义映射Map
    //key为K，value为CircleListNode<K, V>结点对象
    public Map<K, CircleListNode<K, V>> indexMap;


    //定义构造函数
    public LruMapCircleList(){
        this.head = new CircleListNode<>(null);
        this.head.next = this.head;
        this.head.pre = this.head;

        this.indexMap = new HashMap<>();
    }

    //删除最老的元素
    @Override
    public ICacheEntry<K, V> removeEldest() {

        if(isEmpty()){//如果为空，则提示无法删除
            log.error("当前链表为空，无法删除");
            throw new CacheRuntimeException("不可删除头结点");
        }

        //定义循环链表头结点
        CircleListNode<K, V> node = this.head;

        //循环删除最老的结点
        while(node.next != this.head){

            //获得当前node结点的下一个结点
            node = node.next();

            //如果node的下一个结点未访问
            if(!node.accessFLag()){
                //得到当前的key
                K key = node.key();

                //删除key
                this.removeKey(key);

                //返回删除的结点
                return CacheEntry.of(key, node.value());
            }else{
                //将结点的访问变量设置为false
                node.accessFlag(false);
            }
        }

        //如果while循环中没有找到对应的元素
        //那么直接返回第一个元素即可
        CircleListNode<K, V> firstNode = this.head.next();
        //返回CacheEntry对象
        return CacheEntry.of(firstNode.key(), firstNode.value());

    }


    //放入元素
    //类似于FIFO
    @Override
    public void updateKey(final K key) {
        //首先通过indexMap得到对应的结点
        CircleListNode<K, V> node = indexMap.get(key);

        //然后判断是否为null
        if(ObjectUtil.isNotNull(node)){
            //如果不为null，那么设置访问变量为1
            node.accessFlag = true;
            log.debug("节点已存在，设置节点访问标识为 true, key: {}", key);
        }else{
            //创建新元素
            node = new CircleListNode<>(key);

            //添加元素
            //得到尾结点
            CircleListNode<K, V> tail = this.head.pre();
            //在尾结点之后添加元素
            tail.next(node);
            node.pre(tail);
            node.next(this.head);
            this.head.pre(node);

            //将新结点放入到indexMap中
            indexMap.put(key, node);

            //打印日志
            log.debug("结点不存在，添加新结点到链表中，key = {}", key);
        }

    }

    //移除元素函数
    @Override
    public void removeKey(final K key) {
        //首先获得结点
        CircleListNode<K, V> node = indexMap.get(key);

        //然后判断是否为null
        if(ObjectUtil.isNull(node)){
            //如果为null
            log.warn("对应节点不存在，key = {}", key);
            return ;
        }else{
            //如果不为null
            CircleListNode<K, V> pre = node.pre();
            CircleListNode<K, V> next = node.next();

            //删除node结点
            pre.next(next);
            next.pre(pre);

            //删除key
            indexMap.remove(key);

            log.debug("key = {} 从循环链表中删除", key);
        }
    }


    //判断是否为空
    @Override
    public boolean isEmpty() {
        return indexMap.isEmpty();
    }


    //判断是否包含key值
    @Override
    public boolean contains(final K key) {
        return indexMap.containsKey(key);
    }
}
