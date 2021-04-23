package com.peng.news.controller.frontend;

import com.peng.news.model.Result;
import com.peng.news.model.dto.NewsListDTO;
import com.peng.news.service.NewsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 19:41
 */
@RestController
@RequestMapping("/frontend/newsList")
public class NewsListController {

    @Autowired
    NewsListService newsListService;

    /**
     * 分页查询一个栏目的新闻列表
     * @param colId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/{colId}")
    public Result<NewsListDTO> newsListByColId(@PathVariable int colId, Integer page, Integer pageSize) {
        return Result.success(newsListService.newsListByColId(colId, page, pageSize));
    }
}
