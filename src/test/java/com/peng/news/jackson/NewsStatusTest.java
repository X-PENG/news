package com.peng.news.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.vo.NewsVO;
import org.junit.Before;
import org.junit.Test;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/6/25 23:35
 */
public class NewsStatusTest {
    ObjectMapper om;

    @Before
    public void init() {
        om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);
    }

    @Test
    public void t1() throws JsonProcessingException {
        String jsonStr = om.writeValueAsString(NewsStatus.PUBLISHED);
        System.out.println(jsonStr);
        NewsStatus status = om.readValue(jsonStr, NewsStatus.class);
        System.out.println(status);
    }

    @Test
    public void t2() throws JsonProcessingException {
        NewsVO newsVO = new NewsVO();
        newsVO.setId(1);
        newsVO.setTitle("标题");
        newsVO.setContent("正文");
        newsVO.setNewsStatus(NewsStatus.PUBLISHED);
        String jsonStr = om.writeValueAsString(newsVO);
        System.out.println(jsonStr);
        NewsVO newsVO1 = om.readValue(jsonStr, NewsVO.class);
        System.out.println(newsVO1);
    }
}
