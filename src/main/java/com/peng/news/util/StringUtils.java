package com.peng.news.util;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 20:17
 */
public class StringUtils {

    public static boolean isNotEmpty(String s){
        return s != null && s.trim().length() != 0;
    }
}
