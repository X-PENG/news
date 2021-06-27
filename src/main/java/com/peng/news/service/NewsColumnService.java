package com.peng.news.service;

import com.peng.news.model.po.NewsColumnPO;
import com.peng.news.model.vo.NewsColumnVO;

import java.util.List;

/**
 * 新闻栏目服务
 * @author PENG
 * @version 1.0
 * @date 2021/4/9 12:43
 */
public interface NewsColumnService {

    /**
     * 通过父栏目id查询所有的子栏目
     * @param parentId
     * @return
     */
    List<NewsColumnVO> getAllColumnsByParentId(Integer parentId);

    /**
     * 添加新闻栏目
     * @param newsColumnVO
     * @return
     */
    Integer addNewsColumn(NewsColumnVO newsColumnVO);

    /**
     * 删除新闻栏目
     * @param newsColId 新闻栏目id
     * @return
     */
    NewsColumnPO delNewsColumn(Integer newsColId);

    /**
     * 修改新闻栏目基本信息和设置信息，会更新：
     * 新闻栏目表的：
     *  title, description, parentId, externalLink
     * 新闻栏目设置表的：
     *  menuOrder, moduleOrder
     *
     * 必须传递所有信息，如果menuOrder为null，则不更新menuOrder
     * @param newsColumnVO
     * @return
     */
    NewsColumnPO updateNewsColumn(NewsColumnVO newsColumnVO);


    /**
     * 开启或关闭某个新闻栏目
     * @param newsColId
     * @param enabled
     * @return
     */
    NewsColumnPO enableOrDisableNewsColumn(Integer newsColId, boolean enabled);

    /**
     * 查询新闻栏目级联选择器数据
     * @return
     */
    List<NewsColumnVO> newsColumnSelectData();

    boolean changeNewsColShowImgStatus(Integer newsColId, boolean status);
}
