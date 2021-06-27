package com.peng.news.cache.service.imp;

import com.peng.news.cache.service.FrontendIndexPageCacheManageService;
import com.peng.news.cache.constant.CacheConstants;
import com.peng.news.model.po.NewsPO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/6/23 16:06
 */
@Service
public class FrontendIndexPageCacheManageServiceImpl implements FrontendIndexPageCacheManageService, InitializingBean {

    @Autowired
    CacheManager cacheManager;

    Cache frontendIndexPageCache;

    @Override
    public void delHeadlinesCache(int expectedNewsId) {
        Cache.ValueWrapper valueWrapper = frontendIndexPageCache.get(CacheConstants.CACHE_KEY_FRONTEND_HEADLINES);
        Object cacheVal = null;
        //有缓存
        if(valueWrapper != null) {
            //有缓存，但是值为NullValue对象（不需要删除）
//            if((cacheVal = valueWrapper.get()) == null) {
//                frontendIndexPageCache.evict(CacheConstants.CACHE_KEY_FRONTEND_HEADLINES);
//            }else

            if((cacheVal = valueWrapper.get()) != null && ((NewsPO) cacheVal).getId() == expectedNewsId) {
                //有缓存，缓存的头条新闻的id就是expectedNewsId，则删除头条新闻的缓存
                frontendIndexPageCache.evict(CacheConstants.CACHE_KEY_FRONTEND_HEADLINES);
            }
        }
    }

    @Override
    public void delCarouselCache(int expectedNewsId) {
        Cache.ValueWrapper valueWrapper = frontendIndexPageCache.get(CacheConstants.CACHE_KEY_FRONTEND_CAROUSEL);
        Object cacheVal = null;
        if(valueWrapper != null && (cacheVal = valueWrapper.get()) != null) {
            List list = (List) cacheVal;
            for (int i = 0; i < list.size(); i++) {
                if(((NewsPO) list.get(i)).getId() == expectedNewsId) {
                    frontendIndexPageCache.evict(CacheConstants.CACHE_KEY_FRONTEND_CAROUSEL);
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initCache();
    }

    private void initCache() {
        frontendIndexPageCache = cacheManager.getCache(CacheConstants.CACHE_NAME_FRONTEND_INDEX_PAGE);
    }
}
