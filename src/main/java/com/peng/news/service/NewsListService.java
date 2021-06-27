package com.peng.news.service;

import com.peng.news.model.dto.NewsListDTO;
import com.peng.news.model.vo.NewsColumnVO;

import java.util.List;

/**
 * 门户网站的新闻列表服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 19:46
 */
public interface NewsListService {

    /**
     * 按照栏目id查询新闻列表
     * @param colId
     * @param page
     * @param pageSize
     * @return
     */
    NewsListDTO newsListByColId(int colId, int page, int pageSize);

    /**
     * 按照栏目id查询子栏目
     * @param colId
     * @return
     */
    List<NewsColumnVO> subColList(int colId);
}
