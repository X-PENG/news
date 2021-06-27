package com.peng.news.cache;

import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.FrontendIndexService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 前台首页数据缓存测试
 * @author PENG
 * @version 1.0
 * @date 2021/6/22 16:39
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FrontendIndexDataCacheTest {
    @Autowired
    FrontendIndexService frontendIndexService;

    @Test
    public void t1() {
        //导航菜单
        List<NewsColumnVO> newsColumnVOS1 = frontendIndexService.allEnabledOneLevelColsOrderByMenuOrder();
        List<NewsColumnVO> newsColumnVOS2 = frontendIndexService.allEnabledOneLevelColsOrderByMenuOrder();
        Assert.assertTrue(newsColumnVOS1.size() == newsColumnVOS2.size());
        for (int i = 0; i < newsColumnVOS1.size(); i++) {
            NewsColumnVO newsColumnVO1 = newsColumnVOS1.get(i);
            NewsColumnVO newsColumnVO2 = newsColumnVOS2.get(i);
            Assert.assertTrue(newsColumnVO1.getId().equals(newsColumnVO2.getId()));
        }

        //头条新闻
        NewsPO headLines1 = frontendIndexService.getHeadLines();
        NewsPO headLines2 = frontendIndexService.getHeadLines();
        Assert.assertTrue(headLines1.getId().equals(headLines2.getId()));

        //轮播新闻
        List<NewsPO> carouselNewsList1 = frontendIndexService.carouselNewsList(5);
        List<NewsPO> carouselNewsList2 = frontendIndexService.carouselNewsList(5);
        Assert.assertTrue(carouselNewsList1.size() == carouselNewsList2.size());
        for (int i = 0; i < carouselNewsList1.size(); i++) {
            Assert.assertTrue(carouselNewsList1.get(i).getId().equals(carouselNewsList2.get(i).getId()));
        }
    }
}
