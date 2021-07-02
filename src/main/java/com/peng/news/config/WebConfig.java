package com.peng.news.config;

import com.peng.news.component.ReviewInterceptor;
import com.peng.news.idempotent.IdempotentInterceptor;
import com.peng.news.idempotent.WrapRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
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

    @Autowired
    IdempotentInterceptor idempotentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(idempotentInterceptor).addPathPatterns("/management/**");
        registry.addInterceptor(reviewInterceptor).addPathPatterns("/management/news/review/**");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        WrapRequestFilter wrapRequestFilter = new WrapRequestFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(wrapRequestFilter);
        filterRegistrationBean.setName("wrapRequestFilter");
        filterRegistrationBean.addUrlPatterns("/management/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
