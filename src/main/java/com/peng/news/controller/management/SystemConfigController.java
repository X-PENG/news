package com.peng.news.controller.management;

import com.peng.news.model.Result;
import com.peng.news.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统设置菜单相关接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:35
 */

@RestController
@RequestMapping("/management/system/config")
public class SystemConfigController {

    @Autowired
    SystemConfigService systemConfigService;

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }

    /**
     * 设置系统的审核等级
     * @param reviewLevel
     * @return
     */
    @PutMapping("/review")
    public Result setReviewLevel(int reviewLevel){
        systemConfigService.setReviewLevel(reviewLevel);
        return Result.success("设置成功！");
    }
}
