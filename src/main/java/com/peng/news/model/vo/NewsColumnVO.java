package com.peng.news.model.vo;

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

    Integer menuOrder;

    Integer moduleOrder;

    Integer parentId;

    Boolean enabled;

    String externalLink;

    Boolean isHasChildren;

    Timestamp createTime;

    Timestamp updateTime;

    /**
     * 子栏目列表
     */
    List<NewsColumnVO> children;
}
