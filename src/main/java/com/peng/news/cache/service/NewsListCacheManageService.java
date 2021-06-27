package com.peng.news.cache.service;

import com.peng.news.model.dto.NewsListDTO;

/**
 * 新闻列表缓存管理服务
 * @author PENG
 * @version 1.0
 * @date 2021/6/27 17:25
 */
public interface NewsListCacheManageService {
    /**
     * 从缓存中获取当前页的新闻列表
     * @param colId 表示查询哪个栏目
     * @param page 表示查询第几页
     * @return
     */
    NewsListDTO getCurPageNewsListCache(int colId, int page);

    /**
     * 把colId对应的栏目的第page页数据加入到缓存中
     * @param colId
     * @param page
     * @param newsListDTO 待缓存的数据
     */
    void putCurPageNewsListCache(int colId, int page, NewsListDTO newsListDTO);

    /**
     * 清空colId对应栏目的所有新闻列表缓存
     * @param colId
     */
    void clearColumnNewsListCache(int colId);
}
