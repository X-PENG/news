package com.peng.news.service;

import com.peng.news.model.po.NewsPO;

/**
 * 新闻管理的公共服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/17 10:26
 */
public interface NewsPublicService {

    /**
     * 查询一个不是草稿的新闻。
     * 因为草稿是传稿人私有的，所以公共服务不能查询草稿。
     * @param newsId
     * @return
     */
    NewsPO selectOneNotDraftsNews(Integer newsId);
}
