package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.news.mapper.NewsColumnMapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.dto.NewsListDTO;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsColumnPO;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.NewsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        NewsColumnVO columnVO = newsColumnMapper.selectEnabledColWithParentAndSettingsById(colId);
        if(columnVO == null) {
            //栏目不存在或未开启
            throw new RuntimeException("新闻栏目不存在！");
        }

        //查询所有开启的子栏目id
        List<Integer> subColIdList = newsColumnMapper.selectEnabledSubColByParentId(colId);

        //修正信息
        if(subColIdList.size() > 0 && !columnVO.getIsHasChildren()) {
            //有子栏目，但是查出来的栏目信息显示没有子栏目，就修正栏目信息
            columnVO.setIsHasChildren(true);
            newsColumnMapper.update(null, new UpdateWrapper<NewsColumnPO>().set("is_has_children", true).eq("id", colId));
        }else if(subColIdList.size() == 0 && columnVO.getIsHasChildren()) {
            //没有子栏目，但是查出来的栏目信息显示有子栏目，修正信息
            columnVO.setIsHasChildren(false);
            newsColumnMapper.update(null, new UpdateWrapper<NewsColumnPO>().set("is_has_children", false).eq("id", colId));
        }

        //处理分页参数
        page = page == null || page < 1 ? 1 : page;
        pageSize = pageSize == null || pageSize < 0 ? 0 : pageSize;

        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        //必须是已经发布的新闻
        queryWrapper.eq("news_status", NewsStatus.PUBLISHED.getCode());
        //必须是对应栏目
        if(!columnVO.getIsHasChildren()) {
            queryWrapper.eq("column_id", colId);
        }else {
            //有子栏目
            subColIdList.add(colId);
            queryWrapper.in("column_id", subColIdList);
        }
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

    @Override
    public List<NewsColumnVO> subColList(int colId) {
        if(newsColumnMapper.assertColExistAndEnabled(colId) == 0) {
            //栏目不存在或没有开启
            throw new RuntimeException("栏目不存在！");
        }
        return newsColumnMapper.selectEnabledSubColInfoByParentId(colId);
    }
}
