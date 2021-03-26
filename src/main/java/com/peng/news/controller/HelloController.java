package com.peng.news.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 15:40
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/admin")
    public String helloAdmin(){
        return "hello admin";
    }

    @GetMapping("/editor")
    public String helloEditor(){
        return "hello editor";
    }

    @GetMapping("/deliverer")
    public String helloDeliverer(){
        return "hello deliverer";
    }
}
