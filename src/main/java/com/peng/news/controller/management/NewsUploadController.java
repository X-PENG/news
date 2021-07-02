package com.peng.news.controller.management;

import com.peng.news.idempotent.IdempotentApi;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.Result;
import com.peng.news.model.paramBean.NewsBeanForInputterSave;
import com.peng.news.model.paramBean.QueryNewsBeanForInputter;
import com.peng.news.model.po.NewsPO;
import com.peng.news.service.NewsServiceForInputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 撰写新闻菜单相关接口，也就是传稿人用户能够访问的接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:38
 */
@RestController
@RequestMapping("/management/news/upload")
public class NewsUploadController {

    @Autowired
    NewsServiceForInputter newsServiceForInputter;

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }

    /**
     * 创建新闻，状态设为草稿或完成上传的状态
     * @param tag 1.草稿；2.完成上传
     * @return
     */
    @IdempotentApi
    @PostMapping("/create/{tag}")
    public Result createNewsAsDraftOrUpload(@PathVariable int tag, @RequestBody NewsBeanForInputterSave news){
        Integer newsId = newsServiceForInputter.createNewsAsDraftOrUpload(tag, news);
        Result<Object> result = Result.success("创建成功！");
        if(tag == 1) {
            //表示是创建草稿，则返回新的草稿的id
            result.setData(newsId);
        }
        return result;
    }

    /**
     * 保存新闻，状态设为草稿或完成上传的状态
     * @param tag 1.草稿；2.完成上传
     * @return
     */
    @PostMapping("/save/{tag}")
    public Result saveNewsAsDraftOrUpload(@PathVariable int tag, @RequestBody NewsBeanForInputterSave news){
        newsServiceForInputter.updateNewsAsDraftOrUpload(tag, news);
        Result<Object> result = Result.success("保存成功！");
        return result;
    }


    /**
     * 传稿人用户分页、条件查询草稿列表
     * @return
     */
    @GetMapping("/")
    public Result<CustomizedPage<NewsPO>> draftList(Integer page, Integer pageSize, QueryNewsBeanForInputter queryBean){
        return Result.success(newsServiceForInputter.draftList(page, pageSize, queryBean));
    }

    /**
     * 查询当前用户的 id为newsId的草稿
     * @param newsId
     * @return
     */
    @GetMapping("/{newsId}")
    public Result<NewsPO> selectCurUserDraft(@PathVariable int newsId){
        return Result.success(newsServiceForInputter.selectCurUserDraft(newsId));
    }

    /**
     * 删除当前用户的 id为newsId的草稿
     * @param newsId
     * @return
     */
    @DeleteMapping("/{newsId}")
    public Result<NewsPO> deleteCurUserDraft(@PathVariable Integer newsId){
        newsServiceForInputter.deleteCurUserDraft(newsId);
        return Result.success("成功删除！");
    }

    /**
     * 上传当前用户的 id为newsId的草稿
     * @param newsId
     * @return
     */
    @PutMapping("/{newsId}")
    public Result uploadCurUserDraft(@PathVariable Integer newsId){
        newsServiceForInputter.uploadCurUserDraft(newsId);
        return Result.success("上传成功！");
    }
}
