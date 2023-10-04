package com.github.thewangyang.my_cache.core.model;


//定义双向链表结点
public class DoubleListNode<K, V> {

    public K key;

    public V value;

    //定义前一个结点
    public DoubleListNode<K, V> pre;

    //定义后一个结点
    public DoubleListNode<K, V> next;

    //定义key的getter方法
    public K key(){
        return key;
    }

    public DoubleListNode<K, V> key(K key){
        this.key = key;
        return this;
    }

    //定义value的getter方法
    public V value(){
        return value;
    }

    public DoubleListNode<K, V> value(V value){
        this.value = value;
        return this;
    }


    //定义pre的getter方法
    public DoubleListNode<K, V> pre(){
        return pre;
    }

    public DoubleListNode<K, V> pre(DoubleListNode<K, V> pre){
        this.pre = pre;
        return this;
    }

    //定义next的getter方法
    public DoubleListNode<K, V> next(){
        return next;
    }

    public DoubleListNode<K, V> next(DoubleListNode<K, V> next){
        this.next = next;
        return this;
    }

}
