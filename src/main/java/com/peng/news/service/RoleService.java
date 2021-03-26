package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.RolePO;
import com.peng.news.model.vo.RoleVO;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 19:28
 */
public interface RoleService {
    boolean addRole(RolePO rolePO);

    boolean delRole(Integer roleId);

    CustomizedPage<RoleVO> roleList(int page, int pageSize);

    /**
     * 给角色设置能访问的资源
     * @param roleId
     * @param resourceIds
     * @return
     */
    boolean setResourcesForRole(Integer roleId, Integer[] resourceIds);
}
