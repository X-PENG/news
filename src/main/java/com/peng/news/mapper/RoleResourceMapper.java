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

    List<Integer> getAllResourceIdByRoleId(Integer roleId);
}
