package com.github.thewangyang.my_cache.core.support.interceptor.common;


import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICacheInterceptor;
import com.github.thewangyang.my_cache.api.ICacheInterceptorContext;
import com.github.thewangyang.my_cache.api.ICacheSlowListener;
import com.github.thewangyang.my_cache.core.support.listener.slow.CacheSlowListenerContext;

import java.util.Collection;
import java.util.List;

// 定义耗时统计类
// 统计
public class CacheInterceptorCost<K, V> implements ICacheInterceptor<K, V> {
    // 定义log对象
    private static final Log log = LogFactory.getLog(CacheInterceptorCost.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("耗时开始, 方法名称为: {}", context.method().getName());
    }

    // after()方法中得到方法method()操作的耗时详情
    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        // 得到耗时时间
        long costMills = context.startMills() - context.endMills();

        // 得到cache兑现执行的方法
        String methodName = context.method().getName();

        log.debug("耗时结束, 方法名称为: {}, 耗时时间: {}ms", methodName, costMills);

        // 实现慢日志监听
        // 获得context上下文对象的cache对象中的slowListeners监听器
        List<ICacheSlowListener> slowListenerList = context.cache().slowListeners();

        // 判断slowListenerList是否为空
        if(CollectionUtil.isNotEmpty(slowListenerList)){
            // 得到CacheSlowListenerContext
            CacheSlowListenerContext cacheSlowListenerContext = CacheSlowListenerContext.newInstance().startTimeMills(context.startMills())
                    .endTimeMills(context.endMills())
                    .costTimeMills(costMills)
                    .methodName(methodName)
                    .params(context.params())
                    .result(context.result())
                    ;

            // 设置多个慢日志监听器对象，可以考虑不同的慢日志级别，做不同的处理
            for(ICacheSlowListener slowListener : slowListenerList) {
                long slowThanMills = slowListener.slowerThanMills();
                if(costMills >= slowThanMills) {
                    slowListener.listen(cacheSlowListenerContext);
                }
            }
        }

    }
}
