package com.peng.news.idempotent;

import com.alibaba.fastjson.JSON;
import com.peng.news.util.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis实现幂等
 * @author PENG
 * @version 1.0
 * @date 2021/7/2 9:11
 */
@Component
public class RedisIdempotentSupportImpl extends AbstractIdempotentSupport{

    /**
     * key默认值为 default
     */
    static final String DEFAULT_VALUE = "";

    /**
     * key默认过期时间：30秒
     */
    static final long DEFAULT_EXPIRE_TIME = 30 * 1000;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    protected String convertKey(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        //获取url路径参数
        Map pathVariables  = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if(pathVariables != null && pathVariables.size() != 0) {
            stringBuilder.append(JSON.toJSONString(pathVariables));
        }

        //获取查询字符串
        String queryString = request.getQueryString();
        if(queryString != null) {
            stringBuilder.append(queryString);
        }
        //获取请求体
        String bodyString = getBodyString(request);
        if(bodyString != null && !"".equals(bodyString)) {
            stringBuilder.append(bodyString);
        }

        String requestStr = stringBuilder.toString();
        return HashUtils.getMd5(requestStr);
    }

    @Override
    protected boolean hasMark(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    @Override
    protected void leaveMark(String key) {
        stringRedisTemplate.opsForValue().set(key, DEFAULT_VALUE, DEFAULT_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    private String getBodyString(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try(
            BufferedReader bufferedReader = request.getReader()
        ){
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
