package com.peng.news.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peng.news.mapper.RoleMapper;
import com.peng.news.mapper.UserMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.RolePO;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.queryBean.QueryUserBean;
import com.peng.news.model.vo.ResourceVO;
import com.peng.news.model.vo.RoleVO;
import com.peng.news.model.vo.UserVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/26 19:38
 */
/**
 * 系统管理的综合测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RoleUserPrivateTest {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * 添加角色-->设置资源-->添加用户-->设置角色--->查询用户菜单、查询个人资料、修改个人资料、锁定/解锁用户、软删除用户
     */
    @Rollback(true)
    @Transactional
    @Test
    public void t1(){
        String nameEn = "Role_test1";
        String nameZh = "测试角色1";
        String desc = "测试用例中的测试数据";
        RolePO rolePO = new RolePO();
        rolePO.setNameEn(nameEn);
        rolePO.setNameZh(nameZh);
        rolePO.setDescription(desc);
        //添加角色
        roleService.addRole(rolePO);
        System.out.println("是否返回id: " + rolePO);
        //查询角色
        RolePO rolePO1 = roleMapper.selectOne(new QueryWrapper<RolePO>().eq("name_en", nameEn).eq("name_zh", nameZh).eq("description", desc));
        Integer[] resourceIds = {2,3,10,11,16,17};
        //给角色设置可访问资源
        roleService.setResourcesForRole(rolePO1.getId(), resourceIds);
        //查询角色列表以及角色对应的资源id列表
        CustomizedPage<RoleVO> rolesPage = roleService.roleList(1, 100000);
        RoleVO selectLastRole = rolesPage.getRecords().get(rolesPage.getRecords().size() - 1);
        Assert.assertEquals(selectLastRole.getNameEn(), nameEn);
        Assert.assertEquals(selectLastRole.getNameZh(), nameZh);
        Assert.assertEquals(selectLastRole.getDescription(), desc);
        //测试查询出的资源id列表
        Assert.assertEquals(selectLastRole.getResourceIdList().size(), resourceIds.length);
        Collections.sort(selectLastRole.getResourceIdList());
        for (int i = 0; i < resourceIds.length; i++) {
            Assert.assertEquals(selectLastRole.getResourceIdList().get(i), resourceIds[i]);
        }

        String username = "casetest1";
        String realName = "用例测试1";
        String passwd = username;
        boolean gender = false;
        String phone = "13966668888";
        String email = username + "@163.com";
        String descForUser = "测试用例创建的用户";
        UserPO userPO = new UserPO();
        userPO.setUsername(username);
        userPO.setRealName(realName);
        userPO.setPasswd(passwd);
        userPO.setGender(gender);
        userPO.setPhone(phone);
        userPO.setEmail(email);
        userPO.setDescription(descForUser);
        //添加用户
        userService.addUser(userPO);
        //查询用户
        UserPO userPO1 = userMapper.selectOne(new QueryWrapper<UserPO>().eq("username", username).eq("deleted", 0));
        Assert.assertEquals(userPO1.getRealName(), realName);
        Assert.assertEquals(userPO1.getPhone(), phone);
        Assert.assertEquals(userPO1.getEmail(), email);
        Assert.assertEquals(userPO1.getDescription(), descForUser);
        Assert.assertEquals(userPO1.getLocked(), false);
        Assert.assertEquals(userPO1.getDeleted(), false);
        //给用户设置角色
        userService.setRolesForUser(userPO1.getId(), new Integer[]{selectLastRole.getId()});
        //查询用户的个人资料
        UserVO userVO = userService.getUserInfoByUserId(userPO1.getId());
        Assert.assertEquals(userVO.getRoles().size(), 1);
        RoleVO roleOfUser = userVO.getRoles().get(0);
        Assert.assertEquals(roleOfUser.getId(), selectLastRole.getId());
        Assert.assertEquals(roleOfUser.getNameEn(), nameEn);
        Assert.assertEquals(roleOfUser.getNameZh(), nameZh);
        Assert.assertEquals(roleOfUser.getDescription(), desc);
        //分页查询用户
        CustomizedPage<UserVO> userPage = userService.userList(1, 10000, new QueryUserBean());
        UserVO userVO1 = userPage.getRecords().get(0);
        Assert.assertEquals(userVO1.getRealName(), realName);
        Assert.assertEquals(userVO1.getPhone(), phone);
        Assert.assertEquals(userVO1.getEmail(), email);
        Assert.assertEquals(userVO1.getDescription(), descForUser);
        Assert.assertEquals(userVO1.getLocked(), false);
        Assert.assertEquals(userVO1.getDeleted(), false);
        Assert.assertEquals(userVO1.getRoles().size(), 1);
        RoleVO roleVO = userVO1.getRoles().get(0);
        Assert.assertEquals(roleVO.getId(), selectLastRole.getId());
        Assert.assertEquals(roleVO.getNameEn(), nameEn);
        Assert.assertEquals(roleVO.getNameZh(), nameZh);
        Assert.assertEquals(roleVO.getDescription(), desc);
        //查询用户菜单
        List<ResourceVO> menus = userService.getMenusOfCurUser2(userPO1.getId());
        Assert.assertEquals(menus.size(), 2);//只有两个1级菜单
        ResourceVO r1 = menus.get(0);
        ResourceVO r2 = menus.get(1);
        if (r1.getId() == 2){
            Assert.assertEquals(r1.getName(), "新闻栏目管理");
            Assert.assertTrue(CollectionUtils.isEmpty(r1.getChildren()));
            Assert.assertEquals(r1.getUrl(), "/management/column/**");
            Assert.assertEquals(r2.getId().intValue(), 3);
            Assert.assertEquals(r2.getName(), "新闻管理");
            Assert.assertEquals(r2.getChildren().size(), 4);
            Assert.assertEquals(r2.getUrl(), "/management/news/public/**");
        }else{
            Assert.assertEquals(r2.getName(), "新闻栏目管理");
            Assert.assertEquals(r2.getChildren().size(), 0);
            Assert.assertEquals(r2.getUrl(), "/management/column/**");
            Assert.assertEquals(r1.getId().intValue(), 3);
            Assert.assertEquals(r1.getName(), "新闻管理");
            Assert.assertEquals(r1.getChildren().size(), 4);
            Assert.assertEquals(r1.getUrl(), "/management/news/public/**");
        }

        phone = "19188886666";
        email = "casetest1updated@163.com";
        descForUser = "测试修改";
        UserPO userPOForUpdate = new UserPO();
        userPOForUpdate.setId(userPO1.getId());
        userPOForUpdate.setPhone(phone);
        userPOForUpdate.setEmail(email);
        userPOForUpdate.setDescription(descForUser);
        userPOForUpdate.setUsername("updateTest");
        userPOForUpdate.setGender(!userPO1.getGender());
        userPOForUpdate.setRealName("测试用例测试修改");
        userPOForUpdate.setLocked(!userPO1.getLocked());
        userPOForUpdate.setDeleted(!userPO1.getDeleted());
        //修改个人资料
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                UserVO principal = new UserVO();
                principal.setId(userPOForUpdate.getId());
                return new TestingAuthenticationToken(principal, "123");
            }

            @Override
            public void setAuthentication(Authentication authentication) {

            }
        });
        userService.updatePersonalInfo(userPOForUpdate);
        UserPO userPO2 = userMapper.selectById(userPOForUpdate.getId());
        Assert.assertEquals(userPO2.getUsername(), userPO1.getUsername());
        Assert.assertEquals(userPO2.getRealName(), userPO1.getRealName());
        Assert.assertEquals(userPO2.getGender(), userPO1.getGender());
        Assert.assertEquals(userPO2.getLocked(), userPO1.getLocked());
        Assert.assertEquals(userPO2.getDeleted(), userPO1.getDeleted());
        Assert.assertEquals(userPO2.getPhone(), phone);
        Assert.assertEquals(userPO2.getEmail(), email);
        Assert.assertEquals(userPO2.getDescription(), descForUser);
        //锁定用户
        userService.setUserLocked(userPO2.getId(), true);
        Assert.assertEquals(userMapper.selectById(userPO2.getId()).getLocked(), true);
        //解锁用户
        userService.setUserLocked(userPO2.getId(), false);
        Assert.assertEquals(userMapper.selectById(userPO2.getId()).getLocked(), false);
        //软删除用户
        userService.delUser(userPO2.getId());
        //根据用户名查询用户
        try {
            userService.loadUserByUsername(userPO1.getUsername());
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UsernameNotFoundException);
        }
        UserPO userPO3 = userMapper.selectById(userPO2.getId());
        Assert.assertEquals(userPO3.getUsername(), userPO1.getUsername());
        Assert.assertEquals(userPO3.getRealName(), userPO1.getRealName());
        Assert.assertEquals(userPO3.getGender(), userPO1.getGender());
        Assert.assertEquals(userPO3.getLocked(), false);
        Assert.assertEquals(userPO3.getDeleted(), true);
        Assert.assertEquals(userPO3.getPhone(), phone);
        Assert.assertEquals(userPO3.getEmail(), email);
        Assert.assertEquals(userPO3.getDescription(), descForUser);
        //删除角色
        roleService.delRole(selectLastRole.getId());
        Assert.assertNull(roleMapper.selectById(selectLastRole.getId()));
    }
}
