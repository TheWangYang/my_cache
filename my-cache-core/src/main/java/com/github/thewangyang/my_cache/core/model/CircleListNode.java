package com.github.thewangyang.my_cache.core.model;


import javafx.scene.shape.Circle;

//定义循环链表结点类
public class CircleListNode<K, V> {

    //定义K类型的key
    public K key;

    //定义V类型的value
    public V value = null;

    //定义是否被访问过
    public boolean accessFlag = false;

    //定义前一个结点
    public CircleListNode<K, V> pre;

    //定义后一个结点
    public CircleListNode<K, V> next;

    //定义构造函数
    public CircleListNode(K key){
        this.key = key;
    }

    //定义getter函数
    public K key(){
        return key;
    }

    //定义返回对象的getter函数，参数为key
    public CircleListNode<K, V> key(K key){
        this.key = key;
        return this;
    }

    //定义返回对象的getter函数，参数为value
    public V value() {
        return value;
    }

    public CircleListNode<K, V> value(V value) {
        this.value = value;
        return this;
    }

    //返回是否被访问变量函数
    public boolean accessFLag(){
        return accessFlag;
    }

    //参数为accessFlag函数，返回this
    public CircleListNode<K, V>  accessFlag(boolean accessFlag){
        this.accessFlag = accessFlag;
        return this;
    }

    public CircleListNode<K, V> pre() {
        return pre;
    }

    public CircleListNode<K, V> pre(CircleListNode<K, V> pre) {
        this.pre = pre;
        return this;
    }

    public CircleListNode<K, V> next() {
        return next;
    }

    public CircleListNode<K, V> next(CircleListNode<K, V> next) {
        this.next = next;
        return this;
    }


    //重写toString函数
    @Override
    public String toString(){
        return "CircleListNode{" +
                "key=" + key +
                ", value=" + value +
                ", accessFlag=" + accessFlag +
                ", pre=" + pre +
                ", next=" + next +
                '}';
    }

}
