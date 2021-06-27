package com.peng.news.service.imp;

import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.NewsDetailService;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/6/26 11:33
 */
public abstract class AbstractNewsDetailService implements NewsDetailService {

    @Override
    public NewsVO viewNews(Integer newsId) {
        if(newsId == null) {
            return null;
        }

        NewsVO newsVO = queryNews(newsId);
        increaseNewsReadingCount(newsId);
        return newsVO;
    }

    /**
     * 查询新闻
     * @param newsId
     * @return
     */
    protected abstract NewsVO queryNews(int newsId);

    /**
     * 累加新闻的阅读量
     * @param newsId
     */
    protected abstract void increaseNewsReadingCount(int newsId);
}
