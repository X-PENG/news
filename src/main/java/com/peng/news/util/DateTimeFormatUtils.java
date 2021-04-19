package com.peng.news.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/15 14:54
 */
public class DateTimeFormatUtils {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime localDateTime){
        return formatter.format(localDateTime);
    }
}
