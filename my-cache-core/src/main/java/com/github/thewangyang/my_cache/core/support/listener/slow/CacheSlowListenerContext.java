package com.github.thewangyang.my_cache.core.support.listener.slow;


import com.github.thewangyang.my_cache.api.ICacheSlowListenerContext;
import sun.misc.Cache;


// 定义慢日志监听器
// 实现ICacheSlowListenerContext接口
public class CacheSlowListenerContext implements ICacheSlowListenerContext {

    // 定义methodName
    private String methodName;

    // 定义参数信息
    private Object[] params;

    // 定义结果数组
    private Object result;

    // 定义开始时间
    private long startTimeMills;

    // 定义结束时间
    private long endTimeMills;

    // 定义耗时时间
    private long costTimeMills;


    // 创建静态类构造方法
    public static CacheSlowListenerContext newInstance(){
        return new CacheSlowListenerContext();
    }

    // 定义
    @Override
    public String methodName() {
        return methodName;
    }

    // 返回类对象的methodName函数
    public CacheSlowListenerContext methodName(String methodName){
        this.methodName = methodName;
        return this;
    }


    @Override
    public Object[] params() {
        return params;
    }

    public CacheSlowListenerContext params(Object[] params) {
        this.params = params;
        return this;
    }


    @Override
    public Object result() {
        return result;
    }

    public CacheSlowListenerContext result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startTimeMills() {
        return startTimeMills;
    }

    public CacheSlowListenerContext startTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
        return this;
    }


    @Override
    public long endTimeMills() {
        return endTimeMills;
    }

    public CacheSlowListenerContext endTimeMills(long endTimeMills) {
        this.endTimeMills = endTimeMills;
        return this;
    }


    @Override
    public long costTimeMills() {
        return costTimeMills;
    }

    public CacheSlowListenerContext costTimeMills(long costTimeMills) {
        this.costTimeMills = costTimeMills;
        return this;
    }

}
