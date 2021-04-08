package com.peng.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.model.po.RolePO;
import com.peng.news.model.vo.RoleVO;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 14:37
 */
public interface RoleMapper extends BaseMapper<RolePO> {
    List<RoleVO> getRolesByUserId(Integer userId);
    List<RoleVO> getRolesByResourceId(Integer resourceId);

    IPage<RoleVO> selectRolesByPage(Page page);

    List<RoleVO> getAllRoles();
}
