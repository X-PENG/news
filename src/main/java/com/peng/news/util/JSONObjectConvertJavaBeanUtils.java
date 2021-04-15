package com.peng.news.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * 利用反射，将JSONObject对象转成对应的JavaBean对象
 * @author PENG
 * @version 1.0
 * @date 2021/4/15 17:22
 */
@Slf4j
public class JSONObjectConvertJavaBeanUtils {

    public static <T> T convert(JSONObject jsonObject, Class<T> clazz) {
        try {
            T t = clazz.newInstance();

            Set<String> keySet = jsonObject.keySet();

            for (String key : keySet) {
                Field attr = clazz.getDeclaredField(key);
                attr.setAccessible(true);

                Object value = jsonObject.get(key);
                //如果还是JSONObject对象，就递归
                if(value instanceof JSONObject) {
                    attr.set(t, convert((JSONObject) value, attr.getType()));
                }else {
                    attr.set(t, value);
                }
            }

            return t;
        }catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
}
