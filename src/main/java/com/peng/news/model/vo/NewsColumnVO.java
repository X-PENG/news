package com.peng.news.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/9 12:36
 */
@Data
public class NewsColumnVO {
    Integer id;

    String title;

    String description;

    Integer parentId;

    String externalLink;

    Boolean isHasChildren;

    Boolean enabled;

    Integer menuOrder;

    Integer moduleOrder;

    Boolean showImgOnTheRight;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Timestamp createTime;

    Timestamp updateTime;

    /**
     * 子栏目列表
     */
    List<NewsColumnVO> children;

    /**
     * 父栏目
     */
    NewsColumnVO parent;
}
