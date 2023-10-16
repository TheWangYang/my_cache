package com.github.thewangyang.my_cache.core.support.load;


import com.alibaba.fastjson.JSON;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.lang.reflect.ReflectMethodUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.annotation.CacheInterceptor;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheLoad;
import com.github.thewangyang.my_cache.core.core.Cache;
import com.github.thewangyang.my_cache.core.model.PersistAofEntry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 从aof加载缓存对象策略
public class CacheLoadAof<K,V> implements ICacheLoad<K, V> {

    private static final Log log = LogFactory.getLog(CacheLoadAof.class);


    // 方法缓存加载
    private static final Map<String, Method> METHOD_MAP = new HashMap<>();

    // 编写静态方法
    // 在类加载时会执行一次
    /**
    * 该段代码用于通过反射机制，
    * 检查 Cache 类中的所有公共方法是否带有 CacheInterceptor 注解，如果有且注解中 aof 函数返回 true，就将方法名和方法对象添加到一个映射中
    * */
    static{
        // 得到cache对象中的所有公共方法
        Method[] methods = Cache.class.getMethods();
        //遍历所有的方法
        for(Method m: methods){
            // 对每个方法尝试获得拦截器注解
            CacheInterceptor cacheInterceptor = m.getAnnotation(CacheInterceptor.class);
            //如果该方法上有CacheInterceptor注解
            if(cacheInterceptor != null){
                // 判断拦注解中的aof方法返回值是true还是false
                if(cacheInterceptor.aof()){
                    // 得到方法的名称
                    String methodName = m.getName();
                    //向map中添加方法名和方法
                    METHOD_MAP.put(methodName, m);
                }
            }

        }
    }

    // 得到序列化的文件路径
    private final String dbPath;

    public CacheLoadAof(String dbPath){
        this.dbPath = dbPath;
    }


    // 从dbPath路径中加载序列化的文件信息
    @Override
    public void load(ICache<K, V> cache) {
        // 读取文件中的所有行信息，放到list列表中
        List<String> lines = FileUtil.readAllLines(dbPath);
        log.info("[load] 开始处理 path: {}", dbPath);
        //判断list是否为空
        if(CollectionUtil.isEmpty(lines)){
            log.info("[load] path: {} 文件内容为空，直接返回", dbPath);
            return;
        }
        // 不为空的话，读取每一行信息
        for(String line: lines){
            // 去除空行
            if(StringUtil.isEmpty(line)){
                continue;
            }
            //反序列化操作（注意有时候会失败）
            PersistAofEntry persistAofEntry = JSON.parseObject(line, PersistAofEntry.class);
            // 得到方法名称
            final String methodName = persistAofEntry.getMethodName();
            // 得到参数对象
            final Object[] params = persistAofEntry.getParams();
            // 得到方法
            final Method method = METHOD_MAP.get(methodName);
            // 反射调用
            // 填充得到cache对象
            ReflectMethodUtil.invoke(cache, method, params);
        }
    }
}
