package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.paramBean.NewsBeanForReviewerSave;
import com.peng.news.model.paramBean.QueryNewsBeanForReviewer;
import com.peng.news.model.paramBean.ReviewResultParamBean;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsVO;

/**
 * 审核人员新闻管理服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/15 20:49
 */
public interface NewsServiceForReviewer {

    /**
     * 查询审核中的新闻列表
     * @param epoch 查询的审核轮次
     * @param page
     * @param pageSize
     * @param queryBean
     * @return
     */
    CustomizedPage<NewsVO> underReviewNewsList(int epoch, Integer page, Integer pageSize, QueryNewsBeanForReviewer queryBean);

    /**
     * 查询一个审核中的新闻
     * @param epoch 新闻所处的审核轮次
     * @param newsId
     * @return
     */
    NewsPO selectUnderReviewNews(int epoch, int newsId);

    /**
     * 保存修改 或 保存并审核通过 审核中的新闻，都是更新新闻；
     * 要确保新闻处于 审核中状态，且审核轮次为epoch
     * @param epoch 审核轮次
     * @param tag 1-保存修改；2-保存修改并通过审核；其他-报错
     * @param news
     * @return
     */
    boolean saveOrSaveAndReviewSuccess(int epoch, int tag, NewsBeanForReviewerSave news);

    /**
     * 审核一个新闻
     * @param epoch
     * @param newsId
     * @param paramBean 审核结果
     * @return
     */
    boolean reviewOneNews(int epoch, int newsId, ReviewResultParamBean paramBean);
}
