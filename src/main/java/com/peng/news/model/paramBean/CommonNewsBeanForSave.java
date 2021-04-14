package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 为传稿人、编辑和审核人员保存新闻（创建或更新）准备的公共bean
 * @author PENG
 * @version 1.0
 * @date 2021/4/14 14:45
 */
@Data
public abstract class CommonNewsBeanForSave {
    /**
     * 新闻id
     */
    protected Integer id;

    /**
     * 标题
     */
    protected String title;

    /**
     * 正文
     */
    protected String content;

    /**
     * 新闻图片来源
     */
    protected String imgSource;

    /**
     * 文字来源
     */
    protected String articleSource;

    /**
     * 新闻外链
     */
    protected String externalUrl;

    /**
     * 对字符串两边空格进行修剪，如果是空字符串，就设为null
     * @return
     */
    public void trimOrSetNull(){
        if(title == null || "".equals(title = title.trim())) {
            title = null;
        }

        if(imgSource == null || "".equals(imgSource = imgSource.trim())) {
            imgSource = null;
        }

        if(articleSource == null || "".equals(articleSource = articleSource.trim())) {
            articleSource = null;
        }

        if(externalUrl == null || "".equals(externalUrl = externalUrl.trim())) {
            externalUrl = null;
        }
    }
}
