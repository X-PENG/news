package com.peng.news.controller.management;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.Result;
import com.peng.news.model.paramBean.NewsBeanForReviewerSave;
import com.peng.news.model.paramBean.QueryNewsBeanForReviewer;
import com.peng.news.model.paramBean.ReviewResultParamBean;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.NewsServiceForReviewer;
import com.peng.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新闻审核接口，{epoch}表示第几轮审核
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:40
 */
@RestController
@RequestMapping("/management/news/review/{epoch}")
public class NewsReviewController {


    @Autowired
    NewsServiceForReviewer newsServiceForReviewer;

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String hello(@PathVariable int epoch){
        return this.getClass().getName() + " hello[" + epoch + "]";
    }

    /**
     * 查询状态为审核中 且 审核轮次为epoch的新闻列表
     * @param epoch
     * @param page
     * @param pageSize
     * @param queryBean
     * @return
     */
    @GetMapping("/")
    public Result<CustomizedPage<NewsVO>> underReviewNewsList(@PathVariable int epoch,
                                                              Integer page, Integer pageSize,
                                                              QueryNewsBeanForReviewer queryBean) {
        return Result.success(newsServiceForReviewer.underReviewNewsList(epoch, page, pageSize, queryBean));
    }

    /**
     * 查询用户下拉菜单数据
     * @return
     */
    @GetMapping("/userSelectData")
    public Result<List<UserPO>> userSelectData(){
        return Result.success(userService.userSelectData());
    }

    /**
     * 查询一个审核中的新闻
     * @param epoch 审核轮次
     * @param newsId
     * @return
     */
    @GetMapping("/{newsId}")
    public Result<NewsPO> selectUnderReviewNews(@PathVariable int epoch, @PathVariable int newsId) {
        return Result.success(newsServiceForReviewer.selectUnderReviewNews(epoch, newsId));
    }

    /**
     * 保存修改 或 保存并审核通过 审核中的新闻
     * @param epoch 新闻所处的审核轮次
     * @param tag 1-保存修改；2-保存并通过审核；其他-非法请求
     * @param news
     * @return
     */
    @PostMapping("/{tag}")
    public Result saveOrSaveAndReviewSuccess(@PathVariable int epoch, @PathVariable int tag, @RequestBody NewsBeanForReviewerSave news) {
        newsServiceForReviewer.saveOrSaveAndReviewSuccess(epoch, tag, news);
        return Result.success(tag == 1 ? "保存成功！" : "成功保存并通过审核！");
    }

    /**
     * 对一个处于审核状态的新闻进行审核
     * @param epoch 新闻所处的审核轮次
     * @param newsId 新闻id
     * @param paramBean 封装审核结果参数
     * @return
     */
    @PutMapping("/{newsId}")
    public Result reviewOneNews(@PathVariable int epoch, @PathVariable int newsId, @RequestBody ReviewResultParamBean paramBean) {
        newsServiceForReviewer.reviewOneNews(epoch, newsId, paramBean);
        return Result.success( paramBean.getReviewSuccess() ? "新闻通过审核！" : "成功打回！" );
    }
}
