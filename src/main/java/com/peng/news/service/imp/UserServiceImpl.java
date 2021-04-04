package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.ResourceMapper;
import com.peng.news.mapper.RoleMapper;
import com.peng.news.mapper.UserMapper;
import com.peng.news.mapper.UserRoleMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.RolePO;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.po.UserRolePO;
import com.peng.news.model.vo.ResourceVO;
import com.peng.news.model.vo.UserVO;
import com.peng.news.service.UserService;
import com.peng.news.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 14:11
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ResourceMapper resourceMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO user = userMapper.getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在！");
        }
        return user;
    }

    @Override
    public List<ResourceVO> getMenusOfCurUser(Integer userId) {
        return resourceMapper.getMenusByUserId(getUserId(userId));
    }

    /**
     * 经测试，该方法更快
     * @param userId
     * @return
     */
    @Override
    public List<ResourceVO> getMenusOfCurUser2(Integer userId) {
        List<ResourceVO> menus = resourceMapper.getMenusByUserId2(getUserId(userId));
        //组装父子关系
        ResourceVO root = new ResourceVO();
        createMenuTree(menus, root);
        return root.getChildren();
    }

    @Override
    public UserVO getUserInfoByUserId(Integer userId) {
        return userMapper.getUserByUserId(getUserId(userId));
    }

    @Override
    public boolean updatePersonalInfo(UserPO userPO) {
        /**
         * todo 用户信息完整性验证：phone
         */
        //得到当前登录用户的id，防止修改别人的信息
        Integer userId = getUserId(null);
        assertUserExists(userId);
        userPO.setId(userId);
        //只能修改特定信息，确保信息安全
        userPO = UserPO.forUpdatePersonalInfo(userPO);
        userMapper.updateById(userPO);
        return true;
    }

    @Override
    public boolean updatePersonalPassword(String password) {
        //得到当前登录用户的id，确保修改自己的密码
        Integer userId = getUserId(null);
        assertUserExists(userId);
        if(!StringUtils.isNotEmpty(password)){
            throw new RuntimeException("密码不能为空");
        }
        password = passwordEncoder.encode(password);
        UpdateWrapper<UserPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("passwd", password).eq("id",userId);
        userMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public boolean addUser(UserPO userPO) {
        /**
         * todo 用户信息完整性验证：username、realName、passwd、gender、phone
         */
        assertUserNameNotExists(userPO.getUsername());
        userPO.setId(null);
        userPO.setPasswd(passwordEncoder.encode(userPO.getPasswd()));
        userPO.setLocked(false);
        userPO.setDeleted(false);
        userPO.setCreateTime(null);
        userPO.setUpdateTime(null);
        userMapper.insert(userPO);
        return true;
    }

    @Override
    public boolean delUser(Integer userId) {
        assertUserExists(userId);
        Integer i = userMapper.softDelUser(userId);
        return i == 1;
    }

    @Override
    public boolean setUserLocked(Integer userId, boolean locked) {
        UserPO user = assertUserExists(userId);
        if(user.getLocked() == locked){
            throw new RuntimeException("当前用户已经是" + (locked ? "锁定" : "未锁定") + "状态，操作失败！");
        }
        UpdateWrapper<UserPO> wrapper = new UpdateWrapper<>();
        wrapper.set("locked", locked).eq("id", userId);
        userMapper.update(null, wrapper);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean setRolesForUser(Integer userId, Integer[] roleIds) {
        assertUserExists(userId);
        //确定设置的角色都存在
        QueryWrapper<RolePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", Arrays.asList(roleIds));
        Integer count = roleMapper.selectCount(queryWrapper);
        //去重
        Set<Integer> roleIdSet = Arrays.stream(roleIds).collect(Collectors.toSet());
        if(count != null && count == roleIdSet.size()){
            //先删除user_role表中的记录
            userRoleMapper.delete(new QueryWrapper<UserRolePO>().eq("user_id", userId));
            //再插入
            for (Integer roleId : roleIdSet) {
                userRoleMapper.insert(new UserRolePO(userId, roleId));
            }
            return true;
        }

        throw new RuntimeException("设置了一个不存在的角色，操作失败！");
    }

    @Override
    public CustomizedPage<UserVO> userList(int page, int pageSize) {
        page = page < 0 ? 1 : page;
        return CustomizedPage.fromIPage(userMapper.selectUsersByPage(new Page(page, pageSize)));
    }

    Integer getUserId(Integer userId){
        return userId != null ? userId : ((UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    void createMenuTree(List<ResourceVO> menus, ResourceVO root){
        Integer rootId = root.getId();
        for (ResourceVO menu : menus) {
            Integer parentId = menu.getParentId();
            if(isEqual(rootId, parentId)){
                root.addChild(menu);
                createMenuTree(menus, menu);
            }
        }
    }

    boolean isEqual(Integer a, Integer b){
        if(a == null && b == null){
            return true;
        }

        if(a != null && b != null){
            return b.equals(a);
        }

        return false;
    }

    /**
     * 如果用户不存在，抛出异常。
     * @param userId
     */
    UserPO assertUserExists(Integer userId){
        if (userId == null){
            throw new RuntimeException("当前用户不存在，操作失败！");
        }

        QueryWrapper<UserPO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId).eq("deleted", 0);
        UserPO user = userMapper.selectOne(wrapper);
        if(user == null){
            throw new RuntimeException("当前用户不存在，操作失败！");
        }

        return user;
    }

    /**
     * 如果用户名已存在，抛出异常
     * @param username
     */
    void assertUserNameNotExists(String username){
        QueryWrapper<UserPO> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Integer i = userMapper.selectCount(wrapper);
        if(i != null && i > 0){
            throw new RuntimeException("用户名已存在，操作失败！");
        }
    }
}
