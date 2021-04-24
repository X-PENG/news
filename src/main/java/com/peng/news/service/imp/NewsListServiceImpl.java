package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.NewsColumnMapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.dto.NewsListDTO;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.NewsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 19:47
 */
@Service
public class NewsListServiceImpl implements NewsListService {

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    NewsColumnMapper newsColumnMapper;

    @Override
    public NewsListDTO newsListByColId(int colId, Integer page, Integer pageSize) {
        NewsColumnVO columnVO = newsColumnMapper.selectEnabledColWithParentAndSettings(colId);
        if(columnVO == null) {
            //栏目不存在或未开启
            throw new RuntimeException("新闻栏目不存在！");
        }

        //处理分页参数
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        //必须是已经发布的新闻
        queryWrapper.eq("news_status", NewsStatus.PUBLISHED.getCode());
        //必须是对应栏目
        queryWrapper.eq("column_id", colId);
        queryWrapper.select("id", "title", "article_fragment_for_show", "show_pub_time", "img_for_show_on_news_list");
        queryWrapper.orderByDesc("set_top_time").orderByDesc("show_pub_time");

        //构造分页对象
        IPage<NewsPO> pageObj = new Page<>(page, pageSize);
        IPage<NewsPO> selectPage = newsMapper.selectPage(pageObj, queryWrapper);
        CustomizedPage<NewsPO> customizedPage = CustomizedPage.fromIPage(selectPage);

        NewsListDTO newsListDTO = new NewsListDTO();
        newsListDTO.setColumn(columnVO);
        newsListDTO.setNews(customizedPage);

        return newsListDTO;
    }
}
