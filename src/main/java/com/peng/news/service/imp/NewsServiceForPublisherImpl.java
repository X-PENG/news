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
import com.peng.news.model.paramBean.NewsBeanForPublisherPub;
import com.peng.news.model.paramBean.QueryUnpublishedNewsBeanForPublisher;
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
        updateWrapper.set("news_status", NewsStatus.RE_MODIFICATION);
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
        updateWrapper.set("is_top", pubInfo.isTop());
        Timestamp now = new Timestamp(Instant.now().toEpochMilli());
        if(pubInfo.isTop()) {
            //如果新闻设置了置顶，就设置置顶时间
            updateWrapper.set("set_top_time", now);
        }
        updateWrapper.set("show_pub_time", pubInfo.getShowPubTime() == null ? now : pubInfo.getShowPubTime());
        updateWrapper.set("init_reading_count", pubInfo.getInitReadingCount());
        //设置发布人为当前用户
        updateWrapper.set("publisher_id", UserUtils.getUser().getId());
        //设置实际发布时间
        updateWrapper.set("real_pub_time", now);
        //设置发布状态
        int newsStatusCode = pubInfo.getPubStatus().getCode();
        updateWrapper.set("news_status", newsStatusCode);
        if(newsStatusCode == NewsStatus.PUBLISHED_AS_CAROUSEL.getCode() || newsStatusCode == NewsStatus.PUBLISHED_AS_CAROUSEL_AND_HEADLINES.getCode()) {
            //如果设置了轮播发布，需要保存轮播图片的地址
            Map map = new HashMap();
            map.put(Constants.CAROUSEL_IMAGE_URL_KEY, pubInfo.getImgUrlForCarousel());
            updateWrapper.set("extra", JSON.toJSONString(map));
        }

        newsMapper.update(null, updateWrapper);
        return true;
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
}
