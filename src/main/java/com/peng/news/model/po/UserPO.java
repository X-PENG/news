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
     * 返回用于修改个人资料的UserPO。
     * 用户只能修改自己的 phone、email、description
     * @param userPO
     * @return
     */
    public static UserPO forUpdatePersonalInfo(UserPO userPO){
        String phone = userPO.getPhone();
        String email = userPO.getEmail();

        if(phone == null || "".equals(phone = phone.trim())) {
            throw new RuntimeException("用户电话不能为空");
        }

        //格式化为null或修剪字符串两边空格
        if(email == null || "".equals(email = email.trim())) {
            email = null;
        }

        UserPO userPO1 = new UserPO();
        userPO1.setId(userPO.getId());
        userPO1.setPhone(phone);
        userPO1.setEmail(email);
        userPO1.setDescription(userPO.getDescription());
        return userPO1;
    }
}
