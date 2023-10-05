package com.github.thewangyang.my_cache.core.model;


import java.awt.*;
import java.util.Objects;

//定义包含频率信息的节点
public class FreqNode<K, V> {

    //定义成员变量
    public K key;

    public V value;

    //定义结点频率
    public int frequency = 1;

    //定义构造函数
    public FreqNode(K key){
        this.key = key;
    }

    //getter方法
    public K key(){
        return key;
    }

    public FreqNode<K, V> key(K key){
        this.key = key;
        return this;
    }

    public V value(){
        return value;
    }

    public FreqNode<K, V> value(V value){
        this.value = value;
        return this;
    }

    public int frequency(){
        return frequency;
    }


    public FreqNode<K, V> frequency(int frequency){
        this.frequency = frequency;
        return this;
    }

    //重写toString()方法
    @Override
    public String toString(){
        return "FreqNode{" +
                "key=" + key +
                ", value=" + value +
                ", frequency=" + frequency +
                '}';
    }


    //重写equals函数
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }

        if(o == null || this.getClass() != o.getClass()){
            return false;
        }

        //做强制转换
        FreqNode<?, ?> freqNode = (FreqNode<?, ?>)o;

        return frequency == freqNode.frequency && Objects.equals(key, freqNode.key) && Objects.equals(value, freqNode.value);
    }

    @Override
    public int hashCode() {
        //重写hashCode函数
        return Objects.hash(key, value, frequency);
    }
}
