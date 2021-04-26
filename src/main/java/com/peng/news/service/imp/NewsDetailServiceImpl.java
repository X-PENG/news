package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.AsyncTaskService;
import com.peng.news.service.NewsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/24 0:18
 */
@Service
public class NewsDetailServiceImpl implements NewsDetailService {

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    AsyncTaskService asyncTaskService;

    @Override
    public NewsVO getOneNews(Integer newsId) {
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", newsId).eq("news_status", NewsStatus.PUBLISHED.getCode());

        NewsVO newsVO = newsMapper.selectNewsWithColumn(queryWrapper);

        if(newsVO == null) {
            throw new RuntimeException("新闻不存在！");
        }

        if(newsVO.getColumn() == null) {
            throw new RuntimeException("异常新闻，没有指定新闻栏目或所属栏目不存在或栏目未开启！");
        }

        //增加阅读量
        asyncTaskService.increaseReadingCount(newsId);

        return newsVO;
    }
}
