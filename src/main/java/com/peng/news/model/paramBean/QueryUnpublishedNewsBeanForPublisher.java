package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 封装查询未发布的新闻条件参数
 * @author PENG
 * @version 1.0
 * @date 2021/4/17 0:00
 */
@Data
public class QueryUnpublishedNewsBeanForPublisher {

    /**
     * 标题
     */
    String title;

    /**
     * 状态
     */
    Integer newsStatus;

    /**
     * 是否按上一轮审核通过的时间排序；true-升序，false-降序；null-使用默认排序
     */
    Boolean orderByPreviousEpochReviewPassTime;

    public void trimOrSetNull(){
        if(title == null || "".equals((title = title.trim()))) {
            title = null;
        }
    }
}
