package com.peng.news.controller.management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新闻上传菜单相关接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:38
 */
@RestController
@RequestMapping("/management/news/upload")
public class NewsUploadController {

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }
}
