package com.peng.news.util;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/14 21:50
 */
public interface Constants {
    /**
     * 参与新闻编辑的用户姓名分隔符
     */
    String EDITORS_SEPARATOR = "、";

    /**
     * 参与审核的用户姓名分隔符
     */
    String REVIEWERS_SEPARATOR = "、";

    /**
     * 当新闻审核失败时，审核失败相关信息保存在extra字段中时的key
     */
    String REVIEW_FAIL_KEY = "REVIEW_FAIL";

    /**
     * 当新闻被打回修改时，相关信息保存在extra字段中时的key
     */
    String RE_MODIFICATION_KEY = "RE_MODIFICATION";

    /**
     * 当新闻设为轮播发布时，轮播图片地址保存在extra字段中时的key
     */
    String CAROUSEL_IMAGE_URL_KEY = "CAROUSEL_IMAGE_URL";
}
