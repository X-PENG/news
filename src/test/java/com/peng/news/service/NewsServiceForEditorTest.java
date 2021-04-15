package com.peng.news.service;

import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.CustomizedPage;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.paramBean.NewsBeanForEditorSave;
import com.peng.news.model.paramBean.QueryNewsBeanForEditor;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/14 18:48
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NewsServiceForEditorTest {

    @Autowired
    NewsServiceForEditor newsServiceForEditor;

    @Autowired
    NewsMapper newsMapper;

    /**
     * 主要看看打印的sql对不对
     */
    @Rollback
    @Transactional
    @Test
    public void transitNewsList(){

        String titlePrefix = "20210414测试用例添加测试数据";
        int n = 5;
        //先插入几条记录
        for (int i = 1; i <= n; i++) {
            NewsBeanForEditorSave saveBean = new NewsBeanForEditorSave();
            saveBean.setTitle(titlePrefix + i);
            NewsPO newsPO = new NewsPO();
            BeanUtils.copyProperties(saveBean, newsPO);
            newsPO.setNewsStatus(NewsStatus.UPLOAD_SUCCESS.getCode());
            newsPO.setInputterId(1);
            newsPO.setLatestEditorId(1);
            newsMapper.insert(newsPO);
        }

        int page = 1;
        int pageSize = 10;

        QueryNewsBeanForEditor queryBean = new QueryNewsBeanForEditor();
        queryBean.setTitle(titlePrefix);

        CustomizedPage<NewsVO> selectPage = newsServiceForEditor.transitNewsList(page, pageSize, queryBean);
        Assert.assertEquals(selectPage.getRecords().size(), n);

        queryBean.setOrderByLatestEditTime(true);
        selectPage = newsServiceForEditor.transitNewsList(page, pageSize, queryBean);
        Assert.assertEquals(selectPage.getRecords().size(), n);

        queryBean.setOrderByLatestEditTime(false);
        selectPage = newsServiceForEditor.transitNewsList(page, pageSize, queryBean);
        Assert.assertEquals(selectPage.getRecords().size(), n);

        queryBean.setOrderByLatestEditTime(null);
        queryBean.setNewsStatus(NewsStatus.UPLOAD_SUCCESS.getCode());
        selectPage = newsServiceForEditor.transitNewsList(page, pageSize, queryBean);
        Assert.assertEquals(selectPage.getRecords().size(), n);
    }
}
