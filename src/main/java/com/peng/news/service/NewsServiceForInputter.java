package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.paramBean.NewsBeanForInputterSave;
import com.peng.news.model.paramBean.QueryNewsBeanForInputter;
import com.peng.news.model.po.NewsPO;

/**
 * 传稿人的新闻管理服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 21:06
 */
public interface NewsServiceForInputter {
    /**
     * 传稿人创建新闻或保存修改：
     * 1.当id为null就创建新闻，并根据tag设为草稿或上传成功状态；
     * 2.当id不为null就更新新闻，并根据tag设为草稿或上传成功状态。
     *      传稿人想更新新闻，必须满足：
     *      1.首先，新闻必须是当前用户自己创建的；
     *      2.新闻必须是“草稿状态”。
     *
     * 创建或更新新闻时，如果有外链，其他信息都不保存，设为null
     * @param tag 1：草稿状态；2-上传成功状态
     * @param news
     * @return 新闻id
     */
    Integer saveNewsAsDraftOrUpload(int tag, NewsBeanForInputterSave news);

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
