package com.peng.news.controller.management;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.Result;
import com.peng.news.model.dto.ReModificationInfoDTO;
import com.peng.news.model.dto.ReviewFailInfoDTO;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.paramBean.NewsBeanForEditorSave;
import com.peng.news.model.paramBean.QueryNewsBeanForEditor;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.NewsServiceForEditor;
import com.peng.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新闻中转站菜单相关接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:39
 */
@RestController
@RequestMapping("/management/news/transit")
public class NewsTransitController {


    @Autowired
    NewsServiceForEditor newsServiceForEditor;

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }

    /**
     * 分页+条件查询中转状态的新闻列表
     * @param page
     * @param pageSize
     * @param queryBean
     * @return
     */
    @GetMapping("/")
    public Result<CustomizedPage<NewsVO>> transitNewsList(Integer page, Integer pageSize, QueryNewsBeanForEditor queryBean) {
        return Result.success(newsServiceForEditor.transitNewsList(page, pageSize, queryBean));
    }

    /**
     * 查询所有中转状态，用于状态下拉列表
     * @return
     */
    @GetMapping("/transitStatusList")
    public Result<NewsStatus[]> transitStatusList() {
        return Result.success(NewsStatus.TRANSIT_STATUS_ARRAY);
    }

    /**
     * 查询所有传稿人，用于录入人下拉列表
     * @return
     */
    @GetMapping("/userSelectData")
    public Result<List<UserPO>> userSelectData() {
        return Result.success(userService.userSelectData());
    }


    /**
     * 查询一个中转状态的新闻，用于预览或编辑时查询
     * @return
     */
    @GetMapping("/{newsId}")
    public Result<NewsPO> selectTransitNews(@PathVariable int newsId) {
        return Result.success(newsServiceForEditor.selectTransitNews(newsId));
    }

    /**
     * 删除一个中转状态的新闻
     * @return
     */
    @DeleteMapping("/{newsId}")
    public Result deleteTransitNews(@PathVariable int newsId) {
        newsServiceForEditor.deleteTransitNews(newsId);
        return Result.success("删除成功！");
    }

    /**
     * 提交审核中转状态的新闻
     * @return
     */
    @PutMapping("/{newsId}")
    public Result submitTransitNewsToReview(@PathVariable int newsId) {
        newsServiceForEditor.submitTransitNewsToReview(newsId);
        return Result.success("提交成功！");
    }


    /**
     * 保存修改 或 保存并提交审核
     * @param tag 等于1，就是保存修改；等于2，就是保存并提交审核；其他值，非法请求！
     * @param news
     * @return
     */
    @PostMapping("/{tag}")
    public Result saveOrSaveAndSubmitReview(@PathVariable int tag, @RequestBody NewsBeanForEditorSave news) {
        newsServiceForEditor.saveOrSaveAndSubmitReview(tag, news);
        return Result.success(tag == 1 ? "保存成功！" : "成功保存并提交审核！");
    }

    /**
     * 查询审核失败相关信息
     * @return
     */
    @GetMapping("/reviewFailInfo/{newsId}")
    public Result<ReviewFailInfoDTO> queryReviewFailInfo(@PathVariable int newsId){
        return Result.success(newsServiceForEditor.queryReviewFailInfo(newsId));
    }

    /**
     * 查询打回修改相关信息
     * @return
     */
    @GetMapping("/reModification/{newsId}")
    public Result<ReModificationInfoDTO> queryReModificationInfo(@PathVariable int newsId){
        return Result.success(newsServiceForEditor.queryReModificationInfo(newsId));
    }
}
