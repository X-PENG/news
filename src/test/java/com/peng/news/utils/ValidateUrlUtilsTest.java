package com.peng.news.utils;

import com.peng.news.util.ValidateUrlUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/17 16:18
 */
public class ValidateUrlUtilsTest {

    @Test
    public void t1(){
        String u1 = "123";
        String u2 = "dasdasd12";
        String u3 = "http://dasdasd12";
        String u4 = "https://dasdasd12";
        Assert.assertFalse(ValidateUrlUtils.validateUrl(u1));
        Assert.assertFalse(ValidateUrlUtils.validateUrl(u2));
        Assert.assertTrue(ValidateUrlUtils.validateUrl(u3));
        Assert.assertTrue(ValidateUrlUtils.validateUrl(u4));

    }
}
