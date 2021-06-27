package com.peng.news.cache.service.imp;

import com.peng.news.cache.service.NewsListCacheManageService;
import com.peng.news.model.dto.NewsListDTO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.NewsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 使用装饰者模式增强NewsListServiceImpl
 * @author PENG
 * @version 1.0
 * @date 2021/6/26 23:50
 */
@Service("newsListServiceDecoratedByCache")
public class NewsListServiceDecoratedByCacheImpl implements NewsListService {
    @Autowired
    @Qualifier("newsListService")
    NewsListService newsListServiceImpl;

    @Autowired
    NewsListCacheManageService newsListCacheManageService;
    /**
     * 先查缓存，再查数据库
     * @param colId
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public NewsListDTO newsListByColId(int colId, int page, int pageSize) {
        //先查缓存
        NewsListDTO newsList = newsListCacheManageService.getCurPageNewsListCache(colId, page);
        if(newsList == null) {
            //再查数据库
            newsList = newsListServiceImpl.newsListByColId(colId, page, pageSize);
            newsListCacheManageService.putCurPageNewsListCache(colId, page, newsList);
        }

        return newsList;
    }

    @Override
    public List<NewsColumnVO> subColList(int colId) {
        return newsListServiceImpl.subColList(colId);
    }
}
