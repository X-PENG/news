package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.cache.constant.CacheConstants;
import com.peng.news.mapper.NewsColumnMapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.FrontendIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 16:33
 */
@Service
@CacheConfig(cacheNames = CacheConstants.CACHE_NAME_FRONTEND_INDEX_PAGE)
public class FrontendIndexServiceImpl implements FrontendIndexService {

    @Autowired
    NewsColumnMapper newsColumnMapper;

    @Autowired
    NewsMapper newsMapper;

    @Cacheable(key = "'" + CacheConstants.CACHE_KEY_FRONTEND_NAVIGATION + "'")
    @Override
    public List<NewsColumnVO> allEnabledOneLevelColsOrderByMenuOrder() {
        return newsColumnMapper.getEnabledChildrenNewsColumnListByParentId(null);
    }

    @Cacheable(key = "'" + CacheConstants.CACHE_KEY_FRONTEND_HEADLINES + "'")
    @Override
    public NewsPO getHeadLines() {
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        wrapNecessaryConditionForQueryNews(queryWrapper);
        queryWrapper.eq("is_headlines", true);
        queryWrapper.orderByDesc("set_headlines_time");

        queryWrapper.select("id", "title");

        IPage<NewsPO> pageObj = new Page<>(1, 1);
        IPage<NewsPO> selectPage = newsMapper.selectPage(pageObj, queryWrapper);
        return selectPage.getRecords().get(0);
    }

    @Cacheable(key = "'" + CacheConstants.CACHE_KEY_FRONTEND_CAROUSEL + "'")
    @Override
    public List<NewsPO> carouselNewsList(Integer amount) {
        //默认5个
        amount = amount == null ? 5 : amount;
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        wrapNecessaryConditionForQueryNews(queryWrapper);
        queryWrapper.eq("is_carousel", true);
        queryWrapper.orderByDesc("set_carousel_time");

        queryWrapper.select("id", "title", "article_fragment_for_show", "show_pub_time", "extra");

        IPage<NewsPO> page = new Page<>(1, amount);
        IPage<NewsPO> selectPage = newsMapper.selectPage(page, queryWrapper);

        return selectPage.getRecords();
    }


    /**
     * 给查询新闻封装必要条件
     * @param queryWrapper
     */
    private void wrapNecessaryConditionForQueryNews(QueryWrapper<NewsPO> queryWrapper) {
        //新闻必须是已发布状态
        queryWrapper.eq("news_status", NewsStatus.PUBLISHED.getCode());
    }
}
