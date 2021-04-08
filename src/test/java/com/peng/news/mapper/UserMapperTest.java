package com.peng.news.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.model.queryBean.QueryUserBean;
import com.peng.news.model.vo.UserVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 15:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void getUserByUsername(){
        UserVO user1 = userMapper.getUserByUsername("PENG");
        System.out.println(user1);
        UserVO user2 = userMapper.getUserByUsername("20210323");
        System.out.println(user2);
    }

    @Test
    public void selectUsersByPage(){
        Integer count = userMapper.selectCount(null);
        int size = 2;
        int totalPage = (count + size - 1) / size;
        int curPage;
        for(curPage = 1; curPage <= totalPage; curPage++){
            Page page =new Page(curPage, 2);
            IPage<UserVO> userVOIPage = userMapper.selectUsersByPage(page, new QueryUserBean(),null);
            Assert.assertEquals(userVOIPage.getTotal(), count.longValue());
            Assert.assertEquals(userVOIPage.getPages(), totalPage);
            Assert.assertEquals(userVOIPage.getCurrent(), curPage);
            Assert.assertEquals(userVOIPage.getSize(), size);
            Assert.assertEquals(userVOIPage.getRecords().size(), (curPage < totalPage) ? size : (count - count/size * size));
            System.out.println(userVOIPage.getRecords().size());
        }
    }

    /**
     * 测试返回管理员id集合
     */
    @Test
    public void getAdminIdSet(){
        Set<Integer> adminIdSet = userMapper.getAdminIdSet();
        Assert.assertEquals(adminIdSet.size(), 1);
    }
}
