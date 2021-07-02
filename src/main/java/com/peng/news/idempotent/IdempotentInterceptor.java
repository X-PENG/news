package com.peng.news.idempotent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 幂等拦截器
 * @author PENG
 * @version 1.0
 * @date 2021/6/27 18:51
 */
@Component
public class IdempotentInterceptor implements HandlerInterceptor {

    @Autowired
    IdempotentSupport idempotentSupport;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //需要实现幂等
        if(handlerMethod.hasMethodAnnotation(IdempotentApi.class) && idempotentSupport.isRepeatRequest(request)) {
            throw new RuntimeException("不允许重复提交请求");
        }

        return true;
    }
}
