package com.peng.news.controller.management;

import com.peng.news.cache.service.NewsListCacheManageService;
import com.peng.news.model.Result;
import com.peng.news.model.vo.NewsColumnVO;
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

    @Autowired
    NewsListCacheManageService newsListCacheManageService;

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }

    /**
     * 查询所有一级栏目列表，parent_id为null
     * @return
     */
    @GetMapping("/")
    public Result<List<NewsColumnVO>> newsColList(){
        return Result.success(newsColumnService.getAllColumnsByParentId(null));
    }

    /**
     * 查询某个栏目的子栏目列表
     * @return
     */
    @GetMapping("/{newsColId}")
    public Result<List<NewsColumnVO>> newsColList(@PathVariable Integer newsColId){
        return Result.success(newsColumnService.getAllColumnsByParentId(newsColId));
    }

    @PostMapping("/")
    public Result addNewsCol(@RequestBody NewsColumnVO newsColumnVO){
        newsColumnService.addNewsColumn(newsColumnVO);
        return Result.success("添加成功！");
    }

    @DeleteMapping("/{newsColId}")
    public Result delNewsCol(@PathVariable Integer newsColId){
        newsColumnService.delNewsColumn(newsColId);
        return Result.success("删除成功！");
    }

    @PutMapping("/")
    public Result updateNewsCol(@RequestBody NewsColumnVO newsColumnVO){
        newsColumnService.updateNewsColumn(newsColumnVO);
        newsListCacheManageService.clearColumnNewsListCache(newsColumnVO.getId());
        return Result.success("修改成功！");
    }

    @PutMapping("/{newsColId}")
    public Result enableOrDisableNewsCol(@PathVariable Integer newsColId, boolean status){
        newsColumnService.enableOrDisableNewsColumn(newsColId, status);
        return Result.success(status ? "启用成功！" : "关闭成功！");
    }

    /**
     * 修改新闻栏目显示图片的状态
     * @param newsColId
     * @param status
     * @return
     */
    @PutMapping("/showImgStatus/{newsColId}")
    public Result changeNewsColShowImgStatus(@PathVariable Integer newsColId, boolean status){
        newsColumnService.changeNewsColShowImgStatus(newsColId, status);
        newsListCacheManageService.clearColumnNewsListCache(newsColId);
        return Result.success(status ? "成功设置右侧显示图片！" : "成功关闭右侧显示图片！");
    }
}
