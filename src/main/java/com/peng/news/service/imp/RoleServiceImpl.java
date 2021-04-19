package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.ResourceMapper;
import com.peng.news.mapper.RoleMapper;
import com.peng.news.mapper.RoleResourceMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.ResourcePO;
import com.peng.news.model.po.RolePO;
import com.peng.news.model.po.RoleResourcePO;
import com.peng.news.model.vo.RoleVO;
import com.peng.news.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 19:28
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper roleMapper;

    @Autowired
    ResourceMapper resourceMapper;

    @Autowired
    RoleResourceMapper roleResourceMapper;

    @Override
    public boolean addRole(RolePO rolePO) {
        //格式化和校验信息
        formatAndValidateForAddRole(rolePO);

        rolePO.setNameEn(rolePO.getNameEn().trim());
        rolePO.setNameZh(rolePO.getNameZh().trim());
        if(!rolePO.getNameEn().startsWith(RolePO.ROLE_PREFIX)){
            rolePO.setNameEn(RolePO.ROLE_PREFIX + rolePO.getNameEn());
        }
        assertNameNotExists(rolePO.getNameEn(), rolePO.getNameZh());
        rolePO.setId(null);
        rolePO.setCreateTime(null);
        rolePO.setUpdateTime(null);
        roleMapper.insert(rolePO);
        return true;
    }

    /**
     * 为添加角色进行格式化和校验信息。
     * 格式化，是指将字符串两边空格进行修剪。
     * 校验，是完整性校验
     * @param rolePO
     */
    private void formatAndValidateForAddRole(RolePO rolePO) {
        String nameEn = rolePO.getNameEn();
        String nameZh = rolePO.getNameZh();

        if(nameEn == null || "".equals(nameEn = nameEn.trim())) {
            throw new RuntimeException("角色英文名不能为空！");
        }

        if(nameZh == null || "".equals(nameZh = nameZh.trim())) {
            throw new RuntimeException("角色中文名不能为空！");
        }

        //赋值格式化后的值
        rolePO.setNameEn(nameEn);
        rolePO.setNameZh(nameZh);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delRole(Integer roleId) {
        RolePO rolePO = assertRoleExists(roleId);
        if(rolePO.getIsSystemAdmin()){
            throw new RuntimeException("不允许删除系统管理员角色");
        }
        roleMapper.deleteById(roleId);
        //删除该角色分配的所有资源
        roleResourceMapper.delete(new QueryWrapper<RoleResourcePO>().eq("role_id", roleId));
        return true;
    }

    @Override
    public List<RoleVO> getAllRoles() {
        return roleMapper.getAllRoles();
    }

    @Override
    public CustomizedPage<RoleVO> roleList(Integer page, Integer pageSize) {
        page = page == null ? 1 : page < 0 ? 1 : page;
        pageSize = pageSize ==null ? Integer.MAX_VALUE : pageSize < 0 ? 0 : pageSize;
        return CustomizedPage.fromIPage(roleMapper.selectRolesByPage(new Page(page, pageSize)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean setResourcesForRole(Integer roleId, Integer[] resourceIds) {
        RolePO rolePO = assertRoleExists(roleId);
        if(rolePO.getIsSystemAdmin()){
            throw new RuntimeException("不允许修改系统管理员角色的资源");
        }
        if(resourceIds.length != 0){
            //确定设置的资源都存在且都开启了
            QueryWrapper<ResourcePO> wrapper = new QueryWrapper<>();
            wrapper.in("id", Arrays.asList(resourceIds)).eq("enabled", true);
            Integer count = resourceMapper.selectCount(wrapper);
            //去重
            Set<Integer> resourceIdSet = Arrays.stream(resourceIds).collect(Collectors.toSet());
            if(count != null && count == resourceIdSet.size()){
                //先删除role_resource表中的记录
                roleResourceMapper.delete(new QueryWrapper<RoleResourcePO>().eq("role_id", roleId));
                //再插入
                for (Integer resourceId : resourceIdSet) {
                    roleResourceMapper.insert(new RoleResourcePO(roleId, resourceId));
                }
                return true;
            }
        }else {
            //删除该角色分配的所有资源
            roleResourceMapper.delete(new QueryWrapper<RoleResourcePO>().eq("role_id", roleId));
            return true;
        }

        throw new RuntimeException("设置了一个不存在的资源，操作失败！");
    }

    @Override
    public List<Integer> getAllNotSubMenuResourceByRoleId(Integer roleId) {
        return roleResourceMapper.getAllNotSubMenuResourceByRoleId(roleId);
    }

    /**
     * 如果角色不存在，抛出异常
     * @param roleId
     * @return
     */
    RolePO assertRoleExists(Integer roleId){
        if(roleId == null){
            throw new RuntimeException("当前角色不存在，操作失败！");
        }

        RolePO role = roleMapper.selectOne(new QueryWrapper<RolePO>().eq("id", roleId));
        if(role == null){
            throw new RuntimeException("当前角色不存在，操作失败！");
        }

        return role;
    }

    /**
     * 如果角色中/英文名存在，抛出异常
     * @param nameEn
     * @param nameZh
     */
    void assertNameNotExists(String nameEn, String nameZh){
        QueryWrapper<RolePO> wrapper = new QueryWrapper<>();
        wrapper.eq("name_en", nameEn).or().eq("name_zh", nameZh);
        //因为name_en和name_zh是unique的，所以最多查询出两个实体
        List<RolePO> roles = roleMapper.selectList(wrapper);
        if(roles.size() == 2){
            throw new RuntimeException("当前角色的英文名和中文名均存在，操作失败！");
        }
        if(roles.size() == 1){
            RolePO role = roles.get(0);
            if (role.getNameEn().equals(nameEn) && role.getNameZh().equals(nameZh)) {
                throw new RuntimeException("当前角色的英文名和中文名都已存在，操作失败！");
            }else if(role.getNameEn().equals(nameEn)){
                throw new RuntimeException("当前角色的英文名已存在，操作失败！");
            }else{
                throw new RuntimeException("当前角色的中文名已存在，操作失败！");
            }
        }
    }
}
