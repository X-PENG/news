package com.peng.news.controller.management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 11:29
 */
@RestController
@RequestMapping("/management")
public class ManagementTestController {
    @GetMapping("/hello1")
    public String hello1(){
        return this.getClass().getName() + " hello[1]";
    }

    @GetMapping("/hello2")
    public String hello2(){
        return this.getClass().getName() + " hello[2]";
    }
}
