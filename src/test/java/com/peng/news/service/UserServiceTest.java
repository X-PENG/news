package com.peng.news.service;

import com.peng.news.model.vo.ResourceVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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

    /**
     * 得到用户的菜单列表(法2)
     */
    @Test
    public void getMenusOfUser2(){
        List<ResourceVO> menus = userService.getMenusOfCurUser2(1);
        Assert.assertEquals(menus.size(), 5);
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

}
