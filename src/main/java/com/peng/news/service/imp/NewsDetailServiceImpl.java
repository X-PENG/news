package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.NewsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
        increaseReadingCount(newsId);

        return newsVO;
    }

    /**
     * 增加阅读量
     * @param newsId
     */
    @Async
    public synchronized void increaseReadingCount(int newsId) {

        //先查询旧值
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<NewsPO>().select("real_reading_count").eq("id", newsId);
        int oldValue = newsMapper.selectOne(queryWrapper).getRealReadingCount();

        //设置新值
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<NewsPO>().set("real_reading_count", oldValue + 1).eq("id", newsId);
        newsMapper.update(null, updateWrapper);
    }
}
