package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 封装传稿人查询草稿的条件参数的bean
 * @author PENG
 * @version 1.0
 * @date 2021/4/13 9:23
 */
@Data
public class QueryNewsBeanForInputter {

    /**
     * 标题
     */
    String title;

    /**
     * 图片来源
     */
    String imgSource;

    /**
     * 文字来源
     */
    String articleSource;

    /**
     * 外链
     */
    String externalUrl;

    /**
     * 是否按创建时间排序；true-升序，false-降序；null-不按创建时间排序，使用默认排序
     */
    Boolean orderByCreateTime;

    /**
     * 修剪字符串两边空格，如果是空字符串，格式化为null
     * @return
     */
    public QueryNewsBeanForInputter trimAndFormat(){
        if(title == null || "".equals((title = title.trim()))) {
            title = null;
        }
        if(imgSource == null || "".equals((imgSource = imgSource.trim()))){
            imgSource = null;
        }

        if(articleSource == null || "".equals((articleSource = articleSource.trim()))){
            articleSource = null;
        }

        if(externalUrl == null || "".equals((externalUrl = externalUrl.trim()))){
            externalUrl = null;
        }

        return this;
    }
}
