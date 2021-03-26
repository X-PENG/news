package com.peng.news.service;

import com.peng.news.model.vo.ResourceVO;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:00
 */
public interface ResourceService {
    List<ResourceVO> getAllResourceWithRoles();
}
