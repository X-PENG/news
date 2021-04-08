package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.queryBean.QueryUserBean;
import com.peng.news.model.vo.ResourceVO;
import com.peng.news.model.vo.UserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 14:10
 */
public interface UserService extends UserDetailsService {
    /**
     * 得到当前用户的菜单列表(利用数据库的连接查询形成了父子关系的菜单列表)
     * @param userId
     * @return
     */
    List<ResourceVO> getMenusOfCurUser(Integer userId);

    /**
     * 得到当前用户的菜单列表（方法2，尚未形成父子关系的菜单列表）
     * 用户-->角色-->菜单
     * @param userId
     * @return
     */
    List<ResourceVO> getMenusOfCurUser2(Integer userId);

    /**
     * 根据userId查询个人信息
     * @param userId
     * @return
     */
    UserVO getUserInfoByUserId(Integer userId);

    boolean updatePersonalInfo(UserPO userPO);

    boolean updatePersonalPassword(String password);

    boolean addUser(UserPO userPO);

    boolean delUser(Integer userId);

    boolean setUserLocked(Integer userId, boolean locked);

    List<Integer> getRolesOfUser(Integer userId);


    boolean setRolesForUser(Integer userId, Integer[] roleIds);

    CustomizedPage<UserVO> userList(int page, int pageSize, QueryUserBean queryUserBean);
}
