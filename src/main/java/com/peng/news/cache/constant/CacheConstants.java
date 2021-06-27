package com.peng.news.cache.constant;

/**
 * 缓存常量
 * @author PENG
 * @version 1.0
 * @date 2021/6/21 19:28
 */
public class CacheConstants {

    /**
     * 缓存key的分隔符
     */
    public final static String CACHE_KEY_SEPARATOR = "::";

    /**
     * 前台首页缓存的cacheName
     */
    public final static String CACHE_NAME_FRONTEND_INDEX_PAGE = "frontendIndexPage";

    /**
     * 前台导航菜单key
     */
    public final static String CACHE_KEY_FRONTEND_NAVIGATION = "navigation";

    /**
     * 前台头条新闻key
     */
    public final static String CACHE_KEY_FRONTEND_HEADLINES = "headlines";

    /**
     * 前台轮播新闻key
     */
    public final static String CACHE_KEY_FRONTEND_CAROUSEL = "carousel";

    /**
     * 热点新闻缓存的cacheName
     */
    public final static String CACHE_NAME_HOT_NEWS = "hotNews";

    /**
     * 新闻阅读量缓存的key的前缀
     */
    public final static String CACHE_KEY_PREFIX_NEWS_READING_COUNT = "newsReadingCount";

    /**
     * 后台缓存的cacheName
     */
    public final static String CACHE_NAME_REAR_END = "rearEnd";

    /**
     * 所有资源（即所有经权限管理的url）且带有角色列表的缓存的cacheName
     */
    public final static String CACHE_KEY_ALL_RESOURCE_WITH_ROLE_LIST = "allResourceWithRoleList";

    /**
     * 前台新闻列表的cacheName
     */
    public final static String CACHE_NAME_FRONTEND_NEWS_LIST = "frontendNewsList";
}
