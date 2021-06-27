package com.peng.news.cache.service.imp;

import com.peng.news.cache.constant.CacheConstants;
import com.peng.news.cache.service.NewsListCacheManageService;
import com.peng.news.model.dto.NewsListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/6/27 17:26
 */
@Service
public class NewsListCacheManageServiceImpl implements NewsListCacheManageService {
    @Autowired
    CacheManager cacheManager;

    @Override
    public NewsListDTO getCurPageNewsListCache(int colId, int page) {
        String cacheName = getCacheName(colId);
        Cache.ValueWrapper valueWrapper = cacheManager.getCache(cacheName).get(page);
        if(valueWrapper != null) {
            return ((NewsListDTO) valueWrapper.get());
        }

        return null;
    }

    @Override
    public void putCurPageNewsListCache(int colId, int page, NewsListDTO newsListDTO) {
        if(newsListDTO != null) {
            cacheManager.getCache(getCacheName(colId)).put(page, newsListDTO);
        }
    }

    @Override
    public void clearColumnNewsListCache(int colId) {
        cacheManager.getCache(getCacheName(colId)).clear();
    }

    /**
     * 返回当前栏目对应的cacheName
     * @param colId
     * @return
     */
    private String getCacheName(int colId) {
        return CacheConstants.CACHE_NAME_FRONTEND_NEWS_LIST + CacheConstants.CACHE_KEY_SEPARATOR + colId;
    }
}
