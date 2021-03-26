package com.peng.news.model.vo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author  PENG
 * @date  2021/3/23 14:04
 * @version 1.0
 */
@Data
public class UserVO implements UserDetails {

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
     * 配置用户到角色的一对多关系
     */
    List<RoleVO> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNameEn())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
