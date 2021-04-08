package com.peng.news.model.queryBean;

import lombok.Data;

/**
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
