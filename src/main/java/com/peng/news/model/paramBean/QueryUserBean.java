package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 封装查询用户的条件参数
 * @author PENG
 * @version 1.0
 * @date 2021/4/7 16:44
 */
@Data
public class QueryUserBean {

    String username;

    String realName;

    Boolean gender;

    String phone;

    String email;

    Boolean locked;

    Integer roleId;

}
