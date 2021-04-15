package com.peng.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.paramBean.QueryUserBean;
import com.peng.news.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 14:10
 */
public interface UserMapper extends BaseMapper<UserPO> {

    UserVO getUserByUsername(String username);

    UserVO getUserByUserId(Integer userId);

    IPage<UserVO> selectUsersByPage(Page page, @Param("conditionBean") QueryUserBean queryUserBean, @Param("userIdList") List<Object> userIdList);

    /**
     * 软删除用户
     * @param userId
     * @return
     */
    Integer softDelUser(Integer userId);

    /**
     * 查询所有具有系统管理员角色的用户id列表
     * @return
     */
    Set<Integer> getAdminIdSet();

    /**
     * 根据id查询用户的名称，包括：用户名和实名
     * @param userId
     * @return
     */
    @Select("select username,real_name from user where id = #{userId}")
    UserPO selectNameById(@Param("userId") Integer userId);
}
