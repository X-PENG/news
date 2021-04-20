package com.peng.news.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.mapper.UserMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.dto.ReModificationInfoDTO;
import com.peng.news.model.dto.ReviewFailInfoDTO;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.paramBean.NewsBeanForEditorSave;
import com.peng.news.model.paramBean.QueryNewsBeanForEditor;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.model.vo.UserVO;
import com.peng.news.service.NewsServiceForEditor;
import com.peng.news.service.SystemConfigService;
import com.peng.news.util.Constants;
import com.peng.news.util.JSONObjectConvertJavaBeanUtils;
import com.peng.news.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/14 15:19
 */
@Service
public class NewsServiceForEditorImpl implements NewsServiceForEditor {

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SystemConfigService systemConfigService;

    @Override
    public CustomizedPage<NewsVO> transitNewsList(Integer page, Integer pageSize, QueryNewsBeanForEditor queryBean) {
        page = page == null || page <= 0 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;

        QueryWrapper<NewsPO> wrapper = new QueryWrapper<>();
        Integer newsStatusParam = queryBean.getNewsStatus();
        //若指定了状态
        if(newsStatusParam != null) {
            //不是中转状态，则报错
            if (!NewsStatus.TRANSIT_STATUS_CODE_SET.contains(newsStatusParam)) {
                throw new RuntimeException("异常的新闻状态！");
            }else {
                wrapper.eq("news_status", newsStatusParam);
            }
        }else{
            //查询所有中转状态的新闻
            wrapper.in("news_status", NewsStatus.TRANSIT_STATUS_CODE_SET);
        }
        //条件查询
        queryBean.trimOrSetNull();
        wrapper.like(queryBean.getTitle() != null, "title", queryBean.getTitle());
        wrapper.like(queryBean.getExternalUrl() != null, "external_url", queryBean.getExternalUrl());
        wrapper.eq(queryBean.getInputterId() != null, "inputter_id", queryBean.getInputterId());
        wrapper.eq(queryBean.getLatestEditorId() != null, "latest_editor_id", queryBean.getLatestEditorId());
        wrapper.like(queryBean.getParticipateEditor() != null, "editors", queryBean.getParticipateEditor());
        //排序
        Boolean orderByLatestEditTime = queryBean.getOrderByLatestEditTime();
        if(orderByLatestEditTime == null) {
            //默认排序：按完成录入的时间降序
            wrapper.orderByDesc("complete_input_time");
        }else if(orderByLatestEditTime) {
            wrapper.orderByAsc("latest_edit_time");
        }else {
            wrapper.orderByDesc("latest_edit_time");
        }
        //构造分页对象
        IPage pageObj = new Page<>(page, pageSize);
        //分页、条件查询
        IPage<NewsVO> selectPage = newsMapper.selectTransitNewsPage(pageObj, wrapper);
        return CustomizedPage.fromIPage(selectPage);
    }

    @Override
    public NewsPO selectTransitNews(int newsId) {
        assertNewsExistsAndIsTransit(newsId, "中转站中没有此新闻，加载失败！");
        return newsMapper.selectById(newsId);
    }

    @Override
    public boolean deleteTransitNews(int newsId) {
        assertNewsExistsAndIsTransit(newsId, "中转站中没有此新闻，操作失败！");
        newsMapper.deleteById(newsId);
        return true;
    }

