package com.github.thewangyang.my_cache.api;


//导入java内部包含的类或接口
import java.util.List;
import java.util.Map;

//导入自定义的接口或类


//实现自己的WCache接口
//该接口用于给my-cache-core中的文件提供接口服务
public interface ICache<K, V> extends Map<K, V> {

    //定义过期处理函数
    ICache<K, V> expire(final K key, final long timeInMills);

    //定义在指定时间过期函数
    ICache<K, V> expireAt(final K key, final long timeInMills);

    //获取缓存的过期处理类
    ICacheExpire<K, V> expire();

    //定义删除监听类列表函数
    //函数返回值为删除监听类列表
    List<ICacheRemoveListener<K, V>> removeListeners();

    //慢日志监听类列表
    List<ICacheSlowListener> slowListeners();

    //加载信息
    ICacheLoad<K, V> load();

    //持久化类
    ICachePersist<K,V> persist();

    //淘汰策略
    ICacheEvict<K, V> evict();

}
