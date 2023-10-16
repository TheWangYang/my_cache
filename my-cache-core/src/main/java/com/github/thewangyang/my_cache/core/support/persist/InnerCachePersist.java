package com.github.thewangyang.my_cache.core.support.persist;


import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICachePersist;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

// 内部缓存持久化类实现
public class InnerCachePersist<K, V>{

    private static final Log log = LogFactory.getLog(InnerCachePersist.class);

    // 定义缓存信息类
    private final ICache<K, V> cache;

    // 定义CachePersist缓存持久化策略类
    private final ICachePersist<K, V> cachePersist;

    // 创建线程执行类
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    private InnerCachePersist(ICache<K, V> cache, ICachePersist<K, V> persist){
        this.cache = cache;
        this.cachePersist = persist;

        this.init();
    }


    // 定义init函数，初始化线程执行
    private void init(){
        EXECUTOR_SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("开始持久化缓存信息");
                    cachePersist.persist(cache);
                    log.info("完成持久化缓存信息");
                } catch (Exception exception) {
                    log.error("文件持久化异常", exception);
                }
            }
        }, cachePersist.delay(), cachePersist.period(), cachePersist.timeUnit());
    }



}
