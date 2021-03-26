package com.peng.news.controller.management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新闻审核接口，{epoch}表示第几轮审核
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:40
 */
@RestController
@RequestMapping("/management/news/review/{epoch}")
public class NewsReviewController {


    @GetMapping("/hello")
    public String hello(@PathVariable int epoch){
        return this.getClass().getName() + " hello[" + epoch + "]";
    }
}
