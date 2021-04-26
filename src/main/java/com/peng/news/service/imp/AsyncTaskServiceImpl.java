package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.po.NewsPO;
import com.peng.news.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/26 21:48
 */
@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {

    @Autowired
    NewsMapper newsMapper;

    /**
     * 方法必须同步，否则会丢失更新
     * @param newsId
     */
    @Async
    @Override
    public synchronized void increaseReadingCount(int newsId) {
        //先查询旧值
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<NewsPO>().select("real_reading_count").eq("id", newsId);
        int oldValue = newsMapper.selectOne(queryWrapper).getRealReadingCount();

        //设置新值
        UpdateWrapper<NewsPO> updateWrapper = new UpdateWrapper<NewsPO>().set("real_reading_count", oldValue + 1).eq("id", newsId);
        newsMapper.update(null, updateWrapper);
    }
}
