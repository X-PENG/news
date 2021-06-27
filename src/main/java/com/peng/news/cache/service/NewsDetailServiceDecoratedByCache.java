package com.peng.news.cache.service;

import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.NewsDetailService;

/**
 * 被缓存增强的NewsDetailService
 * @author PENG
 * @version 1.0
 * @date 2021/6/24 16:33
 */
public interface NewsDetailServiceDecoratedByCache extends NewsDetailService {

    /**
     * 从缓存中查询id为newsId的新闻详情
     * @param newsId
     * @return
     */
    NewsVO getHotNewsCache(int newsId);

    /**
     * 添加热点新闻缓存
     * @param news 要缓存的新闻
     */
    void putHotNewsCache(NewsVO news);

    /**
     * 从缓存中查询新闻阅读量
     * @param newsId
     * @return
     */
    int getNewsReadingCountCache(int newsId);

    /**
     * 累加缓存的新闻阅读量
     * @param newsId
     */
    void increaseNewsReadingCountCache(int newsId);
}
