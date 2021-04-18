package com.peng.news.util;

/**
 * 验证url工具类
 * @author PENG
 * @version 1.0
 * @date 2021/4/17 16:07
 */
public class ValidateUrlUtils {


    public static boolean validateUrl(String url) {
        //设为null，或修剪两边空格
        if(url == null || "".equals(url = url.trim())) {
            return false;
        }

        return url.startsWith("http://") || url.startsWith("https://");
    }
}
