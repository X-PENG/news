package com.peng.news.controller.management;

import com.peng.news.model.Result;
import com.peng.news.model.po.NewsColumnPO;
import com.peng.news.service.NewsColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新闻栏目管理的接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:35
 */
@RestController
@RequestMapping("/management/column")
public class NewsColController {

    @Autowired
    NewsColumnService newsColumnService;

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }

    /**
     * 查询所有一级栏目列表，parent_id为null
     * @return
     */
    @GetMapping("/")
    public Result<List<NewsColumnPO>> newsColList(){
        return Result.success(newsColumnService.getAllColumnsByParentId(null));
    }

    /**
     * 查询某个栏目的子栏目列表
     * @return
     */
    @GetMapping("/{newsColId}")
    public Result<List<NewsColumnPO>> newsColList(@PathVariable Integer newsColId){
        return Result.success(newsColumnService.getAllColumnsByParentId(newsColId));
    }

    @PostMapping("/")
    public Result addNewsCol(@RequestBody NewsColumnPO newsColumnPO){
        newsColumnService.addNewsColumn(newsColumnPO);
        return Result.success("添加成功！");
    }

    @DeleteMapping("/{newsColId}")
    public Result delNewsCol(@PathVariable Integer newsColId){
        newsColumnService.delNewsColumn(newsColId);
        return Result.success("删除成功！");
    }

    @PutMapping("/")
    public Result updateNewsCol(@RequestBody NewsColumnPO newsColumnPO){
        newsColumnService.updateNewsColumn(newsColumnPO);
        return Result.success("修改成功！");
    }

    @PutMapping("/{newsColId}")
    public Result enableOrDisableNewsCol(@PathVariable Integer newsColId, boolean status){
        newsColumnService.enableOrDisableNewsColumn(newsColId, status);
        return Result.success(status ? "启用成功！" : "关闭成功！");
    }
}
