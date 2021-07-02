package com.peng.news.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/6/27 0:29
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringCacheTest {
    @Autowired
    CacheManager cacheManager;

    @Test
    public void t1() {
        cacheManager.getCache("test").put("a::1", "a1");
        cacheManager.getCache("test").put("a::2", "a2");
        cacheManager.getCache("test").put("a::3", "a3");
        cacheManager.getCache("test").put("a::b::1", "a1");
        cacheManager.getCache("test").put("a::b::2", "a2");
        cacheManager.getCache("test").put("a::b::3", "a3");
        cacheManager.getCache("test").put("b::1", "b1");
        cacheManager.getCache("test").put("b::2", "b2");
        cacheManager.getCache("test").put("b::3", "b3");
        cacheManager.getCache("test::b").clear();//可删除test::b下的所有key
        cacheManager.getCache("test::a::b").clear();//可删除test::a::b下的所有key
    }

    @Test
    public void t2() {
        cacheManager.getCache("test::b").clear();
    }
}
