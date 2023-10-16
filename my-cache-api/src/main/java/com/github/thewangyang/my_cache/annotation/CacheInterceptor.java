package com.github.thewangyang.my_cache.annotation;


import java.lang.annotation.*;

//定义缓存拦截器注释类型
//为拦截器提供配置
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheInterceptor {

    //设置通用拦截器
    //耗时统计
    //慢日志统计
    boolean common() default true;

    //是否开启刷新
    boolean refresh() default false;


    //操作是否需要append to file，默认为false
    boolean aof() default false;

    //是否执行删除更新
    //主要用于 LRU/LFU 等删除策略
    boolean evict() default false;

}
