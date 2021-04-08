package com.peng.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peng.news.model.po.RoleResourcePO;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 20:17
 */
public interface RoleResourceMapper extends BaseMapper<RoleResourcePO> {

    /**
     * 查询一个角色所有不是子菜单的资源的id列表
     * @param roleId
     * @return
     */
    List<Integer> getAllNotSubMenuResourceByRoleId(Integer roleId);
}
