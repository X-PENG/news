package com.peng.news.model.dto;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;
import lombok.Data;

/**
 * 封装新闻列表
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 19:42
 */
@Data
public class NewsListDTO {

    /**
     * 栏目信息。表示哪个栏目的新闻列表
     */
    private NewsColumnVO column;

    /**
     * 分页查询结果
     */
    private CustomizedPage<NewsPO> news;
}
