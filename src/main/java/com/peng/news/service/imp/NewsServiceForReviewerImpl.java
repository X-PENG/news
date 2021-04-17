package com.peng.news.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.dto.ReviewFailInfoDTO;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.paramBean.NewsBeanForReviewerSave;
import com.peng.news.model.paramBean.QueryNewsBeanForReviewer;
import com.peng.news.model.paramBean.ReviewResultParamBean;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.po.UserPO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.model.vo.UserVO;
import com.peng.news.service.NewsServiceForReviewer;
import com.peng.news.service.SystemConfigService;
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
 * @date 2021/4/15 20:49
 */
@Service
public class NewsServiceForReviewerImpl implements NewsServiceForReviewer {

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    SystemConfigService systemConfigService;

    @Override
    public CustomizedPage<NewsVO> underReviewNewsList(int epoch, Integer page, Integer pageSize, QueryNewsBeanForReviewer queryBean) {
        //处理分页
        page = page == null || page <= 0 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        //必须是审核中的新闻，且审核轮次为epoch
        queryWrapper.eq("news_status", NewsStatus.UNDER_REVIEW.getCode()).eq("current_review_epoch", epoch);

        //条件查询
        queryBean.trimOrSetNull();
        queryWrapper.like(queryBean.getTitle() != null, "title", queryBean.getTitle());
        queryWrapper.like(queryBean.getExternalUrl() != null, "external_url", queryBean.getExternalUrl());
        queryWrapper.eq(queryBean.getSubmitterId() != null, "submitter_id", queryBean.getSubmitterId());

        //构造排序
        QueryNewsBeanForReviewer.SingleColumnOrderRule orderRule = queryBean.getOrderRule(epoch);
        if(orderRule.isAsc()) {
            queryWrapper.orderByAsc(orderRule.getColumn());
        }else {
            queryWrapper.orderByDesc(orderRule.getColumn());
        }

        //构造分页对象
        IPage pageObj = new Page(page, pageSize);
        //进行分页、条件查询
        IPage<NewsVO> selectPage = newsMapper.selectUnderReviewNewsPage(pageObj, queryWrapper);
        return CustomizedPage.fromIPage(selectPage);
    }

    @Override
    public NewsPO selectUnderReviewNews(int epoch, int newsId) {
        assertNewsExistsAndUnderReview(epoch, newsId, epoch + "审站中不存在此新闻，加载失败！");
        return newsMapper.selectById(newsId);
    }

    @Override
    public boolean saveOrSaveAndReviewSuccess(int epoch, int tag, NewsBeanForReviewerSave news) {
        if(tag != 1 && tag != 2) {
            throw new RuntimeException("非法请求！");
        }

        //确保新闻存在，且处于审核中状态，且审核轮次为epoch
        assertNewsExistsAndUnderReview(epoch, news.getId(), null);

        //格式化并校验信息
        news.formatAndValidate();

        if(tag == 1) {
            save(news);
        }else {
            saveAndReviewSuccess(news);
        }

        return true;
    }

    @Override
    public boolean reviewOneNews(int epoch, int newsId, ReviewResultParamBean paramBean) {
        Boolean reviewSuccess = paramBean.getReviewSuccess();
        if(reviewSuccess == null) {
            throw new RuntimeException("请求出错，请正确传递参数！");
        }

        assertNewsExistsAndUnderReview(epoch, newsId, null);

        if(reviewSuccess) {
            //审核成功
            reviewPass(newsId);
        }else {
            //审核失败
            reviewNotPass(newsId, epoch, paramBean.getSuggestion());
        }

        return true;
    }

    private void reviewPass(int newsId) {
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新新闻
        updateWrapper.eq("id", newsId);
        wrapReviewSuccessInfo(newsId, updateWrapper);
        newsMapper.update(null, updateWrapper);
    }

    private void reviewNotPass(int newsId, int epoch, String suggestion) {
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按id更新
        updateWrapper.eq("id", newsId);
        updateWrapper.set("news_status", NewsStatus.REVIEW_FAIL.getCode());
        updateWrapper.set("reviewers", null);
        ReviewFailInfoDTO reviewFailInfoDTO = new ReviewFailInfoDTO();
        suggestion = suggestion == null || "".equals(suggestion.trim()) ? "无意见" : suggestion;
        reviewFailInfoDTO.setSuggestion(suggestion);
        reviewFailInfoDTO.setReviewTime(DateTimeFormatUtils.format(LocalDateTime.now()));
        //在哪一轮失败
        reviewFailInfoDTO.setEpoch(epoch);
        //审核人
        UserPO reviewer = new UserPO();
        reviewer.setId(UserUtils.getUser().getId());
        reviewFailInfoDTO.setReviewer(reviewer);

        Map map = new HashMap();
        map.put(Constants.REVIEW_FAIL_KEY, reviewFailInfoDTO);
        //保存审核失败相关信息
        updateWrapper.set("extra", JSON.toJSONString(map));

        newsMapper.update(null, updateWrapper);
    }

