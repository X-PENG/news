package com.peng.news.model.vo;

import com.peng.news.model.po.NewsColumnPO;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 9:32
 */
@Data
public class NewsColSettingsVO {

    Integer id;

    /**
     * 对应的新闻栏目信息。一对一关系
     */
    NewsColumnPO newsCol;

    /**
     * 是否开启
     */
    Boolean enabled;

    /**
     * 菜单序号
     */
    Integer menuOrder;

    /**
     * 模块序号
     */
    Integer moduleOrder;

    /**
     * 是否在右边显示图片
     */
    Boolean showImgOnTheRight;

    Timestamp createTime;

    Timestamp updateTime;

}
