package com.peng.news.service;

import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;

import java.util.List;

/**
 * 门户网站的首页相关服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 16:33
 */
public interface FrontendIndexService {


    /**
     * 查询所有开启的一级栏目，并且按菜单序号排序
     * @return
     */
    List<NewsColumnVO> allEnabledOneLevelColsOrderByMenuOrder();

    NewsPO getHeadLines();
}
