package com.github.thewangyang.my_cache.core.support.evict;


import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheEntry;
import com.github.thewangyang.my_cache.api.ICacheEvictContext;
import com.github.thewangyang.my_cache.core.exception.CacheRuntimeException;
import com.github.thewangyang.my_cache.core.model.CacheEntry;
import com.github.thewangyang.my_cache.core.model.FreqNode;

import java.util.*;

//按照最少使用频率淘汰策略
public class CacheEvictLfu<K, V> extends AbstractCacheEvict<K, V> {

    //定义log对象
    private final Log log = LogFactory.getLog(CacheEvictLfu.class);

    //定义映射信息
    //key为key，value为频率结点
    private final Map<K, FreqNode<K, V>> keyMap;

    //定义频率Map
    private final Map<Integer, LinkedHashSet<FreqNode<K, V>>> freqMap;

    //定义最小频率
    private int minFreq;


    //定义构造函数初始化indexMap
    public CacheEvictLfu(){
        this.keyMap = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFreq = 1;//初始化频率为1
    }

    //定义

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        //定义返回的result变量
        ICacheEntry<K, V> result = null;

        //获得context上下文中保存的cache
        final ICache<K, V> cache = context.cache();

        //判断cache中是否超过了context中的size
        if(cache.size() >= context.size()){
            //得到最小频率的结点
            FreqNode<K, V> freqNode =this.minFreqNode();

            //判断是否为空，不为空进行创建Entry对象
            if(ObjectUtil.isNotNull(freqNode)){
                K evictKey = freqNode.key();
                V evictValue = freqNode.value();

                //打印log日志
                log.debug("淘汰最小频率信息, key: {}, value: {}, freq: {}",
                        evictKey, evictValue, freqNode.frequency());

                result = new CacheEntry<>(evictKey, evictValue);
            }
        }

        return result;
    }


    //定义获得最小频率结点的函数
    private FreqNode<K, V> minFreqNode(){
        //根据freqMap获得频率结点set集合
        LinkedHashSet<FreqNode<K, V>> freqNodeLinkedHashSet = freqMap.get(minFreq);

        //查看最小频率对应的set集合是不是空的
        if(CollectionUtil.isNotEmpty(freqNodeLinkedHashSet)){
            //不是空的，直接返回第一个频率为最小频率的结点
            return freqNodeLinkedHashSet.iterator().next();
        }

        //抛出异常
        throw new CacheRuntimeException("未发现最小频率的key");

    }

    //重写更新元素结点
    @Override
    public void updateKey(K key) {
        //获得key对应的在keyMap中是否存在
        FreqNode<K, V> node = keyMap.get(key);

        //分类讨论
        //1.已经存在结点
        if(ObjectUtil.isNotNull(node)){
            //移除原始结点的信息
            //获得原始结点的频率
            int frequency = node.frequency();
            //获得频率信息对应的set集合
            LinkedHashSet<FreqNode<K, V>> oldSet = freqMap.get(frequency);
            //oldSet移除node
            oldSet.remove(node);
            //更新最小数据结点的频率
            if(minFreq == frequency && oldSet.isEmpty()){
                minFreq ++;//频率增加1
                log.debug("minFreq增加为{}", minFreq);
            }
            //更新频率信息
            frequency ++;
            //更新node结点的frequency
            node.frequency(frequency);
            //增加到新的集合中
            this.addToFreqMap(frequency, node);
        }else{//表示不存在对应的结点
            //创建新结点
            FreqNode<K, V> newFreqNode = new FreqNode<>(key);
            //放入到频率为1的列表中
            this.addToFreqMap(1, newFreqNode);
            //更新最小频率为1
            this.minFreq = 1;
            //添加到keyMap中
            keyMap.put(key, newFreqNode);
        }
    }


    //实现私有的addToFreqMap()方法
    //参数为频率和频率对应的节点
    private void addToFreqMap(final int frequency, FreqNode<K, V> freqNode){
        //首先得到频率对应的frequency的set集合
        LinkedHashSet<FreqNode<K, V>> oldSet = freqMap.get(frequency);

        if(oldSet == null){
            //创建新的set集合
            oldSet = new LinkedHashSet<>();
        }
        //然后在set中添加新的结点
        oldSet.add(freqNode);

        //向freqMap中添加
        freqMap.put(frequency, oldSet);

        log.debug("frequency = {}, 添加元素节点：{}", frequency, freqNode);
    }


    //重写removeKey()函数
    @Override
    public void removeKey(final K key) {

        //得到key对应的freqNode
        FreqNode<K, V> freqNode = keyMap.get(key);

        //通过freqNode得到对应的frequency
        int frequency = freqNode.frequency();

        //通过freqMap得到对应的包含freqNode的set集合
        LinkedHashSet<FreqNode<K, V>> set = this.freqMap.get(frequency);

        //移除元素
        set.remove(freqNode);
        log.debug("frequency = {} 移除元素节点：{}", freq, freqNode);

        //更新minFreq
        if(minFreq == frequency && set.isEmpty()){
            minFreq --;
            log.debug("minFreq 降低为：{}", minFreq);
        }
    }
}
