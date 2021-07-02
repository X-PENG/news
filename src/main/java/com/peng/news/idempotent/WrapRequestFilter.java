package com.peng.news.idempotent;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 包装HttpServletRequest的过滤器。
 * 过滤器是客户端和服务端的中间层组件，用于在Servlet之外对HttpServletRequest或HttpServletResponse进行修改
 * @author PENG
 * @version 1.0
 * @date 2021/7/2 15:18
 */
public class WrapRequestFilter implements Filter{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest wrapRequest = request;
        if(request instanceof HttpServletRequest) {
            wrapRequest = new InputStreamReadRepeatableHttpServletRequest(((HttpServletRequest) request));
        }

        chain.doFilter(wrapRequest, response);
    }
}
