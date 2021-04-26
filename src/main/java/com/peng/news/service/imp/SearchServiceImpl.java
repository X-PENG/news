package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsPO;
import com.peng.news.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/26 20:11
 */
@Service
public class SearchServiceImpl implements SearchService {

    /**
     * 一页最多多少记录
     */
    static final int MAX_PAGE_SIZE = 10;

    @Autowired
    NewsMapper newsMapper;

    @Override
    public CustomizedPage<NewsPO> selectNewsListByCondition(Integer page, Integer pageSize, String condition) {
        if(condition == null) {
            return CustomizedPage.emptyPage(NewsPO.class);
        }

        //处理分页参数
        page = page == null || page < 0 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : ( pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize );

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("news_status", NewsStatus.PUBLISHED.getCode());
        //按照title模糊查询
        queryWrapper.like("title", condition.trim());
        queryWrapper.orderByDesc("show_pub_time");

        IPage<NewsPO> pageObj = new Page<>(page, pageSize);

        IPage<NewsPO> selectPage = newsMapper.selectPage(pageObj, queryWrapper);
        return CustomizedPage.fromIPage(selectPage);
    }
}
