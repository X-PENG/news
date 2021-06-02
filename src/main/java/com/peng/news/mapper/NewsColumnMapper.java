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
     * 通过父栏目id，查询已开启的子栏目列表
     * @param parentId
     * @return
     */
    List<NewsColumnVO> getEnabledChildrenNewsColumnListByParentId(Integer parentId);

    NewsColumnVO selectTitleAndWithParentById(Integer colId);

    /**
     * 根据parent_id查询子栏目列表，并且带有栏目设置信息
     * @param parentId
     * @return
     */
    List<NewsColumnVO> columnListWithSettingsByParentId(Integer parentId);

    /**
     * 查询已开启的栏目，并且携带父栏目信息以及栏目设置信息
     * @param colId
     * @return
     */
    NewsColumnVO selectEnabledColWithParentAndSettingsById(Integer colId);

    /**
     * 按照parent_id查询出所有开启的子栏目id
     * @param parentId
     * @return
     */
    List<Integer> selectEnabledSubColByParentId(Integer parentId);

    /**
     * 按照parent_id查询所有开启的子栏目 信息
     * @param parentId
     * @return
     */
    List<NewsColumnVO> selectEnabledSubColInfoByParentId(Integer parentId);

    int assertColExistAndEnabled(Integer colId);
}
