package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 封装查询已发布新闻的条件参数
 * @author PENG
 * @version 1.0
 * @date 2021/4/18 17:02
 */
@Data
public class QueryPublishedNewsBeanForPublisher {


    /**
     * 标题
     */
    String title;

    /**
     * 外链
     */
    String externalUrl;

    /**
     * 新闻栏目id
     */
    Integer columnId;

    /**
     * 是否置顶
     */
    Boolean isTop;

    /**
     * 是否轮播
     */
    Boolean isCarousel;

    /**
     * 是否是头条
     */
    Boolean isHeadlines;

    /**
     * 发布新闻的用户的id
     */
    Integer publisherId;

    /**
     * 是否按实际发布时间排序。true-升序；false-降序；null-用默认排序
     */
    Boolean orderByRealPubTime;

    /**
     * 是否按 设置轮播时机 排序
     */
    Boolean orderBySetCarouselTime;

    /**
     * 是否按 设置头条时机 排序
     */
    Boolean orderBySetHeadlinesTime;

    /**
     * 是否按 设置置顶时机 排序
     */
    Boolean orderBySetTopTime;

    /**
     * 是否按 实际阅读量 排序
     */
    Boolean orderByRealReadingCount;


    public void trimOrSetNull() {
        if(title == null || "".equals(title = title.trim())) {
            title = null;
        }

        if(externalUrl == null || "".equals(externalUrl = externalUrl.trim())) {
            externalUrl = null;
        }
    }

    public SingleColumnOrderRule getOrderRule() {

        if(orderByRealPubTime != null) {
            return new SingleColumnOrderRule("real_pub_time", orderByRealPubTime);
        }

        if(orderBySetCarouselTime != null) {
            return new SingleColumnOrderRule("set_carousel_time", orderBySetCarouselTime);
        }

        if(orderBySetHeadlinesTime != null) {
            return new SingleColumnOrderRule("set_headlines_time", orderBySetHeadlinesTime);
        }

        if(orderBySetTopTime != null) {
            return new SingleColumnOrderRule("set_top_time", orderBySetTopTime);
        }

        if(orderByRealReadingCount != null) {
            return new SingleColumnOrderRule("real_reading_count", orderByRealReadingCount);
        }

        //默认，按照实际时间降序
        return new SingleColumnOrderRule("real_pub_time", false);
    }
}
