package com.peng.news.controller.frontend;

import com.peng.news.model.Result;
import com.peng.news.model.dto.NewsListDTO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.NewsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 19:41
 */
@RestController
@RequestMapping("/frontend/newsList")
public class NewsListController {

    @Autowired
    @Qualifier("newsListServiceDecoratedByCache")
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
        //处理分页参数
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;
        return Result.success(newsListService.newsListByColId(colId, page, pageSize));
    }

    @GetMapping("/subColList/{colId}")
    public Result<List<NewsColumnVO>> subColList(@PathVariable int colId) {
        return Result.success(newsListService.subColList(colId));
    }
}
