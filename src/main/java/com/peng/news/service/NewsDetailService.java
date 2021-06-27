package com.peng.news.service;

import com.peng.news.model.vo.NewsVO;

/**
 * 新闻详情相关服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/24 0:16
 */
public interface NewsDetailService {

    /**
     * 查看新闻
     * @param newsId
     * @return
     */
    NewsVO viewNews(Integer newsId);

}
