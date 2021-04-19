package com.peng.news.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.NewsColumnMapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.dto.ReModificationInfoDTO;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.paramBean.*;
import com.peng.news.model.po.NewsColumnPO;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.NewsServiceForPublisher;
import com.peng.news.util.Constants;
import com.peng.news.util.DateTimeFormatUtils;
import com.peng.news.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/17 0:05
 */
@Service
public class NewsServiceForPublisherImpl implements NewsServiceForPublisher {

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    NewsColumnMapper newsColumnMapper;

    @Override
    public CustomizedPage<NewsVO> unpublishedNewsList(Integer page, Integer pageSize, QueryUnpublishedNewsBeanForPublisher queryBean) {
        //处理分页
        page = page == null || page <= 0 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        Integer paramNewsStatus = queryBean.getNewsStatus();
        //如果指定了状态，就按指定状态查询
        if(paramNewsStatus != null) {
            if(NewsStatus.TO_BE_RELEASED_STATUS_CODE_SET.contains(paramNewsStatus)) {
                queryWrapper.eq("news_status", paramNewsStatus);
            }else{
                //指定的状态不是待发布状态，报错
                throw new RuntimeException("异常的新闻状态！");
            }
        }else {
            //必要条件：必须是待发布的新闻
            queryWrapper.in("news_status", NewsStatus.TO_BE_RELEASED_STATUS_CODE_SET);
        }

        //条件查询
        queryBean.trimOrSetNull();
        queryWrapper.like(queryBean.getTitle() != null, "title", queryBean.getTitle());

        //排序
        Boolean orderByPreviousEpochReviewPassTime = queryBean.getOrderByPreviousEpochReviewPassTime();
        if(orderByPreviousEpochReviewPassTime == null || !orderByPreviousEpochReviewPassTime) {
            //默认是按 终审通过的时间 降序
            queryWrapper.orderByDesc("previous_epoch_review_pass_time");
        }else {
            queryWrapper.orderByAsc("previous_epoch_review_pass_time");
        }

        //构造分页对象
        IPage pageObj = new Page(page, pageSize);

        IPage<NewsVO> selectPage = newsMapper.selectUnpublishedNewsList(pageObj, queryWrapper);
        return CustomizedPage.fromIPage(selectPage);
    }

