package com.peng.news.mapper;

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
 * @date 2021/3/23 23:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ResourceMapperTest {

    @Autowired
    ResourceMapper resourceMapper;

    /**
     * 得到所有一级菜单，并且递归查询子菜单
     */
    @Test
    public void getOneLevelMenus(){
        List<ResourceVO> oneLevelMenus = resourceMapper.getAllMenuByParentId(null);
        Assert.assertEquals(oneLevelMenus.size(), 5);
    }

    /**
     * 得到权限管理的所有资源，以及资源对应的权限列表
     */
    @Test
    public void getAllResourceWithRoles(){
        List<ResourceVO> allResourceWithRoles = resourceMapper.getAllResourceWithRoles();
        Assert.assertEquals(allResourceWithRoles.size(), 17);
        for (ResourceVO resource : allResourceWithRoles) {
            Assert.assertNotNull(resource.getRoles());
        }
    }

    /**
     * 得到一个用户的菜单列表
     */
    @Test
    public void getMenusOfUser(){
        List<ResourceVO> menusOfUser = resourceMapper.getMenusByUserId(1);
        Assert.assertEquals(menusOfUser.size(), 5);
    }
}
