package com.peng.news.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsColumnPO;
import com.peng.news.model.po.UserPO;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 20:41
 */
@Data
public class NewsVO {
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
     * 多对一关系，新闻所属的新闻栏目
     */
    NewsColumnPO column;

    /**
     * 新闻图片来源
     */
    String imgSource;

    /**
     * 文字来源
     */
    String articleSource;

    /**
     * 新闻状态
     * 0-草稿，1-上传成功（备注：属于中转新闻），
     * 2-审核中，3-审核失败（备注：属于中转新闻），
     * 4-审核成功，待发布，5-已发布（普通发布），
     * 6-已发布（并且作为轮播图），7-已发布（并且作为头条），
     * 8-已发布（并且作为轮播图和头条），9-撤销发布，
     * 10-打回修改（备注：属于中转新闻）',
     */
    NewsStatus newsStatus;

    /**
     * 是否是图片新闻
     */
    Boolean isImageNews;

    /**
     * 是否在所在新闻栏目中置顶
     */
    Boolean isTop;

    /**
     * 外网的新闻链接
     */
    String externalUrl;

    /**
     * 多对一关系，录入（创建）新闻的用户
     */
    UserPO inputter;

    /**
     * 新闻的创建时间
     */
    Timestamp createTime;

    /**
     * 新闻完成录入的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Timestamp completeInputTime;

    /**
     * 参与了新闻编辑工作的用户的实名，多个名字用,分隔
     */
    String editors;

    /**
     * 参与了新闻审核工作的用户的实名，逗哥分隔
     */
    String reviewers;

    /**
     * 多对一关系，提交审核的用户
     */
    UserPO submitter;

    /**
     * 送审时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Timestamp submitTime;

    /**
     * 多对一关系，最近一次对新闻进行了编辑的用户
     */
    UserPO latestEditor;

    /**
     * 最近修改日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Timestamp latestEditTime;

    /**
     * 新闻当前所处的审核轮次
     */
    Integer currentReviewEpoch;

    /**
     * 新闻上一轮审核的通过时间。如果新闻处于待发布状态，则表示新闻通过终审的时间
     */
    Timestamp previousEpochReviewPassTime;

    /**
     * 新闻实际的发布时间
     */
    Timestamp realPubTime;

    /**
     * 新闻对外显示的发布时间
     */
    Timestamp showPubTime;

    /**
     * 新闻初始设置阅读量
     */
    Integer initReadingCount;

    /**
     * 新闻实际阅读量
     */
    Integer realReadingCount;

    /**
     * 额外信息，以JSON字符串形式存储
     */
    String extra;
}
