package com.github.thewangyang.my_cache.core.support.expire;


import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheExpire;
import com.github.thewangyang.my_cache.api.ICacheRemoveListener;
import com.github.thewangyang.my_cache.api.ICacheRemoveListenerContext;
import com.github.thewangyang.my_cache.core.constant.enums.CacheRemoveType;
import com.github.thewangyang.my_cache.core.support.listener.remove.CacheRemoveListenerContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//缓存过期
//普通策略
public class CacheExpire<K, V> implements ICacheExpire<K, V> {

    //设置单次清空的数量限制
    private static final int LIMIT = 100;

    //定义过期Map对象
    private Map<K, Long> expireMap = new HashMap<>();

    //缓存实现
    private final ICache<K, V> cache;

    //定义线程执行类
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    //构造函数
    public CacheExpire(ICache<K, V> cache){
        this.cache = cache;
        this.init();//定义的初始化方法
    }

    //初始化任务
    private void init(){
        //创建新的线程执行ExpireThread类中的run()函数
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    //实现Runnable接口的ExpireThread类
    public class ExpireThread implements Runnable{

        @Override
        public void run() {
            //判断是否map工具类是否为空
            if(MapUtil.isEmpty(expireMap)){
                return ;
            }

            //获得key进行处理
            //定义count变量记录清空的数量
            int count = 0;
            //使用for循环遍历expireMap中的key
            for(Map.Entry<K, Long> entry: expireMap.entrySet()){

                //如果超过了单次删除的数量
                if(count >= LIMIT){
                    return ;
                }

                //调用过期expireKey()函数清空key
                expireKey(entry.getKey(), entry.getValue());
                count ++;
            }
        }
    }


    @Override
    public void expire(K key, long expireAt) {
        //调用expireMap的put函数
        expireMap.put(key, expireAt);
    }

    //刷新过期时间
    @Override
    public void refreshExpire(Collection<K> keyList) {
        //首先判断keyList是否为空
        if(CollectionUtil.isEmpty(keyList)){
            return ;
        }

        //判断keyList的size是否超过了expireMap的size
        if(keyList.size() <= expireMap.size()){
            //删除keyList中的key
            for(K key: keyList){
                //由expireMap得到expireAt
                Long expireAt = expireMap.get(key);
                expireKey(key, expireAt);
            }
        }else{
            //此时keyList的size超过了expireMap的size，那么删除expireMap中的key
            for(Map.Entry<K, Long> entry: expireMap.entrySet()){
                expireKey(entry.getKey(), entry.getValue());
            }
        }
    }


    //获得过期时间
    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }

    //编写的expireKey()函数
    //参数为过期的key和期望过期的时间
    private void expireKey(K key, final Long expireAt){
        //首先判断过期时间是否为null，如果为null直接return
        if(expireAt == null){
            return ;
        }

        //得到当前的时间戳
        long currentTime = System.currentTimeMillis();

        //然后判断当前时间expireAt对应的是否过期
        if(expireAt >= currentTime){
            //移除key
            expireMap.remove(key);

            //再移除缓存
            V expireValue = cache.remove(key);

            //执行淘汰监听器，监听context
            ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance().key(key).value(expireValue).type(CacheRemoveType.EXPIRE.code());

            //循环实现对监听器的设置
            for(ICacheRemoveListener<K, V> listener: cache.removeListeners()){
                listener.listen(removeListenerContext);
            }

        }
    }

}
