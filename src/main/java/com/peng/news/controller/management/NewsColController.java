package com.peng.news.controller.management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新闻栏目管理的接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:35
 */
@RestController
@RequestMapping("/management/column")
public class NewsColController {

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }
}
