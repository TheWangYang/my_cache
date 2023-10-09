package com.github.thewangyang.my_cache.core.support.listener.remove;


import com.github.thewangyang.my_cache.api.ICacheRemoveListener;

import java.util.ArrayList;
import java.util.List;

//缓存删除监听类
public class CacheRemoveListeners {

    private CacheRemoveListeners(){

    }

    //默认监听类
    public static <K, V> List<ICacheRemoveListener<K, V>> defaults(){
        List<ICacheRemoveListener<K, V>> listeners =new ArrayList<>();
        listeners.add(new CacheRemoveListener<K, V>());
        return listeners;
    }

}
