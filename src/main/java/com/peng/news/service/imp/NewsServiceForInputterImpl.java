package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.paramBean.NewsBeanForInputterSave;
import com.peng.news.model.paramBean.QueryNewsBeanForInputter;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.UserVO;
import com.peng.news.util.Constants;
import com.peng.news.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 21:07
 */
@Service
public class NewsServiceForInputterImpl extends AbstractNewsServiceForInputter {

    @Autowired
    NewsMapper newsMapper;

    @Override
    protected NewsPO assertNewsExists(Integer newsId) {
        if(newsId == null){
            throw new RuntimeException("新闻不存在，操作失败！");
        }

        NewsPO newsPO = newsMapper.selectById(newsId);
        if(newsPO == null){
            throw new RuntimeException("新闻不存在，操作失败！");
        }

        return newsPO;
    }

    @Override
    protected Integer createNewsAsDraft(NewsBeanForInputterSave news) {
        NewsPO newsPO = new NewsPO();
        BeanUtils.copyProperties(news, newsPO);
        newsPO.setNewsStatus(NewsStatus.DRAFT.getCode());
        Integer curUserId = UserUtils.getUser().getId();
        newsPO.setInputterId(curUserId);
        newsPO.setLatestEditTime(new Timestamp(Instant.now().toEpochMilli()));
        newsMapper.insert(newsPO);
        return newsPO.getId();
    }

    @Override
    protected Integer createNewsAsCompleted(NewsBeanForInputterSave news) {
        UserVO curUser = UserUtils.getUser();
        NewsPO newsPO = new NewsPO();
        BeanUtils.copyProperties(news, newsPO);
        newsPO.setNewsStatus(NewsStatus.UPLOAD_SUCCESS.getCode());
        newsPO.setInputterId(curUser.getId());
        newsPO.setCompleteInputTime(new Timestamp(Instant.now().toEpochMilli()));
        //上传新闻时，要自动将当前用户的姓名填进去
        newsPO.setEditors(curUser.getRealName() + Constants.EDITORS_SEPARATOR);
        newsMapper.insert(newsPO);
        return newsPO.getId();
    }

    @Override
    protected Integer saveAsDraft(NewsBeanForInputterSave news) {
        UpdateWrapper<NewsPO> updateWrapper = commonUpdateWrapperForSave(news);
        NewsPO newsPO = new NewsPO();
        newsPO.setLatestEditTime(new Timestamp(Instant.now().toEpochMilli()));
        newsMapper.update(newsPO, updateWrapper);
        return news.getId();
    }

    @Override
    protected Integer saveAsCompleted(NewsBeanForInputterSave news) {
        UpdateWrapper<NewsPO> updateWrapper = commonUpdateWrapperForSave(news);
        //上传新闻时，要将最近修改时间设为null
        updateWrapper.set("latest_edit_time", null);
        NewsPO newsPO = new NewsPO();
        newsPO.setNewsStatus(NewsStatus.UPLOAD_SUCCESS.getCode());
        newsPO.setCompleteInputTime(new Timestamp(Instant.now().toEpochMilli()));
        UserVO curUser = UserUtils.getUser();
        //上传新闻时，要自动将当前用户的姓名填进去
        newsPO.setEditors(curUser.getRealName() + Constants.EDITORS_SEPARATOR);
        newsMapper.update(newsPO, updateWrapper);
        return news.getId();
    }

    /**
     * 返回保存新闻时公共的UpdateWrapper
     * @param news
     * @return
     */
    UpdateWrapper<NewsPO> commonUpdateWrapperForSave(NewsBeanForInputterSave news){
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", news.getId());
        updateWrapper.set("title", news.getTitle());
        updateWrapper.set("content", news.getContent());
        updateWrapper.set("img_source", news.getImgSource());
        updateWrapper.set("article_source", news.getArticleSource());
        updateWrapper.set("external_url", news.getExternalUrl());
        return updateWrapper;
    }

    @Override
    public CustomizedPage<NewsPO> draftList(Integer page, Integer pageSize, QueryNewsBeanForInputter queryBean) {
        QueryWrapper<NewsPO> wrapper = new QueryWrapper<>();
        //当前用户自己的草稿
        wrapper.eq("inputter_id", UserUtils.getUser().getId());
        wrapper.eq("news_status", NewsStatus.DRAFT.getCode());
        //分页
        page = page == null || page <= 0 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;
        IPage<NewsPO> pageObj = new Page<>(page, pageSize);
        //条件查询
        queryBean = queryBean.trimAndFormat();
        wrapper.like(queryBean.getTitle() != null, "title", queryBean.getTitle());
        wrapper.like(queryBean.getImgSource() != null, "img_source", queryBean.getImgSource());
        wrapper.like(queryBean.getArticleSource() != null, "article_source", queryBean.getArticleSource());
        wrapper.like(queryBean.getExternalUrl() != null, "external_url", queryBean.getExternalUrl());
        //排序
        Boolean orderByCreateTime = queryBean.getOrderByCreateTime();
        if(orderByCreateTime == null){
            //默认，按最近修改时间降序
            wrapper.orderByDesc("latest_edit_time");
        }else if (orderByCreateTime){
            wrapper.orderByAsc("create_time");
        }else {
            wrapper.orderByDesc("create_time");
        }
        //查询字段
        wrapper.select("id", "title", "img_source", "article_source", "external_url", "create_time", "latest_edit_time");

        //执行查询
        IPage<NewsPO> selectPage = newsMapper.selectPage(pageObj, wrapper);
        return CustomizedPage.fromIPage(selectPage);
    }

    @Override
    public NewsPO selectCurUserDraft(int newsId) {
        Integer curUserId = UserUtils.getUser().getId();
        QueryWrapper<NewsPO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", newsId).eq("inputter_id", curUserId).eq("news_status", NewsStatus.DRAFT.getCode());
        return newsMapper.selectOne(wrapper);
    }

    @Override
    public boolean deleteCurUserDraft(Integer newsId) {
        assertNewsExistsAndBelongCurUserDraft(newsId);

        newsMapper.deleteById(newsId);
        return true;
    }

    @Override
    public boolean uploadCurUserDraft(Integer newsId) {
        assertNewsExistsAndBelongCurUserDraft(newsId);

        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新
        updateWrapper.eq("id", newsId);
        updateWrapper.set("news_status", NewsStatus.UPLOAD_SUCCESS.getCode());
        updateWrapper.set("complete_input_time", new Timestamp(Instant.now().toEpochMilli()));
        //上传时，将最近修改时间设为null，表示最近没有编辑修改这个新闻
        updateWrapper.set("latest_edit_time", null);
        //插入自己的姓名
        updateWrapper.set("editors", UserUtils.getUser().getRealName() + ",");

        newsMapper.update(null, updateWrapper);
        return true;
    }
}
