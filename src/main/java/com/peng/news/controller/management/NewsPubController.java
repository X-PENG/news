package com.peng.news.controller.management;

import org.springframework.web.bind.annotation.*;

/**
 * 新闻发布相关接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:44
 */
@RestController
@RequestMapping("/management/news/pub")
public class NewsPubController {

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello[Get]";
    }

    @PostMapping("/hello")
    public String helloPost(){
        return this.getClass().getName() + " hello[Post]";
    }

    @PutMapping("/hello")
    public String helloPut(){
        return this.getClass().getName() + " hello[Put]";
    }

    @DeleteMapping("/hello")
    public String helloDelete(){
        return this.getClass().getName() + " hello[Delete]";
    }



}
