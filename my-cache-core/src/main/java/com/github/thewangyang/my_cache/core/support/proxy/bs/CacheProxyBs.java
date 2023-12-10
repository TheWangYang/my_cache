package com.github.thewangyang.my_cache.core.support.proxy.bs;


import com.github.thewangyang.my_cache.annotation.CacheInterceptor;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheInterceptor;
import com.github.thewangyang.my_cache.api.ICachePersist;
import com.github.thewangyang.my_cache.core.support.interceptor.CacheInterceptorContext;
import com.github.thewangyang.my_cache.core.support.interceptor.CacheInterceptors;
import com.github.thewangyang.my_cache.core.support.persist.CachePersistAof;

import java.util.ArrayList;
import java.util.List;

// 定义的代理引导类
public class CacheProxyBs {

    private CacheProxyBs(){

    }

    // 定义代理上下文
    private ICacheProxyBsContext context;

    // 定义默认通用拦截器
    private final List<ICacheInterceptor> commonInterceptors = CacheInterceptors.commonInterceptorList();

    // 定义默认刷新拦截器
    private final List<ICacheInterceptor> refreshInterceptors = CacheInterceptors.refreshInterceptorList();

    // 定义持久化拦截器
    private final ICacheInterceptor aofInterceptor = CacheInterceptors.aofInterceptor();

    // 定义驱除拦截器
    private final ICacheInterceptor evictInterceptor = CacheInterceptors.evictInterceptor();


    // 创建对象实例
    public static CacheProxyBs newInstance(){
        return new CacheProxyBs();
    }

    // 定义根据context返回对象的函数
    public CacheProxyBs context(ICacheProxyBsContext context){
        this.context = context;
        return this;
    }

    // 拦截器执行类
    @SuppressWarnings("all")
    public Object execute() throws Throwable{

        // 获得开始时间
        final long startMills = System.currentTimeMillis();

        // 获得cache对象
        final ICache cache = context.target();

        // 创建cache interceptor context对象
        CacheInterceptorContext cacheInterceptorContext = CacheInterceptorContext.newInstance().
                startMills(startMills).
                method(context.method()).
                params(context.params()).
                cache(context.target());

        // 获得刷新注解信息
        CacheInterceptor cacheInterceptor = context.interceptor();
        this.interceptorHandler(cacheInterceptor, cacheInterceptorContext, cache, true);

        // 正常执行
        Object result = context.process();

        final long endMills = System.currentTimeMillis();
        cacheInterceptorContext.endMills(endMills);

        this.interceptorHandler(cacheInterceptor, cacheInterceptorContext, cache, false);
        return result;

    }

    // 创建interceptorHandler方法
    @SuppressWarnings("all")
    private void interceptorHandler(CacheInterceptor cacheInterceptor,
                                    CacheInterceptorContext cacheInterceptorContext,
                                    ICache cache,
                                    boolean before) {

        // 如果interceptor不等于null
        if(cacheInterceptor != null){

            //判断使用什么类型的拦截器实现
            if(cacheInterceptor.common()){
                for(ICacheInterceptor interceptor : commonInterceptors) {
                    if(before) {
                        interceptor.before(cacheInterceptorContext);
                    } else {
                        interceptor.after(cacheInterceptorContext);
                    }
                }
            }

            //2. 刷新
            if(cacheInterceptor.refresh()) {
                for(ICacheInterceptor interceptor : refreshInterceptors) {
                    if(before) {
                        interceptor.before(cacheInterceptorContext);
                    } else {
                        interceptor.after(cacheInterceptorContext);
                    }
                }
            }

            //3. AOF 追加
            final ICachePersist cachePersist = cache.persist();
            if(cacheInterceptor.aof() && (cachePersist instanceof CachePersistAof)) {
                if(before) {
                    aofInterceptor.before(cacheInterceptorContext);
                } else {
                    aofInterceptor.after(cacheInterceptorContext);
                }
            }

            //4. 驱除策略更新
            if(cacheInterceptor.evict()) {
                if(before) {
                    evictInterceptor.before(cacheInterceptorContext);
                } else {
                    evictInterceptor.after(cacheInterceptorContext);
                }
            }

        }
    }


}
