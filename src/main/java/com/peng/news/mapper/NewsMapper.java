package com.peng.news.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 21:04
 */
public interface NewsMapper extends BaseMapper<NewsPO> {

    /**
     * 自定义使用mp的分页查询以及条件构造器Wrapper，分页、条件查询中转新闻
     * @param page
     * @param wrapper
     * @return
     */
    IPage<NewsVO> selectTransitNewsPage(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);


    /**
     * 自定义使用mp的分页查询以及条件构造器Wrapper，分页、条件查询审核中的新闻
     * @param page
     * @param wrapper
     * @return
     */
    IPage<NewsVO> selectUnderReviewNewsPage(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 自定义使用mp的分页查询以及条件构造器Wrapper，分页、条件查询 待发布 的新闻
     * @param page
     * @param wrapper
     * @return
     */
    IPage<NewsVO> selectUnpublishedNewsList(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 自定义使用mp的分页查询以及条件构造器Wrapper，分页、条件查询 已发布 的新闻
     * @param page
     * @param wrapper
     * @return
     */
    IPage<NewsVO> selectPublishedNewsList(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);


    /**
     * 查询新闻，且携带栏目信息。使用Wrapper自定义sql查询
     * @param wrapper
     * @return
     */
    NewsVO selectNewsWithColumn(@Param(Constants.WRAPPER) Wrapper wrapper);
}