    @Override
    public boolean remodification(int newsId, String suggestion) {
        assertNewsExistsAndToBeReleased(newsId);

        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按id更新
        updateWrapper.eq("id", newsId);
        updateWrapper.set("news_status", NewsStatus.RE_MODIFICATION.getCode());
        //设置打回修改相关信息
        ReModificationInfoDTO info = new ReModificationInfoDTO();
        info.setOperateTime(DateTimeFormatUtils.format(LocalDateTime.now()));
        suggestion = suggestion == null || "".equals(suggestion.trim()) ? "无意见" : suggestion;
        info.setSuggestion(suggestion);
        UserPO operator = new UserPO();
        operator.setId(UserUtils.getUser().getId());
        info.setOperator(operator);

        Map map = new HashMap();
        map.put(Constants.RE_MODIFICATION_KEY, info);
        updateWrapper.set("extra", JSON.toJSONString(map));

        newsMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public boolean publishOneNews(Integer newsId, NewsBeanForPublisherPub pubInfo) {
        //首先，确保新闻存在，且处于待发布状态
        assertNewsExistsAndToBeReleased(newsId);

        //格式化并且校验发布设置信息
        pubInfo.formatAndValidate();

        Integer columnId = pubInfo.getColumnId();
        //确保设置的新闻栏目存在
        if(newsColumnMapper.selectCount(new QueryWrapper<NewsColumnPO>().eq("id", columnId)) == 0) {
            throw new RuntimeException("选择的新闻栏目不存在，操作失败！");
        }

        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新
        updateWrapper.eq("id", newsId);
        updateWrapper.set("column_id", columnId);
        updateWrapper.set("img_source", pubInfo.getImgSource());
        updateWrapper.set("article_source", pubInfo.getArticleSource());
        updateWrapper.set("editors", pubInfo.getEditors());
        updateWrapper.set("reviewers", pubInfo.getReviewers());
        updateWrapper.set("is_image_news", pubInfo.isImageNews());
        setTopStatus(updateWrapper, pubInfo.isTop());
        setCarouselStatus(updateWrapper, pubInfo.isCarousel(), pubInfo.getImgUrlForCarousel());
        setHeadlinesStatus(updateWrapper, pubInfo.isHeadlines());
        Timestamp now = new Timestamp(Instant.now().toEpochMilli());
        updateWrapper.set("show_pub_time", pubInfo.getShowPubTime() == null ? now : pubInfo.getShowPubTime());
        updateWrapper.set("init_reading_count", pubInfo.getInitReadingCount());
        //设置发布人为当前用户
        updateWrapper.set("publisher_id", UserUtils.getUser().getId());
        //设置实际发布时间
        updateWrapper.set("real_pub_time", now);
        //设置为已发布状态
        updateWrapper.set("news_status", NewsStatus.PUBLISHED.getCode());

        newsMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public CustomizedPage<NewsVO> publishedNewsList(Integer page, Integer pageSize, QueryPublishedNewsBeanForPublisher queryBean) {
        //处理分页
        page = page == null || page <= 0 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        //必要条件：必须是已发布新闻
        queryWrapper.eq("news_status", NewsStatus.PUBLISHED.getCode());

        queryBean.trimOrSetNull();
        //条件查询
        queryWrapper.like(queryBean.getTitle() != null, "title", queryBean.getTitle());
        queryWrapper.like(queryBean.getExternalUrl() != null, "external_url", queryBean.getExternalUrl());
        queryWrapper.eq(queryBean.getPublisherId() != null, "publisher_id", queryBean.getPublisherId());
        queryWrapper.eq(queryBean.getIsCarousel() != null, "is_carousel", queryBean.getIsCarousel());
        queryWrapper.eq(queryBean.getIsTop() != null, "is_top", queryBean.getIsTop());
        queryWrapper.eq(queryBean.getIsHeadlines() != null, "is_headlines", queryBean.getIsHeadlines());
        Integer columnId = queryBean.getColumnId();
        if(columnId != null) {
            //如果要按新闻栏目查询，首先看该栏目有没有子栏目
            List<Object> childIdList = newsColumnMapper.selectObjs(new QueryWrapper<NewsColumnPO>().select("id").eq("parent_id", columnId));
            if(childIdList.size() != 0) {
                //有子栏目，查询出当前栏目+所有子栏目的新闻
                childIdList.add(columnId);
                queryWrapper.in("column_id", childIdList);
            }else {
                queryWrapper.eq("column_id", columnId);
            }
        }

        //排序
        SingleColumnOrderRule orderRule = queryBean.getOrderRule();
        if(orderRule.isAsc()) {
            queryWrapper.orderByAsc(orderRule.getColumn());
        }else {
            queryWrapper.orderByDesc(orderRule.getColumn());
        }

        //构造分页对象
        IPage pageObj = new Page(page, pageSize);

        IPage<NewsVO> selectPage = newsMapper.selectPublishedNewsList(pageObj, queryWrapper);
        return CustomizedPage.fromIPage(selectPage);
    }

    @Override
    public boolean revokePub(int newsId) {
        //确保新闻存在，且是已发布的
        assertNewsExistsAndPublished(newsId);
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新
        updateWrapper.eq("id", newsId);
        updateWrapper.set("column_id", null);
        updateWrapper.set("news_status", NewsStatus.REVOKE_PUBLISHED.getCode());
        setTopStatus(updateWrapper, false);
        setHeadlinesStatus(updateWrapper, false);
        setCarouselStatus(updateWrapper, false, null);
        updateWrapper.set("is_image_news", false);
        updateWrapper.set("publisher_id", null);
        updateWrapper.set("real_pub_time", null);
        updateWrapper.set("show_pub_time", null);
        updateWrapper.set("init_reading_count", 0);
        //执行更新
        newsMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public NewsPO topManage(int newsId, int tag) {
        if(tag != 1 && tag != 2) {
            throw new RuntimeException("非法请求，请求参数出错！");
        }

        assertNewsExistsAndPublished(newsId);

        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新
        updateWrapper.eq("id", newsId);
        setTopStatus(updateWrapper, tag == 1);

        newsMapper.update(null, updateWrapper);

        //查询出设置新闻的时间并返回
        return newsMapper.selectOne(new QueryWrapper<NewsPO>().select("set_top_time").eq("id", newsId));
    }

    @Override
    public NewsPO headlinesManage(int newsId, int tag) {
        if(tag != 1 && tag != 2) {
            throw new RuntimeException("非法请求，请求参数出错！");
        }

        assertNewsExistsAndPublished(newsId);

        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新
        updateWrapper.eq("id", newsId);
        setHeadlinesStatus(updateWrapper, tag == 1);

        newsMapper.update(null, updateWrapper);

        //查询出设置头条的时间并返回
        return newsMapper.selectOne(new QueryWrapper<NewsPO>().select("set_headlines_time").eq("id", newsId));
    }

    @Override
    public NewsPO carouselManage(int newsId, CarouselParamBean paramBean) {
        //对参数进行格式化和校验
        paramBean.formatAndValidate();

        assertNewsExistsAndPublished(newsId);

        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新
        updateWrapper.eq("id", newsId);
        setCarouselStatus(updateWrapper, paramBean.getIsCarousel(), paramBean.getImgUrlForCarousel());

        newsMapper.update(null, updateWrapper);

        //查询出设置轮播的时间并返回
        return newsMapper.selectOne(new QueryWrapper<NewsPO>().select("set_carousel_time").eq("id", newsId));
    }


    /**
     * 确定新闻存在，并且属于 待发布 状态
     * @param newsId
     */
    private void assertNewsExistsAndToBeReleased(Integer newsId) {
        if(newsId == null){
            throw new RuntimeException("待发布站中不存在此新闻，操作失败！");
        }

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", newsId).in("news_status", NewsStatus.TO_BE_RELEASED_STATUS_CODE_SET);

        if(newsMapper.selectCount(queryWrapper) == 0) {
            throw new RuntimeException("待发布站中不存在此新闻，操作失败！");
        }
    }

    /**
     * 确定新闻存在，并且属于 已发布 状态
     * @param newsId
     */
    private void assertNewsExistsAndPublished(Integer newsId) {
        if(newsId == null){
            throw new RuntimeException("已发布站中不存在此新闻，操作失败！");
        }

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", newsId).eq("news_status", NewsStatus.PUBLISHED.getCode());

        if(newsMapper.selectCount(queryWrapper) == 0) {
            throw new RuntimeException("已发布站中不存在此新闻，操作失败！");
        }
    }


    /**
     * 设置新闻的置顶状态
     * @param updateWrapper
     * @param isTop
     */
    private void setTopStatus(UpdateWrapper<NewsPO> updateWrapper, boolean isTop) {
        updateWrapper.set("is_top", isTop);
        if(isTop) {
            //如果置顶，则更新 设置置顶的时机
            updateWrapper.set("set_top_time", new Timestamp(Instant.now().toEpochMilli()));
        }else {
            //不置顶，则设为null
            updateWrapper.set("set_top_time", null);
        }
    }

    /**
     * 设置新闻的头条状态
     * @param updateWrapper
     * @param isHeadlines 是否作为头条
     */
    private void setHeadlinesStatus(UpdateWrapper<NewsPO> updateWrapper, boolean isHeadlines) {
        updateWrapper.set("is_headlines", isHeadlines);
        if(isHeadlines) {
            //如果设为头条，则更新 设置头条的时机
            updateWrapper.set("set_headlines_time", new Timestamp(Instant.now().toEpochMilli()));
        }else {
            updateWrapper.set("set_headlines_time", null);
        }
    }

    /**
     * 设置新闻的轮播状态
     * @param updateWrapper
     * @param isCarousel 是否轮播
     * @param imgUrlForCarousel 用作轮播的图片地址
     */
    private void setCarouselStatus(UpdateWrapper<NewsPO> updateWrapper, boolean isCarousel, String imgUrlForCarousel) {
        updateWrapper.set("is_carousel", isCarousel);
        if(isCarousel) {
            //如果将新闻设为轮播，则更新 设置轮播的时机
            updateWrapper.set("set_carousel_time", new Timestamp(Instant.now().toEpochMilli()));

            //如果设置了轮播，需要保存轮播图片的地址到extra字段
            Map map = new HashMap();
            map.put(Constants.CAROUSEL_IMAGE_URL_KEY, imgUrlForCarousel);
            updateWrapper.set("extra", JSON.toJSONString(map));
        }else {
            updateWrapper.set("set_carousel_time", null);
            updateWrapper.set("extra", null);
        }
    }
}
