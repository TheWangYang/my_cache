package com.github.thewangyang.my_cache.core.support.listener.slow;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheSlowListener;

import java.util.ArrayList;
import java.util.List;

// 实现创建监听器类
public class CacheSlowListeners {


    // 创建默认的None监听器类方法
    public static List<ICacheSlowListener> none(){
        return new ArrayList<>();
    }

    // 创建默认的慢日志监听器类方法
    public static ICacheSlowListener defaultListener(){
        return new CacheSlowListener();
    }

}
