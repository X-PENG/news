package com.peng.news.controller.frontend;

import com.peng.news.model.Result;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.FrontendIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 门户网站首页的接口
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 16:27
 */
@RestController
@RequestMapping("/frontend/index")
public class IndexController {

    @Autowired
    FrontendIndexService frontendIndexService;

    /**
     * 查询所有开启的一级栏目，并且按菜单序号排序
     * @return
     */
    @GetMapping("/cols")
    public Result<List<NewsColumnVO>> allEnabledOneLevelColsOrderByMenuOrder() {
        return Result.success(frontendIndexService.allEnabledOneLevelColsOrderByMenuOrder());
    }

    /**
     * 查询新闻头条
     * @return
     */
    @GetMapping("/headlines")
    public Result<NewsPO> getHeadLines(){
        return Result.success(frontendIndexService.getHeadLines());
    }
}
