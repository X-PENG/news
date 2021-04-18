package com.peng.news.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 新闻状态枚举类
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 20:44
 */

/**
 * 让枚举类对象按照普通类对象的格式进行序列化。
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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
    RE_MODIFICATION(10, "打回修改")
    ;

    /**
     * 属于中转状态的状态列表，包括：
     * 上传成功、审核失败、打回修改
     */
    public static final NewsStatus[] TRANSIT_STATUS_ARRAY = new NewsStatus[] {
            NewsStatus.UPLOAD_SUCCESS,
            NewsStatus.REVIEW_FAIL,
            NewsStatus.RE_MODIFICATION
    };


    /**
     * 属于中转状态的code集合
     */
    public static final Set<Integer> TRANSIT_STATUS_CODE_SET;

    /**
     * 属于 待发布状态 的状态列表，包括：
     * 待发布、撤销发布 都是 待发布状态
     */
    public static final NewsStatus[] TO_BE_RELEASED_STATUS_ARRAY = new NewsStatus[] {
            NewsStatus.TO_BE_RELEASED,
            NewsStatus.REVOKE_PUBLISHED
    };

    /**
     * 属于待发布状态的code集合
     */
    public static final Set<Integer> TO_BE_RELEASED_STATUS_CODE_SET;

    static {
        TRANSIT_STATUS_CODE_SET = Arrays.stream(TRANSIT_STATUS_ARRAY).map(NewsStatus::getCode).collect(Collectors.toSet());
        TO_BE_RELEASED_STATUS_CODE_SET = Arrays.stream(TO_BE_RELEASED_STATUS_ARRAY).map(NewsStatus::getCode).collect(Collectors.toSet());
    }

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
