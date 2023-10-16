package com.github.thewangyang.my_cache.core.support.listener.slow;


import com.alibaba.fastjson.JSON;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICacheSlowListener;
import com.github.thewangyang.my_cache.api.ICacheSlowListenerContext;

// 创建慢缓冲监听器类
public class CacheSlowListener implements ICacheSlowListener {
    // 定义打印log日志对象
    private static final Log log = LogFactory.getLog(CacheSlowListener.class);

    @Override
    public void listen(ICacheSlowListenerContext context) {
        // 警告标识
        log.warn("[Slow] methodName: {}, params: {}, cost time: {}",
                context.methodName(), JSON.toJSON(context.params()), context.costTimeMills());
    }

    @Override
    public long slowerThanMills() {
        return 1000L;
    }
}
