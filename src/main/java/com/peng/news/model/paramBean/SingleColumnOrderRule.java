package com.peng.news.model.paramBean;

/**
 * 封装单字段排序规则
 * @author PENG
 * @version 1.0
 * @date 2021/4/18 21:33
 */
public class SingleColumnOrderRule {

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
