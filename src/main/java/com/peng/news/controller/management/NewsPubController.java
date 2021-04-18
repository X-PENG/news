package com.peng.news.controller.management;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.Result;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.paramBean.NewsBeanForPublisherPub;
import com.peng.news.model.paramBean.QueryUnpublishedNewsBeanForPublisher;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.NewsColumnService;
import com.peng.news.service.NewsServiceForPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新闻发布相关接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:44
 */
@RestController
@RequestMapping("/management/news/pub")
public class NewsPubController {

    @Autowired
    NewsServiceForPublisher newsServiceForPublisher;

    @Autowired
    NewsColumnService newsColumnService;

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello[Get]";
    }


    /**
     * 分页、条件查询 待发布状态的 新闻列表
     * @param page
     * @param pageSize
     * @param queryBean
     * @return
     */
    @GetMapping("/unpublished")
    public Result<CustomizedPage<NewsVO>> unpublishedNewsList(Integer page, Integer pageSize, QueryUnpublishedNewsBeanForPublisher queryBean){
        return Result.success(newsServiceForPublisher.unpublishedNewsList(page, pageSize, queryBean));
    }

    /**
     * 查询所有待发布状态列表
     * @return
     */
    @GetMapping("/toBeReleasedStatusList")
    public Result<NewsStatus[]> toBeReleasedStatusList() {
        return Result.success(NewsStatus.TO_BE_RELEASED_STATUS_ARRAY);
    }

    /**
     * 查询新闻栏目列表，带有子栏目；作为新闻栏目级联选择器的数据
     * @return
     */
    @GetMapping("/allNewsColumn")
    public Result<List<NewsColumnVO>> allNewsColumn() {
        return Result.success(newsColumnService.newsColumnSelectData());
    }

    /**
     * 将 待发布状态的 新闻打回修改。新闻被打回修改，就会回到中转站。
     * @param newsId
     * @param suggestion 打回意见
     * @return
     */
    @PutMapping("/remodification/{newsId}")
    public Result remodification(@PathVariable int newsId, String suggestion) {
        newsServiceForPublisher.remodification(newsId, suggestion);
        return Result.success("成功打回！");
    }

    /**
     * 发布一个新闻
     * @param newsId
     * @param pubInfo 发布设置相关信息
     * @return
     */
    @PutMapping("/{newsId}")
    public Result publishOneNews(@PathVariable int newsId,@RequestBody NewsBeanForPublisherPub pubInfo) {
        newsServiceForPublisher.publishOneNews(newsId, pubInfo);
        return Result.success("发布成功！");
    }


}
