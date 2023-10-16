package com.github.thewangyang.my_cache.core.support.map;


import java.util.HashMap;
import java.util.Map;

// Maps类
public final class Maps {
    // 默认构造函数
    public Maps(){

    }

    public static <K, V> Map<K, V> hashMap(){
        return new HashMap<>();
    }

}
