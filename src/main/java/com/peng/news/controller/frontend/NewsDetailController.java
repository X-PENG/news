package com.peng.news.controller.frontend;

import com.peng.news.cache.service.NewsDetailServiceDecoratedByCache;
import com.peng.news.model.Result;
import com.peng.news.model.vo.NewsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新闻详情相关接口
 * @author PENG
 * @version 1.0
 * @date 2021/4/24 0:15
 */
@RestController
@RequestMapping("/frontend/newDetail")
public class NewsDetailController {

    @Autowired
    NewsDetailServiceDecoratedByCache newsDetailService;

    /**
     * 查看新闻接口
     * @param newsId
     * @return
     */
    @GetMapping("/{newsId}")
    public Result<NewsVO> viewNews(@PathVariable int newsId) {
        return Result.success(newsDetailService.viewNews(newsId));
    }

}
