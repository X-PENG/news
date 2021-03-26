package com.peng.news.controller.frontend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 15:16
 */
@RestController
public class TestController {

    @GetMapping("/cols")
    public String cols(){
        return "所有新闻栏目";
    }

    @GetMapping("/col/{colId}")
    public String hello(@PathVariable int colId){
        return "新闻栏目[" + colId + "]的列表";
    }
}
