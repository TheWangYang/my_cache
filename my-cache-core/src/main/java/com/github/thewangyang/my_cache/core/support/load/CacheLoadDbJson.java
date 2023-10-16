package com.github.thewangyang.my_cache.core.support.load;


import com.alibaba.fastjson.JSON;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.thewangyang.my_cache.api.ICache;
import com.github.thewangyang.my_cache.api.ICacheLoad;
import com.github.thewangyang.my_cache.core.model.PersistRdbEntry;

import java.util.Collection;
import java.util.List;

// 定义从DbJson加载策略类
public class CacheLoadDbJson<K, V> implements ICacheLoad<K, V> {

    private static final Log log = LogFactory.getLog(CacheLoadDbJson.class);

    private final String dbPath;

    public CacheLoadDbJson(String dbPath){
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) {
        // 加载dbPath对应的dbJson文件
        List<String> lines = FileUtil.readAllLines(this.dbPath);
        if(CollectionUtil.isEmpty(lines)){
            log.debug("CacheLoadDbJson类中从dbPath读取的dbJson数据为空");
            return ;
        }
        for(String line: lines){
            if(StringUtil.isEmpty(line)){
                continue;
            }
            // 反序列化
            PersistRdbEntry<K, V> persistRdbEntry = JSON.parseObject(line, PersistRdbEntry.class);
            K key = persistRdbEntry.getKey();
            V value = persistRdbEntry.getValue();
            // 向cache中添加key,value
            cache.put(key, value);
            // 获得过期时间
            Long expire = persistRdbEntry.getExpire();
            // 判断过期时间是否为空
            if(ObjectUtil.isNotNull(expire)) {
                cache.expireAt(key, expire);
            }
        }
    }
}
