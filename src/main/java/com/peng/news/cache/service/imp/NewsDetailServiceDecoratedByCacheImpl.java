package com.peng.news.cache.service.imp;

import com.peng.news.cache.constant.CacheConstants;
import com.peng.news.cache.service.NewsDetailServiceDecoratedByCache;
import com.peng.news.model.vo.NewsVO;
import com.peng.news.service.imp.NewsDetailServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/6/24 16:37
 */
@Service
public class NewsDetailServiceDecoratedByCacheImpl extends NewsDetailServiceImpl implements NewsDetailServiceDecoratedByCache, InitializingBean {

    @Autowired
    CacheManager cacheManager;

    Cache hotNewsCache;

    @Override
    public NewsVO getHotNewsCache(int newsId) {
        NewsVO newsVO = getNewsInfoCache(newsId);
        if(newsVO == null) {
            return null;
        }
        newsVO.setRealReadingCount(getNewsReadingCountCache(newsId));

        return newsVO;
    }

    @Override
    public void putHotNewsCache(NewsVO news) {
        //缓存头条新闻和轮播新闻
        if(news != null && (news.getIsHeadlines() || news.getIsCarousel())) {
            int newsId = news.getId().intValue();
            hotNewsCache.put(newsId, news);
            hotNewsCache.put(getNewsReadingCountCacheKey(newsId), news.getRealReadingCount());
        }
    }

    @Override
    public int getNewsReadingCountCache(int newsId) {
        Cache.ValueWrapper valueWrapper = hotNewsCache.get(getNewsReadingCountCacheKey(newsId));
        Object cacheVal = null;
        if(valueWrapper != null && (cacheVal = valueWrapper.get()) != null) {
            return (Integer) cacheVal;
        }

        return 0;
    }

    @Override
    public void increaseNewsReadingCountCache(int newsId) {
        int oldReadingCount = getNewsReadingCountCache(newsId);
        hotNewsCache.put(getNewsReadingCountCacheKey(newsId), oldReadingCount + 1);
    }

    /**
     * 先查缓存，再查数据库
     * @param newsId
     * @return
     */
    @Override
    protected NewsVO queryNews(int newsId) {
        //先查缓存
        NewsVO result = getHotNewsCache(newsId);

        if(result == null) {
            //通过父类查询数据库
            result = super.queryNews(newsId);
            //将查询结果放入缓存
            putHotNewsCache(result);
        }

        return result;
    }

    /**
     * 既要更新缓存中的阅读量，也要更新数据库中的
     * @param newsId
     */
    @Override
    protected void increaseNewsReadingCount(int newsId) {
        increaseNewsReadingCountCache(newsId);
        //通过父类更新数据库
        super.increaseNewsReadingCount(newsId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initCache();
    }

    private void initCache() {
        hotNewsCache = cacheManager.getCache(CacheConstants.CACHE_NAME_HOT_NEWS);
    }

    /**
     * 从缓存中查询新闻基本信息
     * @param newsId
     * @return
     */
    private NewsVO getNewsInfoCache(int newsId) {
        Cache.ValueWrapper valueWrapper = hotNewsCache.get(newsId);
        if(valueWrapper == null) {
            return null;
        }

        return ((NewsVO) valueWrapper.get());
    }

    private String getNewsReadingCountCacheKey(int newsId) {
        return CacheConstants.CACHE_KEY_PREFIX_NEWS_READING_COUNT + CacheConstants.CACHE_KEY_SEPARATOR + newsId;
    }
}
