package com.peng.news.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 新闻栏目设置表对应的实体类
 * 为了解耦，将栏目设置信息和栏目基本信息解耦开
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 9:27
 */
@Data
@TableName("news_column_settings")
public class NewsColSettingsPO {

    @TableId(type = IdType.AUTO)
    Integer id;

    /**
     * 对应的新闻栏目id
     */
    Integer colId;

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
