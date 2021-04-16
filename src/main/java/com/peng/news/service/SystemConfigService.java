package com.peng.news.service;

import com.peng.news.model.vo.SystemConfigVO;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 21:08
 */
public interface SystemConfigService {
    String setReviewLevel(int reviewLevel);

    SystemConfigVO loadCurSystemConfig();

    /**
     * 得到当前系统配置的review_level
     * @return
     */
    int getCurReviewLevel();
}
