package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.NewsPO;

/**
 * 搜索新闻服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/26 20:11
 */
public interface SearchService {
    CustomizedPage<NewsPO> selectNewsListByCondition(Integer page, Integer pageSize, String condition);
}
