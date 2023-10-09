package com.github.thewangyang.my_cache.core.support.listener.remove;


import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICacheRemoveListener;
import com.github.thewangyang.my_cache.api.ICacheRemoveListenerContext;

//定义cache对象的监听器类
public class CacheRemoveListener<K, V> implements ICacheRemoveListener<K, V> {

    private static final Log log = LogFactory.getLog(CacheRemoveListener.class);

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.debug("Remove key: {}, value: {}, type: {}",
                context.key(), context.value(), context.type());
    }

}

