package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.NewsColumnMapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.FrontendIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 16:33
 */
@Service
public class FrontendIndexServiceImpl implements FrontendIndexService {

    @Autowired
    NewsColumnMapper newsColumnMapper;

    @Autowired
    NewsMapper newsMapper;

    @Override
    public List<NewsColumnVO> allEnabledOneLevelColsOrderByMenuOrder() {
        return newsColumnMapper.getEnabledChildrenNewsColumnListByParentId(null);
    }

    @Override
    public NewsPO getHeadLines() {
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("news_status", NewsStatus.PUBLISHED.getCode());
        queryWrapper.eq("is_headlines", true);
        queryWrapper.orderByDesc("set_headlines_time");

        IPage<NewsPO> pageObj = new Page<>(1, 1);
        IPage<NewsPO> selectPage = newsMapper.selectPage(pageObj, queryWrapper);
        return selectPage.getRecords().get(0);
    }
}
