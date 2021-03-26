package com.peng.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/23 15:45
 *
 * 统一的JSON响应格式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    String code;

    String msg;

    T data;

    boolean success;

    public static <T> Result<T> success(String msg,T data){
        return new Result<>("200", msg, data, true);
    }

    public static <T> Result<T> success(String msg){
        return success(msg, null);
    }


    public static <T> Result<T> success(T data){
        return success(null, data);
    }

    public static <T> Result<T> fail(String code, String msg){
        return new Result<>(code, msg, null, false);
    }


    public static <T> Result<T> fail(String msg){
        return fail("500", msg);
    }
}
