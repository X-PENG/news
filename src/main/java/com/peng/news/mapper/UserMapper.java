package com.peng.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.vo.UserVO;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 14:10
 */
public interface UserMapper extends BaseMapper<UserPO> {

    UserVO getUserByUsername(String username);

    UserVO getUserByUserId(Integer userId);

    IPage<UserVO> selectUsersByPage(Page page);

    /**
     * 软删除用户
     * @param userId
     * @return
     */
    Integer softDelUser(Integer userId);
}
