package com.peng.news.controller.management;

import com.peng.news.model.Result;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.vo.ResourceVO;
import com.peng.news.model.vo.UserVO;
import com.peng.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录用户共有的接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 16:39
 */
@RestController
@RequestMapping("/management/private")
public class PrivateController {

    @Autowired
    UserService userService;


    /**
     * 得到当前登录用户的菜单
     * @return
     */
    @GetMapping("/menu")
    public Result<List<ResourceVO>> getMenusOfCurUser(){
        return Result.success(userService.getMenusOfCurUser2(null));
    }

    /**
     * 查询个人资料
     * @return
     */
    @GetMapping("/")
    public Result<UserVO> getPersonalInfo(){
        return Result.success(userService.getUserInfoByUserId(null));
    }

    /**
     * 修改个人信息
     * @return
     */
    @PutMapping("/")
    public Result updatePersonalInfo(UserPO userPO){
        userPO = UserPO.forUpdatePersonalInfo(userPO);
        userService.updatePersonalInfo(userPO);
        return Result.success("修改成功！");
    }

    /**
     * 修改密码
     * @return
     */
    @PutMapping("/key")
    public Result updatePersonalPassWord(UserPO userPO){
        userService.updatePersonalPassword(userPO.getId(), userPO.getPasswd());
        return Result.success("修改成功！");
    }



}
