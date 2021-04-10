package com.peng.news.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 21:01
 */
@TableName("resource")
@Data
public class ResourcePO {

    @TableId(type = IdType.AUTO)
    Integer id;

    /**
     * 进行权限管理的url资源（url pattern）
     */
    String url;

    /**
     * 绑定的菜单名
     */
    String name;

    /**
     * 绑定的菜单的路由路径
     */
    String path;

    /**
     * 绑定的菜单对应的组件
     */
    String component;

    /**
     * 菜单的图标
     */
    String iconCls;

    /**
     * 父菜单id
     */
    Integer parentId;

    /**
     * 是否启用菜单
     */
    Boolean enabled;

    /**
     * 资源描述
     */
    String description;

    /**
     * 是否是子菜单
     */
    Boolean subMenu;

    /**
     * 是否隐藏，不作为侧边栏菜单
     */
    Boolean hidden;
}
