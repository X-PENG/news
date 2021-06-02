package com.peng.news.model.bean;

import lombok.Data;

/**
 * 路由的meta信息
 * @author PENG
 * @version 1.0
 * @date 2021/3/30 16:29
 */
@Data
public class RouteMeta {
    /**
     * 当前路由是否是子菜单
     */
    Boolean subMenu;
}
