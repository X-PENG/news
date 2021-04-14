package com.peng.news.component;

import com.peng.news.model.vo.ResourceVO;
import com.peng.news.model.vo.RoleVO;
import com.peng.news.service.ResourceService;
import com.peng.news.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 17:15
 */
@Component
public class CustomizedSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /**
     * 没有匹配到，默认返回该标志，表示登录即可
     */
    public static final String LOGIN_TAG = "ROLE_login";

    public static final String FORBIDDEN_TAG = "Forbidden";

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    ResourceService resourceService;

    /**
     * 根据请求的url，去匹配被权限管理的资源，返回权限资源对应的角色列表
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation invocation = (FilterInvocation) object;
        String requestUrl = invocation.getRequestUrl();
        List<ResourceVO> allResourceWithRoles = resourceService.getAllResourceWithRoles();
        //遍历匹配资源
        for (ResourceVO resource : allResourceWithRoles) {
            String urlPattern = resource.getUrl();
            if(StringUtils.isNotEmpty(urlPattern) && antPathMatcher.match(urlPattern, requestUrl)){
                /**
                 * 1.匹配上了资源，但是资源未开启，则forbidden
                 * 2.如果进行权限管理的资源没有配置任何角色可以访问，则直接禁止所有人访问
                 */
                if(resource.getEnabled() == false || resource.getRoles().size() == 0){
                    return SecurityConfig.createList(FORBIDDEN_TAG);
                }
                //返回资源对应的角色列表
                List<RoleVO> roles = resource.getRoles();
                List<String> roleStrList = roles.stream().map(role -> role.getNameEn()).collect(Collectors.toList());
                return SecurityConfig.createList(roleStrList.toArray(new String[0]));
            }
        }

        if(requestUrl.contains("/management/news/review/")){
            //没有匹配到的审核资源，直接forbidden
            return SecurityConfig.createList(FORBIDDEN_TAG);
        }

        return SecurityConfig.createList(LOGIN_TAG);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
