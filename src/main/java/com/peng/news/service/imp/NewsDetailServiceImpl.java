package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/24 0:18
 */
@Service
public class NewsDetailServiceImpl extends AbstractNewsDetailService {

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    AsyncTaskService asyncTaskService;

    /**
     * 直接从数据库中查询
     * @param newsId
     * @return
     */
    @Override
    protected NewsVO queryNews(int newsId) {
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", newsId).eq("news_status", NewsStatus.PUBLISHED.getCode());

        NewsVO newsVO = newsMapper.selectNewsWithColumn(queryWrapper);

        if(newsVO == null) {
            throw new RuntimeException("新闻不存在！");
        }

        if(newsVO.getColumn() == null) {
            throw new RuntimeException("异常新闻，没有指定新闻栏目或所属栏目不存在或栏目未开启！");
        }

        return newsVO;
    }

    /**
     * 直接更新数据库
     * @param newsId
     */
    @Override
    protected void increaseNewsReadingCount(int newsId) {
        asyncTaskService.increaseReadingCount(newsId);
    }
}
