package com.peng.news.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;


/**
 * @author  PENG
 * @date  2021/3/23 14:04
 * @version 1.0
 */
@TableName("user")
@Data
public class UserPO {

    @TableId(type = IdType.AUTO)
    Integer id;

    String username;

    String realName;

    String passwd;

    /**
     * false：女；true：男
     */
    Boolean gender;

    String phone;

    String email;

    String description;

    Boolean locked;

    Boolean deleted;

    Timestamp createTime;

    Timestamp updateTime;

    /**
     * 返回修改个人资料的UserPO。
     * 用户只能修改自己的 phone、email、description
     * @param userPO
     * @return
     */
    public static UserPO forUpdatePersonalInfo(UserPO userPO){
        UserPO userPO1 = new UserPO();
        userPO1.setId(userPO.getId());
        userPO1.setPhone(userPO.getPhone());
        userPO1.setEmail(userPO.getEmail());
        userPO1.setDescription(userPO.getDescription());
        return userPO1;
    }
}
