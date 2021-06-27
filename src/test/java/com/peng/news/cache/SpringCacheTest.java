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
        cacheManager.getCache("test").put(1, 1);
        cacheManager.getCache("test").put(2, 2);
        cacheManager.getCache("test").put(2, 3);
        cacheManager.getCache("test").put(3, 4);
        cacheManager.getCache("test").put(4, 5);
        cacheManager.getCache("test1").put(1, 1);
        cacheManager.getCache("test1").put(2, 2);
        cacheManager.getCache("test1").put(2, 3);
        cacheManager.getCache("test1").put(3, 4);
        cacheManager.getCache("test1").put(4, 5);
    }

    @Test
    public void t2() {
        cacheManager.getCache("test1").clear();
    }
}
