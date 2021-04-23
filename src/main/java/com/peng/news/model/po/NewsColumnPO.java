package com.peng.news.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/9 12:32
 */
@Data
@TableName("news_column")
public class NewsColumnPO {

    @TableId(type = IdType.AUTO)
    Integer id;

    String title;

    String description;

    Integer parentId;

    String externalLink;

    Boolean isHasChildren;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Timestamp createTime;

    Timestamp updateTime;
}
