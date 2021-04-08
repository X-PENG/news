package com.peng.news.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 14:16
 */
@TableName("role")
@Data
public class RolePO {

    public static final String ROLE_PREFIX = "ROLE_";

    @TableId(type = IdType.AUTO)
    Integer id;

    /**
     * 角色英文名
     */
    String nameEn;

    /**
     * 角色中文名
     */
    String nameZh;

    /**
     * 角色描述
     */
    String description;

    Boolean isSystemAdmin;

    Timestamp createTime;

    Timestamp updateTime;
}
