package com.peng.news.controller.frontend;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.Result;
import com.peng.news.model.po.NewsPO;
import com.peng.news.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索新闻接口
 * @author PENG
 * @version 1.0
 * @date 2021/4/26 20:02
 */
@RestController
@RequestMapping("/frontend/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    /**
     * 搜索新闻
     * @param page
     * @param pageSize
     * @param condition
     * @return
     */
    @GetMapping("/")
    public Result<CustomizedPage<NewsPO>> selectNewsListByCondition(Integer page, Integer pageSize, String condition) {
        return Result.success(searchService.selectNewsListByCondition(page, pageSize, condition));
    }
}
