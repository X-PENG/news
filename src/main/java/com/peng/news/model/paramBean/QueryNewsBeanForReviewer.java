package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 封装审核人员查询新闻的条件参数
 * @author PENG
 * @version 1.0
 * @date 2021/4/15 20:42
 */
@Data
public class QueryNewsBeanForReviewer {
    /**
     * 标题
     */
    String title;

    /**
     * 外链
     */
    String externalUrl;


    /**
     * 送审人id
     */
    Integer submitterId;


    /**
     * 是否按最近修改时间排序；true-升序，false-降序；null-不按最近修改时间排序，使用默认排序
     */
    Boolean orderByLatestEditTime;

    /**
     * 是否按上一轮审核通过的时间排序；true-升序，false-降序；null-使用默认排序
     */
    Boolean orderByPreviousEpochReviewPassTime;

    /**
     * 是否按送审时间排序；true-升序，false-降序；null-使用默认排序
     */
    Boolean orderBySubmitTime;


    public void trimOrSetNull(){
        if(title == null || "".equals((title = title.trim()))){
            title = null;
        }

        if(externalUrl == null || "".equals((externalUrl = externalUrl.trim()))){
            externalUrl = null;
        }
    }

    /**
     * 返回新闻列表的排序规则
     * @param epoch 新闻审核轮次
     * @return
     */
    public SingleColumnOrderRule getOrderRule(int epoch) {

        if(orderByLatestEditTime != null) {
            return new SingleColumnOrderRule("latest_edit_time", orderByLatestEditTime);
        }

        if(orderBySubmitTime != null) {
            return new SingleColumnOrderRule("submit_time", orderBySubmitTime);
        }

        //一审新闻列表不能按上轮审核时间排序
        if(epoch != 1 && orderByPreviousEpochReviewPassTime != null) {
            return new SingleColumnOrderRule("previous_epoch_review_pass_time", orderByPreviousEpochReviewPassTime);
        }


        //按默认规则排序
        if(epoch == 1) {
            //一审新闻列表 默认按 送审时间 降序排序
            return new SingleColumnOrderRule("submit_time", false);
        }else {
            //其他审核轮次的新闻列表 默认按 上一轮审核时间 降序排序
            return new SingleColumnOrderRule("previous_epoch_review_pass_time", false);
        }
    }


    /**
     * 封装单字段排序规则
     */
    public static class SingleColumnOrderRule {

        /**
         * 排序的字段名
         */
        String column;

        /**
         * 是否升序；true，升序；false，则降序
         */
        boolean isAsc;

        public SingleColumnOrderRule() {
        }

        public SingleColumnOrderRule(String column, boolean isAsc) {
            this.column = column;
            this.isAsc = isAsc;
        }

        public String getColumn() {
            return column;
        }

        public boolean isAsc() {
            return isAsc;
        }
    }
}
