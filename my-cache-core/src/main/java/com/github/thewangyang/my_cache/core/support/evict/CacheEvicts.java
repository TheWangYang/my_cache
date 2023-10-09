package com.github.thewangyang.my_cache.core.support.evict;


import com.github.thewangyang.my_cache.api.ICacheEvict;

//将多个实现LRU策略的类进行汇总
//定义为final类，表示该类不能有任何子类
public final class CacheEvicts {

    private CacheEvicts(){

    }

    /**
     * 无策略
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.2
     * 表示该方法接受两个泛型K, V作为参数
     */

    public static <K, V> ICacheEvict<K, V> none() {
        return new CacheEvictNone<>();
    }

    //fifo策略
    //先进先出策略
    public static <K, V> ICacheEvict<K, V> fifo(){
        return new CacheEvictFifo<>();
    }


    //时钟算法
    public static <K, V> ICacheEvict<K, V> clock(){
        return new CacheEvictClock<>();
    }

    //LRU删除策略
    public static <K, V> ICacheEvict<K, V> lru(){
        return new CacheEvictLru<>();
    }

    /**
     * LRU 驱除策略
     *
     * 基于双向链表 + map 实现
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.12
     */
    public static <K, V> ICacheEvict<K, V> lruDoubleListMap() {
        return new CacheEvictLruDoubleListMap<>();
    }


    /**
     * LRU 驱除策略
     *
     * 基于LinkedHashMap
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.12
     */
    public static <K, V> ICacheEvict<K, V> lruLinkedHashMap() {
        return new CacheEvictLruLinkedHashMap<>();
    }

    /**
     * LRU 驱除策略
     *
     * 基于 2Q 实现
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.13
     */
    public static <K, V> ICacheEvict<K, V> lru2Q() {
        return new CacheEvictLru2Q<>();
    }

    /**
     * LRU 驱除策略
     *
     * 基于 LRU-2 实现
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.13
     */
    public static <K, V> ICacheEvict<K, V> lru2() {
        return new CacheEvictLru2<>();
    }

    /**
     * LFU 驱除策略
     *
     * 基于 LFU 实现
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @since 0.0.14
     */
    public static <K, V> ICacheEvict<K, V> lfu() {
        return new CacheEvictLfu<>();
    }

}
