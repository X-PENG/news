package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsPO;
import com.peng.news.service.NewsPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/17 10:29
 */
@Service
public class NewsPublicServiceImpl implements NewsPublicService {


    @Autowired
    NewsMapper newsMapper;

    @Override
    public NewsPO selectOneNotDraftsNews(Integer newsId) {
        assertNewsExistsAndNotDraft(newsId);
        return newsMapper.selectById(newsId);
    }

    /**
     * 确保新闻存在，且不是草稿；查不到，就报错
     * @param newsId
     */
    private void assertNewsExistsAndNotDraft(Integer newsId) {
        if(newsId == null) {
            throw new RuntimeException("新闻不存在，加载失败！");
        }

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", newsId).ne("news_status", NewsStatus.DRAFT.getCode());

        if(newsMapper.selectCount(queryWrapper) == 0) {
            throw new RuntimeException("新闻不存在，加载失败！");
        }
    }
}
