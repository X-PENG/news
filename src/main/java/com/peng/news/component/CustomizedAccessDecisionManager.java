package com.peng.news.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 17:22
 */
@Component
public class CustomizedAccessDecisionManager implements AccessDecisionManager {

    /**
     * 知道了访问接口需要哪些角色，也知道了用户具有哪些角色；
     * 就可以自定义规则进行权限认证！
     * @param authentication
     * @param object
     * @param configAttributes
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            String requireRole = configAttribute.getAttribute();
            if(requireRole.equals(CustomizedSecurityMetadataSource.LOGIN_TAG)){
                //需要登录才能访问接口
                if(authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登录，请登录！");
                }else{
                    return;
                }
            }
            if(requireRole.equals(CustomizedSecurityMetadataSource.FORBIDDEN_TAG)){
                throw new AccessDeniedException("禁止访问！");
            }

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if(authority.getAuthority().equals(requireRole)){
                    //自定义规则：用户具有其中一个角色，则认证通过
                    return;
                }
            }
        }

        throw new AccessDeniedException("权限不足，请联系管理员！");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