    @Override
    public boolean submitTransitNewsToReview(int newsId) {
        //确保新闻存在且处于中转状态
        assertNewsExistsAndIsTransit(newsId, "中转站中没有此新闻，操作失败！");
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //根据id修改
        updateWrapper.eq("id", newsId);
        wrapSubmitReviewInfo(updateWrapper);

        //执行更新
        newsMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public boolean saveOrSaveAndSubmitReview(int tag, NewsBeanForEditorSave news) {
        if(tag != 1 && tag != 2) {
            throw new RuntimeException("非法请求！");
        }

        //首先，确保存在这个中转新闻
        assertNewsExistsAndIsTransit(news.getId(), "中转站中不存在此新闻，操作失败！");

        //格式化并校验新闻信息
        news.formatAndValidate();

        if(tag == 1) {
            save(news);
        }else {
            saveAndSubmitReview(news);
        }

        return true;
    }

    @Override
    public ReviewFailInfoDTO queryReviewFailInfo(int newsId) {
        assertNewsExistsAndIsTransit(newsId, "中转站中不存在此新闻，查询失败！");
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("news_status", "extra").eq("id", newsId);
        NewsPO newsPO = newsMapper.selectOne(queryWrapper);
        Integer newsStatus = newsPO.getNewsStatus();
        if(newsStatus != NewsStatus.REVIEW_FAIL.getCode()) {
            throw new RuntimeException("新闻状态异常，当前是【" + NewsStatus.fromCode(newsStatus).getName() + "】状态");
        }

        JSONObject jsonObject = (JSONObject) JSON.parseObject(newsPO.getExtra(), Map.class).get(Constants.REVIEW_FAIL_KEY);
        ReviewFailInfoDTO convert = JSONObjectConvertJavaBeanUtils.convert(jsonObject, ReviewFailInfoDTO.class);
        if(convert == null) {
            throw new RuntimeException("不能得到审核失败相关信息，系统内部错误！");
        }else {
            Integer reviewerId = convert.getReviewer().getId();
            convert.setReviewer(userMapper.selectNameById(reviewerId));
        }

        return convert;
    }

    @Override
    public ReModificationInfoDTO queryReModificationInfo(int newsId) {
        assertNewsExistsAndIsTransit(newsId, "中转站中不存在此新闻，查询失败！");
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("news_status", "extra").eq("id", newsId);
        NewsPO newsPO = newsMapper.selectOne(queryWrapper);
        Integer newsStatus = newsPO.getNewsStatus();
        if(newsStatus != NewsStatus.RE_MODIFICATION.getCode()) {
            throw new RuntimeException("新闻状态异常，当前是【" + NewsStatus.fromCode(newsStatus).getName() + "】状态");
        }

        JSONObject jsonObject = (JSONObject) JSON.parseObject(newsPO.getExtra(), Map.class).get(Constants.RE_MODIFICATION_KEY);
        ReModificationInfoDTO convert = JSONObjectConvertJavaBeanUtils.convert(jsonObject, ReModificationInfoDTO.class);
        if(convert == null) {
            throw new RuntimeException("不能得到打回修改相关信息，系统内部错误！");
        }else {
            Integer operatorId = convert.getOperator().getId();
            convert.setOperator(userMapper.selectNameById(operatorId));
        }

        return convert;
    }


    /**
     * 给UpdateWrapper包装提交审核的相关信息
     * 提交审核时，新闻状态可能会变成：审核中 或 待发布状态（如果关闭系统审核的话，即审核等级为0）
     * @param updateWrapper
     */
    private void wrapSubmitReviewInfo(UpdateWrapper<NewsPO> updateWrapper) {
        //送审人设为当前用户
        updateWrapper.set("submitter_id", UserUtils.getUser().getId());
        //设置送审时间
        updateWrapper.set("submit_time", new Timestamp(Instant.now().toEpochMilli()));
        //将新闻当前所处的审核轮次设为1，表示待一审！
        updateWrapper.set("current_review_epoch", 1);
        //将reviewers设为null，表示还没有人参与审核
        updateWrapper.set("reviewers", null);
        //extra设为null，清空脏数据
        updateWrapper.set("extra", null);

        //查询系统审核等级
        if(systemConfigService.getCurReviewLevel() == 0) {
            //如果没有开启审核，则新闻变成待发布状态
            updateWrapper.set("news_status", NewsStatus.TO_BE_RELEASED.getCode());
            //新闻变成待发布，说明审核通过了，则将previous_epoch_review_pass_time字段设为当前时间
            updateWrapper.set("previous_epoch_review_pass_time", new Timestamp(Instant.now().toEpochMilli()));
        }else {
            //状态改为 审核中
            updateWrapper.set("news_status", NewsStatus.UNDER_REVIEW.getCode());
            //清空脏数据
            updateWrapper.set("previous_epoch_review_pass_time", null);
        }
    }


    private void save(NewsBeanForEditorSave news) {
        UpdateWrapper<NewsPO> updateWrapper = commonUpdateWrapper(news);
        newsMapper.update(null, updateWrapper);
    }

    private void saveAndSubmitReview(NewsBeanForEditorSave news) {
        UpdateWrapper<NewsPO> updateWrapper = commonUpdateWrapper(news);
        //还要包装提交审核的相关信息
        wrapSubmitReviewInfo(updateWrapper);
        newsMapper.update(null, updateWrapper);
    }

    private UpdateWrapper<NewsPO> commonUpdateWrapper(NewsBeanForEditorSave news){
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<>();
        //按照id更新
        Integer newsId = news.getId();
        updateWrapper.eq("id", newsId);
        //都会更新的内容
        updateWrapper.set("title", news.getTitle());
        updateWrapper.set("article_fragment_for_show", news.getArticleFragmentForShow());
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
     * 确保新闻存在，且处于中转状态
     * @param newsId
     * @return
     */
    private void assertNewsExistsAndIsTransit(Integer newsId, String exceptionMsg) {
        QueryWrapper<NewsPO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", newsId).in("news_status", NewsStatus.TRANSIT_STATUS_CODE_SET);

        if(newsMapper.selectCount(wrapper) == 0) {
            throw new RuntimeException(exceptionMsg);
        }
    }
}
