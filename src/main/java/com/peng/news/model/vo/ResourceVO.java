package com.peng.news.model.vo;

import com.peng.news.model.bean.RouteMeta;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 21:01
 */
@Data
public class ResourceVO {

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
     * 配置资源和角色的一对多关系
     */
    List<RoleVO> roles;

    /**
     * 路由的meta信息
     */
    RouteMeta meta;

    /**
     * 是否隐藏，不作为侧边栏菜单
     */
    Boolean hidden;

    /**
     * 子菜单
     */
    List<ResourceVO> children;

    public void addChild(ResourceVO child){
        if(children == null){
            children = new ArrayList<>();
        }
        children.add(child);
    }
}
