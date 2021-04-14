package com.peng.news.util;

import com.peng.news.model.vo.UserVO;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 21:51
 */
public class UserUtils {

    public static UserVO getUser(){
        return (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
