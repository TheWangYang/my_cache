package com.github.thewangyang.my_cache.core.support.expire;


import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheExpire;
import com.github.thewangyang.my_cache.api.ICacheRemoveListener;
import com.github.thewangyang.my_cache.api.ICacheRemoveListenerContext;
import com.github.thewangyang.my_cache.core.constant.enums.CacheRemoveType;
import com.github.thewangyang.my_cache.core.support.listener.remove.CacheRemoveListenerContext;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;



//实现缓存过期随机删除类
public class CacheExpireRandom<K, V> implements ICacheExpire<K, V>{

    private final Log log = LogFactory.getLog(CacheExpireRandom.class);

    //设置单次清空的数量限制
    //设置为static表示是类变量，所有对象实例使用同一个变量
    //定义为final表示，一旦编译，该变量就不可修改了
    private static final int COUNT_LIMIT = 100;

    //定义过期map
    private final Map<K, Long> expireMap = new HashMap<>();

    //定义缓存对象
    private ICache<K, V> cache = null;

    //是否启用快模式
    private volatile boolean fastMode = false;

    //定义线程执行类
    private ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    //定义函数
    public CacheExpireRandom(ICache<K, V> cache){
        this.cache = cache;
        this.init();
    }


    //定义初始化函数
    private void init(){
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThreadRandom(), 10, 10, TimeUnit.SECONDS);
    }


    //实现Runnable接口，重写run()方法
    public class ExpireThreadRandom implements Runnable{

        @Override
        public void run() {
            //判断expireMap是否为空
            if(MapUtil.isEmpty(expireMap)){
                log.info("expireMap 信息为空，直接跳过本次处理。");
                return;
            }

            //是否启用了快模式
            if(fastMode){
                expireKeys(10L);
            }else{//使用慢模式
                expireKeys(100L);
            }
        }
    }

    //定义expireKeys()函数
    //参数为设置的过期时间
    private void expireKeys(final long timeoutMills){

        //获得从当前时间开始+timeoutMills的时间，作为该元素的过期时间
        final long timeLimit = System.currentTimeMillis() + timeoutMills;

        //关闭快模式
        this.fastMode = false;

        //设置本次清空的元素数量计数器
        int count = 0;

        //一直循环去判断是否超过了
        while(true){

            if(count >= COUNT_LIMIT){
                log.info("过期淘汰次数已经达到最大次数: {}，完成本次执行。", COUNT_LIMIT);
                return;
            }

            //当前时间超过了最长时间限制，那么中断本次执行
            if(System.currentTimeMillis() >= timeLimit) {
                this.fastMode = true;//设置快模式
                log.info("过期淘汰已经达到限制时间，中断本次执行，设置 fastMode=true;");
                return;
            }

            //随即过期
            K key = getRandomKey();

            Long expireAt = expireMap.get(key);

            //得到过期成功与否标记flag
            boolean expireFlag = expireKey(key, expireAt);

            log.debug("key: {} 过期执行结果 {}", key, expireFlag);

            count ++;
        }
    }

    //getRandomKey()函数
    //随机获得key
    private K getRandomKey(){
        Random randomKey = ThreadLocalRandom.current();

        //获得keySet
        Set<K> keySet = expireMap.keySet();

        //获得keyList
        List<K> list = new ArrayList<>(keySet);

        int randomIndex = randomKey.nextInt(list.size());

        return list.get(randomIndex);
    }


    //设置
    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }


    @Override
    public void refreshExpire(Collection<K> keyList) {
        //更新expireMap
        if(CollectionUtil.isEmpty(keyList)){
            return ;
        }

        //如果不为空
        //判断keyList和expireMap的size那个小，更新哪个
        if(keyList.size() <= expireMap.size()){
            for(K key: keyList){
                Long expireAt = expireMap.get(key);
                expireKey(key, expireAt);
            }
        }else{
            for(Map.Entry<K, Long> entry: expireMap.entrySet()){
                this.expireKey(entry.getKey(), entry.getValue());
            }
        }
    }


    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }


    //定义删除单个过期key的方法
    private boolean expireKey(K key, final Long expireAt){
        if(expireAt == null){
            return false;
        }

        //得到当前时间
        long currentTime = System.currentTimeMillis();

        if(currentTime >= expireAt){
            //当前时间超过了元素过期时间，执行移除
            expireMap.remove(key);

            V expireValue = cache.remove(key);

            //执行淘汰监听器，监听context
            ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance().key(key).value(expireValue).type(CacheRemoveType.EXPIRE.code());

            //循环实现对监听器的设置
            for(ICacheRemoveListener<K, V> listener: cache.removeListeners()){
                listener.listen(removeListenerContext);
            }

            return true;
        }

        return false;
    }


}
