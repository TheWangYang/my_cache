package com.github.thewangyang.my_cache.core.support.persist;


import com.alibaba.fastjson.JSON;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.core.model.PersistRdbEntry;

import javax.swing.plaf.FileChooserUI;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

// 实现DbJson持久化策略
// 基于json的db持久化策略类
public class CachePersistDbJson<K, V> extends CachePersistAdaptor<K, V>{

    // 定义dbPath
    private final String dbPath;

    // 重写构造函数
    public CachePersistDbJson(String dbPath) {
        this.dbPath = dbPath;
    }

    // 重写持久化过程
    @Override
    public void persist(ICache<K, V> cache) {

        // 初始化一个set集合，其中放入Map.Entry对象
        Set<Map.Entry<K, V>> entrySet = cache.entrySet();

        // 创建文件
        FileUtil.createFile(dbPath);

        // 清空文件
        FileUtil.truncate(dbPath);

        // 遍历entry对象
        for (Map.Entry<K, V> entry : entrySet) {
            // 得到entry对象的key
            K key = entry.getKey();
            V value = entry.getValue();
            // 得到key对应的在cache对象中的过期时间
            Long expireTime = cache.expire().expireTime(key);
            // 持久化对象
            PersistRdbEntry<K, V> persistRdbEntry = new PersistRdbEntry<>();
            persistRdbEntry.setKey(key);
            persistRdbEntry.setExpire(expireTime);
            persistRdbEntry.setValue(value);
            // 得到持久化对象的String格式
            String line = JSON.toJSONString(persistRdbEntry);
            // 以追加模式写
            FileUtil.write(dbPath, line, StandardOpenOption.APPEND);
        }
    }

    @Override
    public long delay() {
        return 5;
    }

    @Override
    public long period() {
        return 5;
    }

    // 设置计时单位为分钟
    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }
}
