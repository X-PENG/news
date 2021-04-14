package com.peng.news.service;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.RolePO;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.paramBean.QueryUserBean;
import com.peng.news.model.vo.ResourceVO;
import com.peng.news.model.vo.UserVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 10:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    /**
     * 得到用户的菜单列表(法2)
     */
    @Test
    public void getMenusOfUser2(){
        List<ResourceVO> menus = userService.getMenusOfCurUser2(1);
        Assert.assertEquals(menus.size(), 6);
    }

    /**
     * 比较法1和法2的速度。
     * 测试结果：
     *    13369,8498
     *    9861,8512
     *    8602,8063
     *    10030,7804
     *    11170,9351
     *    12117,11190
     *    12028,10001
     *    10202,10665
     *    9365,8274
     *    10207,10008
     *
     */
    @Test
    public void speed(){
        for (int i = 0; i < 10; i++) {
            once();
        }
    }

    /**
     * 一次测试
     */
    @Test
    public void once(){
        int n = 100;
        long start1, end1, start2, end2;
        start1 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            userService.getMenusOfCurUser(1);
        }
        end1 = System.currentTimeMillis();


        start2 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            userService.getMenusOfCurUser2(1);
        }
        end2 = System.currentTimeMillis();

        System.out.println((end1 - start1) + "," +  (end2 - start2));
    }


    /**
     * 分页条件查询用户
     */
    @Transactional
    @Rollback(true)
    @Test
    public void t3(){

        //添加1个角色
        RolePO rolePO = new RolePO();
        rolePO.setNameEn("ROLE_20210407");
        rolePO.setNameZh("20210407测试");
        roleService.addRole(rolePO);

        String username_prefix = "20210407";
        String realName_prefix = "分页条件查询";
        String phone_prefix = "1320210407";
        String email_prefix = "20210407";
        String passwd = UUID.randomUUID().toString();
        int size = 10;
        for(int i=0; i<size; i++){
            UserPO userPO = new UserPO();
            userPO.setUsername(username_prefix + i);
            userPO.setRealName(realName_prefix + i);
            userPO.setPhone(phone_prefix + i);
            userPO.setEmail(email_prefix + i + "@163.com");
            userPO.setPasswd(passwd);
            userPO.setGender(i % 2 == 0);//5 5开
            userService.addUser(userPO);

            try {
                userService.setUserLocked(userPO.getId(), i % 3 ==0);//4真6假
            } catch (Exception e) {
            }

            userService.setRolesForUser(userPO.getId(), new Integer[]{rolePO.getId()});
        }
        int page = 1;
        int pageSize = 100000;
        QueryUserBean queryUserBean = new QueryUserBean();
        queryUserBean.setUsername(username_prefix);
        CustomizedPage<UserVO> userVOCustomizedPage = userService.userList(page, pageSize, queryUserBean);
        Assert.assertEquals(userVOCustomizedPage.getRecords().size(), size);

        queryUserBean = new QueryUserBean();
        queryUserBean.setRealName(realName_prefix);
        userVOCustomizedPage = userService.userList(page, pageSize, queryUserBean);
        Assert.assertEquals(userVOCustomizedPage.getRecords().size(), size);

        queryUserBean = new QueryUserBean();
        queryUserBean.setPhone(phone_prefix);
        userVOCustomizedPage = userService.userList(page, pageSize, queryUserBean);
        Assert.assertEquals(userVOCustomizedPage.getRecords().size(), size);

        queryUserBean = new QueryUserBean();
        queryUserBean.setEmail(email_prefix);
        userVOCustomizedPage = userService.userList(page, pageSize, queryUserBean);
        Assert.assertEquals(userVOCustomizedPage.getRecords().size(), size);

        queryUserBean = new QueryUserBean();
        queryUserBean.setUsername(username_prefix);
        queryUserBean.setRealName(realName_prefix);
        queryUserBean.setPhone(phone_prefix);
        queryUserBean.setEmail(email_prefix);
        queryUserBean.setGender(true);
        userVOCustomizedPage = userService.userList(page, pageSize, queryUserBean);
        Assert.assertEquals(userVOCustomizedPage.getRecords().size(), 5);

        queryUserBean = new QueryUserBean();
        queryUserBean.setUsername(username_prefix);
        queryUserBean.setRealName(realName_prefix);
        queryUserBean.setPhone(phone_prefix);
        queryUserBean.setEmail(email_prefix);
        queryUserBean.setLocked(true);
        userVOCustomizedPage = userService.userList(page, pageSize, queryUserBean);
        Assert.assertEquals(userVOCustomizedPage.getRecords().size(), 4);

        queryUserBean = new QueryUserBean();
        queryUserBean.setUsername(username_prefix);
        queryUserBean.setRealName(realName_prefix);
        queryUserBean.setPhone(phone_prefix);
        queryUserBean.setEmail(email_prefix);
        queryUserBean.setGender(false);//1 3 5 7 9
        queryUserBean.setLocked(true);//0 3 6 9
        userVOCustomizedPage = userService.userList(page, pageSize, queryUserBean);
        Assert.assertEquals(userVOCustomizedPage.getRecords().size(), 2);

        queryUserBean = new QueryUserBean();
        queryUserBean.setUsername(username_prefix);
        queryUserBean.setRealName(realName_prefix);
        queryUserBean.setPhone(phone_prefix);
        queryUserBean.setEmail(email_prefix);
        queryUserBean.setGender(true);//0 2 4 6 8
        queryUserBean.setLocked(false);//1 2 4 5 7 8
        queryUserBean.setRoleId(rolePO.getId());
        userVOCustomizedPage = userService.userList(page, pageSize, queryUserBean);
        Assert.assertEquals(userVOCustomizedPage.getRecords().size(), 3);

    }
}
