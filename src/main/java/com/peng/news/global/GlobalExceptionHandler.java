package com.peng.news.global;

import com.peng.news.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/7 15:44
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.warn("", e);
        return Result.fail(e.getMessage());
    }
}
