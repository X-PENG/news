package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.paramBean.NewsBeanForPublisherPub;
import com.peng.news.model.paramBean.QueryUnpublishedNewsBeanForPublisher;
import com.peng.news.model.vo.NewsVO;

/**
 * 发布员新闻管理服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/17 0:05
 */
public interface NewsServiceForPublisher {
    /**
     * 分页、条件查询 待发布状态的 新闻列表
     * @param page
     * @param pageSize
     * @param queryBean
     * @return
     */
    CustomizedPage<NewsVO> unpublishedNewsList(Integer page, Integer pageSize, QueryUnpublishedNewsBeanForPublisher queryBean);

    /**
     * 将 待发布 的新闻 打回修改
     * 待发布状态有：待发布 、 撤销发布
     * @param newsId
     * @param suggestion 打回意见
     * @return
     */
    boolean remodification(int newsId, String suggestion);

    /**
     * 发布一个新闻
     * @param newsId
     * @param pubInfo  发布设置相关信息
     * @return
     */
    boolean publishOneNews(Integer newsId, NewsBeanForPublisherPub pubInfo);
}
