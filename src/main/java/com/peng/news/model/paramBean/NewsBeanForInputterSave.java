package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 为传稿人保存新闻准备的bean
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 21:15
 */
@Data
public class NewsBeanForInputterSave {


    /**
     * 新闻id
     */
    Integer id;

    /**
     * 标题
     */
    String title;

    /**
     * 正文
     */
    String content;

    /**
     * 新闻图片来源
     */
    String imgSource;

    /**
     * 文字来源
     */
    String articleSource;

    /**
     * 新闻外链
     */
    String externalUrl;

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
