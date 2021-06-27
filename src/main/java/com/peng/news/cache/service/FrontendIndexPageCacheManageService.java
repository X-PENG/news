package com.peng.news.cache.service;

/**
 * 前台首页头条新闻缓存管理服务
 * @author PENG
 * @version 1.0
 * @date 2021/6/23 16:05
 */
public interface FrontendIndexPageCacheManageService {

    /**
     * 删除头条缓存
     * @param expectedNewsId 若缓存的头条新闻的id为expectedNewsId，则删除头条新闻的缓存
     */
    void delHeadlinesCache(int expectedNewsId);

    /**
     * 删除轮播新闻缓存
     * @param expectedNewsId 若当前缓存的轮播新闻包含id为expectedNewsId的新闻，则删除轮播新闻的缓存
     */
    void delCarouselCache(int expectedNewsId);
}
