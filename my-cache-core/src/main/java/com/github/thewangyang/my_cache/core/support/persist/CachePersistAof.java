package com.github.thewangyang.my_cache.core.support.persist;


import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import com.github.thewangyang.my_cache.api.ICache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


// 创建的CachePersistAof类
// 定义cache的持久化模式类
// 将cache缓存持久化
public class CachePersistAof<K, V> extends CachePersistAdaptor<K, V>{
    // 定义log对象
    private static final Log log = LogFactory.getLog(CachePersistAof.class);

    // 定义缓存列表
    // 之后无法再修改
    private final List<String> bufferList = new ArrayList<>();

    // 定义cache持久话的数据路径
    private final String dbPath;


    // 构造函数
    public CachePersistAof(String dbPath){
        // 创建CachePersistAof对象时赋值，之后不能再更改
        this.dbPath = dbPath;
    }

    // 重写持久化方法
    @Override
    public void persist(ICache<K, V> cache) {
        log.info("开始 AOF 持久化到文件");
        // 首先判断是否存在dbPath
        if(!FileUtil.exists(this.dbPath)){
            // 不存在的话创建
            FileUtil.createFile(dbPath);
        }else{
            // 存在文件的话，直接追加到dbPath对象的文件中
            FileUtil.append(dbPath, bufferList);

            // 持久化之后，清空bufferList列表
            bufferList.clear();

            log.info("完成 AOF 持久化到文件");
        }
    }


    @Override
    public long delay() {
        return 1;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }


    // 添加文件到bufferList中
    public void append(final String json){
        if(StringUtil.isNotEmpty(json)){
            bufferList.add(json);
        }
    }

}
