package com.peng.news.model.enums;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 23:09
 */
public class NewsStatusTest {

    @Test
    public void t1(){
        Class<NewsStatus> clazz = NewsStatus.class;
        NewsStatus[] enumConstants = clazz.getEnumConstants();
        System.out.println(Arrays.asList(enumConstants));
        for(int i=0;i<enumConstants.length;i++){
            NewsStatus newsStatus = enumConstants[i];
            int code = newsStatus.getCode();
            Assert.assertEquals(NewsStatus.fromCode(code).getName(), newsStatus.getName());
        }
    }
}
