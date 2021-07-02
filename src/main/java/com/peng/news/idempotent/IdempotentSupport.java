package com.peng.news.idempotent;

import javax.servlet.http.HttpServletRequest;

/**
 * 幂等支持接口
 * @author PENG
 * @version 1.0
 * @date 2021/6/27 19:02
 */
public interface IdempotentSupport {
    /**
     * 检查是否重复提交请求
     * @param request
     * @return
     */
    boolean isRepeatRequest(HttpServletRequest request);
}
