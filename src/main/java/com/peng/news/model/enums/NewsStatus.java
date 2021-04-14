package com.peng.news.model.enums;

/**
 * 新闻状态枚举类
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 20:44
 */
public enum NewsStatus {
    /**
     * 草稿
     */
    DRAFT(0, "草稿"),
    /**
     * 传稿人上传成功
     */
    UPLOAD_SUCCESS(1, "上传成功"),
    UNDER_REVIEW(2, "审核中"),
    REVIEW_FAIL(3, "审核失败"),
    TO_BE_RELEASED(4, "待发布"),
    /**
     * 普通发布
     */
    PUBLISHED_NORMAL(5, "已发布"),
    /**
     * 发布，且放到轮播图上
     */
    PUBLISHED_AS_CAROUSEL(6, "已发布+轮播图"),
    /**
     * 发布，且显示在头条上
     */
    PUBLISHED_AS_HEADLINES(7, "已发布+头条新闻"),
    PUBLISHED_AS_CAROUSEL_AND_HEADLINES(8, "已发布+轮播+头条"),
    REVOKE_PUBLISHED(9, "撤销发布"),
    /**
     * 打回，重新修改
     */
    REMODIFICATION(10, "打回修改")
    ;

    NewsStatus() {
    }

    NewsStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 状态编码
     */
    int code;

    /**
     * 状态名
     */
    String name;

    public static NewsStatus fromCode(int code){
        for (NewsStatus newsStatus : values()) {
            if(newsStatus.getCode() == code){
                return newsStatus;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
