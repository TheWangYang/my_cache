package com.github.thewangyang.my_cache.core.bs;


import com.github.thewangyang.my_cache.api.ICache;
import org.junit.Assert;
import org.junit.Test;


// 缓存引导类测试
public class CacheBsTest {

    @Test
    public void helloTest(){
        // 创建ICache对象
        ICache<String, String> cache = CacheBs.<String,String>newInstance().size(2).build();

        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");

        Assert.assertEquals(2, cache.size());
        System.out.println(cache.keySet());
    }

}
