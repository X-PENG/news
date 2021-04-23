package com.peng.news.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.peng.news.model.po.NewsColSettingsPO;
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

    /**
     * 对应的设置信息。一对一关系
     */
    NewsColSettingsPO settings;
}
