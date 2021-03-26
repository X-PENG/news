package com.peng.news.config;

import com.peng.news.component.ReviewInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/26 10:29
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    ReviewInterceptor reviewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reviewInterceptor).addPathPatterns("/management/news/review/**");
    }
}
