package com.github.thewangyang.my_cache.core.support.interceptor;


import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheInterceptor;
import com.github.thewangyang.my_cache.api.ICacheInterceptorContext;
import com.github.thewangyang.my_cache.core.support.interceptor.aof.CacheInterceptorAof;
import com.github.thewangyang.my_cache.core.support.interceptor.common.CacheInterceptorCost;
import com.github.thewangyang.my_cache.core.support.interceptor.evict.CacheInterceptorEvict;
import com.github.thewangyang.my_cache.core.support.interceptor.refresh.CacheInterceptorRefresh;

import java.util.ArrayList;
import java.util.List;

// 实现拦截器类创建类
public class CacheInterceptors{

    // 得到包含默认通用的拦截器对象List
    @SuppressWarnings("all")
    public static List<ICacheInterceptor> commonInterceptorList(){
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorCost());
        return list;
    }

    @SuppressWarnings("all")
    public static List<ICacheInterceptor> refreshInterceptorList(){
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorRefresh());
        return list;
    }

    // 返回AoF持久化拦截器对象
    @SuppressWarnings("all")
    public static ICacheInterceptor aofInterceptor(){
        return new CacheInterceptorAof();
    }


    @SuppressWarnings("all")
    public static ICacheInterceptor evictInterceptor(){
        return new CacheInterceptorEvict();
    }



}
