package com.peng.news.service.imp;

import com.peng.news.mapper.ResourceMapper;
import com.peng.news.model.vo.ResourceVO;
import com.peng.news.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:05
 */
@Service
public class ResourceServiceImpl implements ResourceService {


    @Autowired
    ResourceMapper resourceMapper;

    /**
     * todo 做缓存
     * 查询所有资源（包括不开启的资源），并且包含资源对应的角色列表
     * @return
     */
    @Override
    public List<ResourceVO> getAllResourceWithRoles() {
        return resourceMapper.getAllResourceWithRoles();
    }

    @Override
    public List<ResourceVO> getOneLevelMenuWithChildren() {
        //一级菜单的parent_id为null
        return resourceMapper.getAllMenuByParentId(null);
    }
}
