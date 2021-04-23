package com.peng.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peng.news.model.po.NewsColumnPO;
import com.peng.news.model.vo.NewsColumnVO;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/9 12:38
 */
public interface NewsColumnMapper extends BaseMapper<NewsColumnPO> {

    /**
     * 通过父栏目id，查询子栏目列表
     * @param parentId
     * @return
     */
    List<NewsColumnVO> getChildrenNewsColumnListByParentId(Integer parentId);

    NewsColumnVO selectTitleAndWithParentById(Integer colId);

    /**
     * 根据parent_id查询子栏目列表，并且带有栏目设置信息
     * @param parentId
     * @return
     */
    List<NewsColumnVO> columnListWithSettingsByParentId(Integer parentId);
}
