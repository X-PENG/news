package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.RolePO;
import com.peng.news.model.vo.RoleVO;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 19:28
 */
public interface RoleService {
    boolean addRole(RolePO rolePO);

    boolean delRole(Integer roleId);

    List<RoleVO> getAllRoles();

    CustomizedPage<RoleVO> roleList(Integer page, Integer pageSize);

    /**
     * 给角色设置能访问的资源
     * @param roleId
     * @param resourceIds
     * @return
     */
    boolean setResourcesForRole(Integer roleId, Integer[] resourceIds);

    /**
     * 查询一个角色所有不是子菜单的资源的id列表
     * @param roleId
     * @return
     */
    List<Integer> getAllNotSubMenuResourceByRoleId(Integer roleId);
}
