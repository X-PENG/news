package com.peng.news.service;

import com.peng.news.model.dto.NewsListDTO;

/**
 * 门户网站的新闻列表服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 19:46
 */
public interface NewsListService {

    NewsListDTO newsListByColId(int colId, Integer page, Integer pageSize);
}
