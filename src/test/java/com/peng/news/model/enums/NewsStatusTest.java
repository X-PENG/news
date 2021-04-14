package com.peng.news.model.enums;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 23:09
 */
public class NewsStatusTest {

    @Test
    public void t1(){
        for(int i=0;i<NewsStatus.values().length;i++){
            NewsStatus newsStatus = NewsStatus.values()[i];
            int code = newsStatus.getCode();
            Assert.assertEquals(NewsStatus.fromCode(code).getName(), newsStatus.getName());
        }
    }
}
