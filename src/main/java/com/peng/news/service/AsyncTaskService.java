package com.peng.news.service;

/**
 * 异步任务service
 * @author PENG
 * @version 1.0
 * @date 2021/4/26 21:48
 */
public interface AsyncTaskService {

    /**
     * 异步增加新闻的阅读量
     * @param newsId
     */
    void increaseReadingCount(int newsId);
}