    private void save(NewsBeanForReviewerSave news) {
        UpdateWrapper<NewsPO> updateWrapper = commonUpdateWrapper(news);
        newsMapper.update(null, updateWrapper);
    }

    private void saveAndReviewSuccess(NewsBeanForReviewerSave news) {
        UpdateWrapper<NewsPO> updateWrapper = commonUpdateWrapper(news);
        //包装审核成功时相关信息
        wrapReviewSuccessInfo(news.getId(), updateWrapper);
        newsMapper.update(null, updateWrapper);
    }

    private UpdateWrapper<NewsPO> commonUpdateWrapper(NewsBeanForReviewerSave news){
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新
        Integer newsId = news.getId();
        updateWrapper.eq("id", newsId);
        //都会更新的内容
        updateWrapper.set("title", news.getTitle());
        updateWrapper.set("content", news.getContent());
        updateWrapper.set("img_source", news.getImgSource());
        updateWrapper.set("article_source", news.getArticleSource());
        updateWrapper.set("external_url", news.getExternalUrl());
        //判断是否要将当前用户插入到参与编辑人员中。若已经包含，则不插入
        UserVO curUser = UserUtils.getUser();
        NewsPO selectNews = newsMapper.selectOne(new QueryWrapper<NewsPO>().select("editors").eq("id", newsId));
        String selectNewsEditors = selectNews.getEditors();
        if(selectNewsEditors == null || !selectNewsEditors.contains(curUser.getRealName())) {
            //如果不包含，就插入当前用户
            //首先处理selectNewsEditors
            if(selectNewsEditors == null) {
                selectNewsEditors = "";
            }else if(!selectNewsEditors.endsWith(Constants.EDITORS_SEPARATOR)) {
                //不是以分隔符结尾，先加上分隔符
                selectNewsEditors = selectNewsEditors + Constants.EDITORS_SEPARATOR;
            }
            updateWrapper.set("editors", selectNewsEditors + curUser.getRealName() + Constants.EDITORS_SEPARATOR);
        }
        //更新最近修改的用户为自己
        updateWrapper.set("latest_editor_id", curUser.getId());
        //更新最近修改时间
        updateWrapper.set("latest_edit_time", new Timestamp(Instant.now().toEpochMilli()));
        return updateWrapper;
    }

    /**
     * 当审核成功时，构造UpdateWrapper
     * 审核成功时，新闻状态可能会变成：审核中 或 待发布状态（如果当前审核轮次到达系统设置的上限）
     * @param newsId
     * @param updateWrapper
     */
    private void wrapReviewSuccessInfo(int newsId, UpdateWrapper<NewsPO> updateWrapper) {
        UserVO curUser = UserUtils.getUser();
        //填充参与审核的人
        NewsPO selectNews = newsMapper.selectOne(new QueryWrapper<NewsPO>().select("current_review_epoch", "reviewers").eq("id", newsId));
        String reviewers = selectNews.getReviewers();
        if(reviewers == null || !reviewers.contains(curUser.getRealName())) {
            updateWrapper.set("reviewers", (reviewers == null ? "" : reviewers) + curUser.getRealName() + Constants.REVIEWERS_SEPARATOR);
        }

        //判断当前审核轮次是否达到了系统设置的审核上限
        int systemMaxReviewLevel = systemConfigService.getCurReviewLevel();
        int currentReviewEpoch = selectNews.getCurrentReviewEpoch();
        if(currentReviewEpoch == systemMaxReviewLevel) {
            //设置为待发布状态
            updateWrapper.set("news_status", NewsStatus.TO_BE_RELEASED.getCode());
        }else if(currentReviewEpoch < systemMaxReviewLevel){
            //还是审核中状态，审核轮次+1
            updateWrapper.set("current_review_epoch", currentReviewEpoch + 1);
        }else {
            throw new RuntimeException("审核轮次异常，大于系统设置的审核等级上限");
        }

        //本次审核通过，则设置通过审核的时间
        updateWrapper.set("previous_epoch_review_pass_time", new Timestamp(Instant.now().toEpochMilli()));

    }

    /**
     * 确保新闻存在，且处于审核中状态，并且审核轮次为epoch
     * @param epoch 新闻所处的审核轮次
     * @param newsId
     * @param exceptionMsg 报错的消息
     */
    private void assertNewsExistsAndUnderReview(int epoch, int newsId, String exceptionMsg) {
        exceptionMsg = exceptionMsg == null ? epoch + "审站中不存在此新闻，操作失败！" : exceptionMsg;

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", newsId).eq("news_status", NewsStatus.UNDER_REVIEW.getCode()).eq("current_review_epoch", epoch);

        if(newsMapper.selectCount(queryWrapper) == 0) {
            throw new RuntimeException(exceptionMsg);
        }
    }

}
