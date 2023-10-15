package com.github.thewangyang.my_cache.api;


//定义的慢日志监听类上下文接口
public interface ICacheSlowListenerContext {

    //获得方法名称
    String methodName();

    //获得参数信息
    Object[] params();

    //获得方法执行结果
    Object result();

    //获得开始时间
    long startTimeMills();

    //获得结束时间
    long endTimeMills();

    //获得消耗时间
    long costTimeMills();

}
