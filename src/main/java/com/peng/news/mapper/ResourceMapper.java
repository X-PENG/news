package com.peng.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peng.news.model.po.ResourcePO;
import com.peng.news.model.vo.ResourceVO;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 21:08
 */
public interface ResourceMapper extends BaseMapper<ResourcePO> {

    /**
     *
     * todo 思考是一次连接查询好还是多次嵌套查询好
     * @param parentId  为null，就是查询一级菜单
     * @return
     */
    List<ResourceVO> getAllMenuByParentId(Integer parentId);

    /**
     * todo 思考是一次连接查询好还是多次嵌套查询好
     * 得到所有的资源及对应的角色列表
     * @return
     */
    List<ResourceVO> getAllResourceWithRoles();

    /**
     * 根据userId返回菜单列表（父菜单会包含子菜单）
     * @param userId
     * @return
     */
    List<ResourceVO> getMenusByUserId(Integer userId);

    /**
     * 根据userId返回菜单列表（没有形成父子关系的）
     * @param userId
     * @return
     */
    List<ResourceVO> getMenusByUserId2(Integer userId);
}
