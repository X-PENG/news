package com.peng.news.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 16:10
 */
@TableName("user_role")
@Data
@NoArgsConstructor
public class UserRolePO {

    public UserRolePO(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @TableId(type = IdType.AUTO)
    Integer id;

    Integer userId;

    Integer roleId;
}



