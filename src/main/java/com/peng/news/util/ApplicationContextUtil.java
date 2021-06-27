package com.peng.news.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/6/24 15:46
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<T> classType){
        return APPLICATION_CONTEXT.getBean(classType);
    }

    public static Object getBean(String beanName){
        return APPLICATION_CONTEXT.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> classType){
        return APPLICATION_CONTEXT.getBean(beanName, classType);
    }
}
