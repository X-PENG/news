package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.dto.ReModificationInfoDTO;
import com.peng.news.model.dto.ReviewFailInfoDTO;
import com.peng.news.model.paramBean.NewsBeanForEditorSave;
import com.peng.news.model.paramBean.QueryNewsBeanForEditor;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsVO;

/**
 * 编辑人员的新闻管理服务。编辑，即具有新闻中转站这个菜单的权限的用户
 * @author PENG
 * @version 1.0
 * @date 2021/4/14 15:17
 */
public interface NewsServiceForEditor {


    /**
     * 分页、条件查询中转新闻列表
     * @param page
     * @param pageSize
     * @param queryBean
     * @return 中转新闻DTO
     */
    CustomizedPage<NewsVO> transitNewsList(Integer page, Integer pageSize, QueryNewsBeanForEditor queryBean);

    /**
     * 查询一个中转状态的新闻
     * @param newsId
     * @return
     */
    NewsPO selectTransitNews(int newsId);

    /**
     * 删除一个中转状态的新闻
     * @param newsId
     * @return
     */
    boolean deleteTransitNews(int newsId);

    /**
     * 提交审核中转状态的新闻
     * @param newsId
     * @return
     */
    boolean submitTransitNewsToReview(int newsId);

    /**
     * 保存修改 或 保存并提交审核 中转新闻
     * @param tag 等于1，保存修改；等于2，保存并提交审核；其他，非法请求
     * @param news
     * @return
     */
    boolean saveOrSaveAndSubmitReview(int tag, NewsBeanForEditorSave news);

    /**
     * 查询审核失败信息
     * @param newsId
     * @return
     */
    ReviewFailInfoDTO queryReviewFailInfo(int newsId);

    /**
     * 查询打回修改信息
     * @param newsId
     * @return
     */
    ReModificationInfoDTO queryReModificationInfo(int newsId);
}
