package com.peng.news.controller.management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 友情链接管理接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:46
 */
@RestController
@RequestMapping("/management/link")
public class LinkController {

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }
}
