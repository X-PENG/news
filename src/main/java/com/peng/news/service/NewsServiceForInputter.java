package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.paramBean.NewsBeanForInputterSave;
import com.peng.news.model.paramBean.QueryNewsBeanForInputter;
import com.peng.news.model.po.NewsPO;

/**
 * 传稿人的新闻管理服务。传稿人，即具有 撰写新闻 菜单权限的用户
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 21:06
 */
public interface NewsServiceForInputter {
    /**
     * 创建新闻，并根据tag设为草稿或上传成功状态；
     * @param tag 1：草稿状态；2-上传成功状态
     * @param news
     * @return
     */
    Integer createNewsAsDraftOrUpload(int tag, NewsBeanForInputterSave news);

    /**
     * 更新新闻，并根据tag设为草稿或上传成功状态。
     * @param tag 1：草稿状态；2-上传成功状态
     * @param news
     * @return
     */
    Integer updateNewsAsDraftOrUpload(int tag, NewsBeanForInputterSave news);

    CustomizedPage<NewsPO> draftList(Integer page, Integer pageSize, QueryNewsBeanForInputter queryBean);


    /**
     * 查询当前请求用户的草稿
     * @param newsId
     * @return
     */
    NewsPO selectCurUserDraft(int newsId);


    /**
     * 删除当前请求用户的草稿
     * @param newsId
     * @return
     */
    boolean deleteCurUserDraft(Integer newsId);

    /**
     * 上传当前请求用户的草稿
     * @param newsId
     * @return
     */
    boolean uploadCurUserDraft(Integer newsId);
}
