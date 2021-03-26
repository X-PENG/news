package com.peng.news.component;

import com.peng.news.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 拦截审核相关的请求（1/2/3/4审等等），如果审核等级大于系统配置，则报404错误
 * @author PENG
 * @version 1.0
 * @date 2021/3/26 10:13
 */
@Component
@Slf4j
public class ReviewInterceptor implements HandlerInterceptor {

    @Autowired
    SystemConfigService systemConfigService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        Map map = (Map) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        boolean tag404 = false;
        if(map == null){
            tag404 = true;
        }else{
            String epoch = ((String) map.get("epoch"));
            try {
                int epochInt = Integer.parseInt(epoch);
                if(epochInt > systemConfigService.getCurReviewLevel()){
                    tag404 = true;
                }
            } catch (NumberFormatException e) {
                tag404 = true;
            }
        }

        if(tag404){
            resp.setStatus(404);
            return false;
        }

        return true;
    }
}
