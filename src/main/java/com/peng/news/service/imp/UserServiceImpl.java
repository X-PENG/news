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
import com.peng.news.model.paramBean.QueryUserBean;
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
        //格式化并且校验信息
        formatAndValidateForAddUser(userPO);

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

    /**
     * 为添加用户进行格式化和校验信息。
     * 格式化，就是修剪字符串两边空格。
     * 校验，就是对信息进行完整性校验。
     * @param userPO
     */
    private void formatAndValidateForAddUser(UserPO userPO) {
        String username = userPO.getUsername();
        String realName = userPO.getRealName();
        Boolean gender = userPO.getGender();
        String passwd = userPO.getPasswd();
        String phone = userPO.getPhone();

        //校验和格式化
        if(gender == null) {
            throw new RuntimeException("请选择性别！");
        }
        if (username == null || "".equals(username = username.trim())) {
            throw new RuntimeException("用户名不能为空！");
        }

        if (realName == null || "".equals(realName = realName.trim())) {
            throw new RuntimeException("用户实名不能为空！");
        }

        if (passwd == null || "".equals(passwd = passwd.trim())) {
            throw new RuntimeException("密码不能为空！");
        }

        if (phone == null || "".equals(phone = phone.trim())) {
            throw new RuntimeException("用户电话不能为空！");
        }

        //赋值格式化后的值
        userPO.setUsername(username);
        userPO.setRealName(realName);
        userPO.setPasswd(passwd);
        userPO.setPhone(phone);
    }

    @Override
    public boolean delUser(Integer userId) {
        assertUserExists(userId);
        if (isAdmin(userId)) {
            throw new RuntimeException("不允许删除系统管理员用户");
        }
        Integer i = userMapper.softDelUser(userId);
        return i == 1;
    }

    @Override
    public boolean setUserLocked(Integer userId, boolean locked) {
        UserPO user = assertUserExists(userId);
        if(user.getLocked() == locked){
            throw new RuntimeException("当前用户已经是" + (locked ? "锁定" : "未锁定") + "状态，操作失败！");
        }
        if(isAdmin(userId)){
            throw new RuntimeException("不允许操作系统管理员");
        }
        UpdateWrapper<UserPO> wrapper = new UpdateWrapper<>();
        wrapper.set("locked", locked).eq("id", userId);
        userMapper.update(null, wrapper);
        return true;
    }

    @Override
    public List<Integer> getRolesOfUser(Integer userId) {
        assertUserExists(userId);
        QueryWrapper<UserRolePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("role_id").eq("user_id", userId);
        return userRoleMapper.selectList(queryWrapper).stream().map(UserRolePO::getRoleId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean setRolesForUser(Integer userId, Integer[] roleIds) {
        assertUserExists(userId);
        //去重
        if(isAdmin(userId) || ( roleIds.length > 0 && roleMapper.selectCount(new QueryWrapper<RolePO>().in("id", roleIds).eq("is_system_admin", true)) > 0 )){
            throw new RuntimeException("不允许修改系统管理员的配置或设置为系统管理员！");
        }

        if(roleIds.length > 0){
            Set<Integer> roleIdSet = Arrays.stream(roleIds).collect(Collectors.toSet());
            QueryWrapper<RolePO> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", roleIdSet);
            Integer count = roleMapper.selectCount(queryWrapper);
            //确定设置的角色都存在
            if(count == roleIdSet.size()){
                //先删除user_role表中的记录
                userRoleMapper.delete(new QueryWrapper<UserRolePO>().eq("user_id", userId));
                //再插入
                for (Integer roleId : roleIdSet) {
                    userRoleMapper.insert(new UserRolePO(userId, roleId));
                }
                return true;
            }
        }else {
            //删除user_role表中的记录
            userRoleMapper.delete(new QueryWrapper<UserRolePO>().eq("user_id", userId));
            return true;
        }

        throw new RuntimeException("待分配的角色中包含不存在的角色，操作失败！");
    }

    @Override
    public CustomizedPage<UserVO> userList(Integer page, Integer pageSize, QueryUserBean queryUserBean) {
        page = page == null || page <= 0 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;
        List<Object> userIdList = null;
        if(queryUserBean.getRoleId() != null){
            userIdList = userRoleMapper.selectObjs(new QueryWrapper<UserRolePO>().select("user_id").eq("role_id", queryUserBean.getRoleId()));
            if(userIdList == null || userIdList.size() == 0){
                //没有符合条件的用户
                CustomizedPage<UserVO> emptyPage = new CustomizedPage<>();
                emptyPage.setCurrent(page);
                emptyPage.setSize(pageSize);
                return emptyPage;
            }
        }
        return CustomizedPage.fromIPage(userMapper.selectUsersByPage(new Page(page, pageSize), queryUserBean, userIdList));
    }

    @Override
    public List<UserPO> userSelectData() {
        QueryWrapper<UserPO> wrapper = new QueryWrapper<>();
        wrapper.select("id", "real_name").eq("deleted", 0);
        return userMapper.selectList(wrapper);
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

    private boolean isAdmin(Integer userId) {
        Set<Integer> adminIdSet = getAdminIdSet();
        return adminIdSet.contains(userId);
    }

    /**
     * 返回管理员id集合
     * @return
     */
    Set<Integer> getAdminIdSet(){
        return userMapper.getAdminIdSet();
    }
}
