package com.github.thewangyang.my_cache.core.support.expire;


import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheExpire;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 缓存过期
// 策略：排序实现
public class CacheExpireSort<K, V> implements ICacheExpire<K, V> {

    private final Log log = LogFactory.getLog(CacheExpireSort.class);

    // 定义单次清空的数量限制
    private static final int LIMIT = 100;

    // 排序缓存存储Map
    // 按照时间排序
    // key为Long类型时间
    // value为List<K>
    private final Map<Long, List<K>> sortMap = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            //做强制转换
            return (int)(o1 - o2);
        }
    });


    // 定义过期Map
    private final Map<K, Long> expireMap = new HashMap<>();

    // 定义缓存实现
    private final ICache<K, V> cache;

    // 定义线程执行类
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireSort(ICache<K, V> cache){
        this.cache = cache;
    }


    //初始化任务
    private void init(){
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    // 实现Runnable接口
    public class ExpireThread implements Runnable{

        @Override
        public void run() {
            //实现过期策略
            //首先判断是否为空
            if(MapUtil.isEmpty(sortMap)){
                return ;
            }

            int count = 0;

            //循环sortMap，按照时间顺序删除
            for(Map.Entry<Long, List<K>> entry: sortMap.entrySet()){

                //首先得到过期时间
                long expireAt = entry.getKey();

                //得到当前过期时间对应的keys
                List<K> keyList = entry.getValue();

                //判断对应当前过期时间的队列是否为空
                if(CollectionUtil.isEmpty(keyList)){
                    // 在sortMap中移除当前的keyList
                    sortMap.remove(expireAt);

                    // 继续
                    continue;
                }

                //判断单次删除的数量是否超过了LIMIT
                //如果超过了限制
                if(count >= LIMIT){
                    return ;
                }

                //执行删除的逻辑
                long currentTime = System.currentTimeMillis();

                //如果当前时间超过了过期时间，需要执行删除逻辑
                if(expireAt >= currentTime){
                    //遍历keyList
                    //获得迭代器
                    Iterator<K> iterator = keyList.iterator();

                    //遍历keyList数组
                    while(iterator.hasNext()){
                        //获得当前过期时间对应的key
                        K key = iterator.next();
                        //从list中移除
                        iterator.remove();
                        //从expireMap中移除key
                        expireMap.remove(key);
                        //从缓存中移除
                        cache.remove(key);

                        count++;
                    }
                }else{
                    //没有过期，那么直接return
                    return ;
                }
            }
        }
    }


    @Override
    public void expire(K key, long expireAt) {
        //由sortMap得到List<K>
        List<K> keyList = sortMap.get(expireAt);

        if(keyList == null){
            //创建新的keyList
            keyList = new ArrayList<>();
        }

        //将key添加到keyList
        keyList.add(key);

        //在sortMap和expireMap中设置对应的信息
        sortMap.put(expireAt, keyList);
        expireMap.put(key, expireAt);

    }


    //刷新expire过期缓存map
    @Override
    public void refreshExpire(Collection<K> keyList) {
        //首先判断是否为空，为空直接返回
        if(CollectionUtil.isEmpty(keyList)){
            return ;
        }

        //判断expireMap的size和keyList的size
        final int expireSize = expireMap.size();
        if(expireSize <= keyList.size()) {
            // 一般过期的数量都是较少的
            for(Map.Entry<K,Long> entry : expireMap.entrySet()) {
                K key = entry.getKey();

                // 这里直接执行过期处理，不再判断是否存在于集合中。
                // 因为基于集合的判断，时间复杂度为 O(n)
                this.removeExpireKey(key);
            }
        } else {
            for(K key : keyList) {
                this.removeExpireKey(key);
            }
        }

    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }

    //定义的移除key的函数
    private void removeExpireKey(K key){
        //使用expireMap根据key得到过期时间
        Long expireAt = expireMap.get(key);

        if(expireAt != null){
            final long currentTime = System.currentTimeMillis();

            if(expireAt >= currentTime){
                expireMap.remove(key);

                List<K> expireKeys = sortMap.get(expireAt);
                expireKeys.remove(key);
                sortMap.put(expireAt, expireKeys);
            }
        }
    }
}
