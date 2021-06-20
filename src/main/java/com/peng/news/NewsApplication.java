package com.peng.news;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author  PENG
 * @date  2021/3/23 14:04
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.peng.news.mapper")
@EnableAsync
@EnableCaching
public class NewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsApplication.class, args);
    }

}
