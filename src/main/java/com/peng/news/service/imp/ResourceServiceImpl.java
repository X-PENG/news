package com.peng.news.service.imp;

import com.peng.news.cache.constant.CacheConstants;
import com.peng.news.mapper.ResourceMapper;
import com.peng.news.model.vo.ResourceVO;
import com.peng.news.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(cacheNames = CacheConstants.CACHE_NAME_REAR_END, key = "'" + CacheConstants.CACHE_KEY_ALL_RESOURCE_WITH_ROLE_LIST + "'")
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
