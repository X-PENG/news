package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.peng.news.mapper.NewsColumnMapper;
import com.peng.news.mapper.NewsMapper;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.NewsColumnPO;
import com.peng.news.model.po.NewsPO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.NewsColumnService;
import com.peng.news.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/9 12:43
 */
@Slf4j
@Service
public class NewsColumnServiceImpl implements NewsColumnService {

    static final String NEWS_COL_NOT_EXISTS_MSG_FOR_DEL_OR_UPDATE = "当前栏目不存在，操作失败！";

    @Autowired
    NewsColumnMapper newsColumnMapper;

    @Autowired
    NewsMapper newsMapper;

    @Override
    public List<NewsColumnVO> getAllColumnsByParentId(Integer parentId) {
        return newsColumnMapper.columnListWithSettingsByParentId(parentId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addNewsColumn(NewsColumnVO newsColumnVO) {
        //信息完整性、合法性校验以及格式化信息
        validateInfoAndFormat(newsColumnVO);
        //格式化完之后，保证title不重复
        assertTitleNotExists(newsColumnVO.getTitle(), null);

        //处理parent_id
        Integer parentId = newsColumnVO.getParentId();
        if(parentId != null){
            //把parent_id这个栏目设为父栏目
            operateParentNewsCol(parentId, false);
        }

        //插入栏目基本信息
        NewsColumnPO newsColumnPO = new NewsColumnPO();
        newsColumnPO.setTitle(newsColumnVO.getTitle());
        newsColumnPO.setDescription(newsColumnVO.getDescription());
        newsColumnPO.setParentId(newsColumnVO.getParentId());
        newsColumnPO.setExternalLink(newsColumnVO.getExternalLink());
        newsColumnMapper.insert(newsColumnPO);

        return newsColumnPO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delNewsColumn(Integer newsColId) {
        NewsColumnPO newsColumnPO = assertNewsColExists(newsColId, NEWS_COL_NOT_EXISTS_MSG_FOR_DEL_OR_UPDATE);

        if(hasChildren(newsColId) > 0){
            throw new RuntimeException("当前栏目包含子栏目，不允许删除，操作失败！");
        }

        if(hasNews(newsColId)){
            throw new RuntimeException("当前栏目下已经发布了新闻，不允许删除，操作失败！");
        }

        //如果有父栏目，则取消设置
        if(newsColumnPO.getParentId() != null){
            operateParentNewsCol(newsColumnPO.getParentId(), true);
        }

        newsColumnMapper.deleteById(newsColId);

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateNewsColumn(NewsColumnVO newsColumnVO) {
        Integer columnVOId = newsColumnVO.getId();

        //确保要更新的新闻栏目存在
        NewsColumnPO queryResult = assertNewsColExists(columnVOId, NEWS_COL_NOT_EXISTS_MSG_FOR_DEL_OR_UPDATE);

        if(columnVOId.equals(newsColumnVO.getParentId())){
            throw new RuntimeException("自己不能作为自己的父栏目，操作失败！");
        }

        //信息完整性、合法性校验以及格式化
        validateInfoAndFormat(newsColumnVO);

        //格式化后，保证title不重复
        assertTitleNotExists(newsColumnVO.getTitle(), newsColumnVO.getId());

        //处理父栏目
        Integer newParentId = newsColumnVO.getParentId();
        Integer oldParentId = queryResult.getParentId();
        if(oldParentId != null && !oldParentId.equals(newParentId)){
            //将原父栏目取消设置为父栏目
            operateParentNewsCol(oldParentId, true);
        }

        if(newParentId != null && !newParentId.equals(oldParentId)){
            //设为父栏目
            operateParentNewsCol(newParentId, false);
        }

        //按照id更新栏目基本信息
        UpdateWrapper<NewsColumnPO> updateWrapper = new UpdateWrapper<NewsColumnPO>().eq("id", columnVOId);
        updateWrapper.set("title", newsColumnVO.getTitle());
        updateWrapper.set("parent_id", newParentId);
        updateWrapper.set("external_link", newsColumnVO.getExternalLink());
        updateWrapper.set("description", newsColumnVO.getDescription());
        //更新menu_order和module_order
        updateWrapper.set("menu_order", newsColumnVO.getMenuOrder());
        updateWrapper.set("module_order", newsColumnVO.getModuleOrder());
        //执行更新
        newsColumnMapper.update(null, updateWrapper);

        return true;
    }

    @Override
    public boolean enableOrDisableNewsColumn(Integer newsColId, boolean enabled) {
        assertNewsColExists(newsColId, NEWS_COL_NOT_EXISTS_MSG_FOR_DEL_OR_UPDATE);

        //若是关闭栏目
        if(!enabled){
            if(hasChildren(newsColId) > 0){
                throw new RuntimeException("当前栏目包含子栏目，不允许关闭，操作失败！");
            }

            if(hasNews(newsColId)){
                throw new RuntimeException("当前栏目下已经发布了新闻，不允许关闭，操作失败！");
            }
        }

        //更新enabled
        newsColumnMapper.update(null, new UpdateWrapper<NewsColumnPO>().eq("id", newsColId).set("enabled", enabled));
        return true;
    }

    @Override
    public List<NewsColumnVO> newsColumnSelectData() {
        //获得所有一级栏目
        List<NewsColumnVO> list = newsColumnMapper.getEnabledChildrenNewsColumnListByParentId(null);
        List<NewsColumnVO> returnList = new ArrayList<>();
        for (NewsColumnVO newsColumnVO : list) {
            if(newsColumnVO.getIsHasChildren()) {
                //查询子栏目列表
                newsColumnVO.setChildren(newsColumnMapper.getEnabledChildrenNewsColumnListByParentId(newsColumnVO.getId()));
                /**
                 * 如果该栏目有子栏目，就创建一个不包含子栏目的副本，并添加到集合中，目的是：
                 * 解决element-ui的级联选择器不能选择父节点的问题
                 */
                //创建副本
                NewsColumnVO copyOne = new NewsColumnVO();
                copyOne.setId(newsColumnVO.getId());
                copyOne.setTitle(newsColumnVO.getTitle());

                //将无孩子的副本添加到集合中
                returnList.add(copyOne);
            }
            //添加自己
            returnList.add(newsColumnVO);
        }
        return returnList;
    }

    @Override
    public boolean changeNewsColShowImgStatus(Integer newsColId, boolean status) {
        //确保栏目存在
        assertNewsColExists(newsColId, NEWS_COL_NOT_EXISTS_MSG_FOR_DEL_OR_UPDATE);
        //更新show_img_on_the_right字段
        newsColumnMapper.update(null, new UpdateWrapper<NewsColumnPO>().eq("id", newsColId).set("show_img_on_the_right", status));
        return true;
    }


    /**
     * 保证title不重复
     * @param title
     * @param expectedNewsColId 预期的新闻id，如果title存在，并且是预期的栏目，则通过；不是预期的栏目，则表示重复了；若为null，则只要查到了，就表示重复了
     */
    void assertTitleNotExists(String title, Integer expectedNewsColId){
        /**
         * EXPLAIN select count(*) from news_column where title = ?
         * EXPLAIN select id from news_column where title = ?
         * 执行计划是一样的，type都是const，很快！
         */
        NewsColumnPO newsColumnPO = newsColumnMapper.selectOne(new QueryWrapper<NewsColumnPO>().select("id").eq("title", title));

        //如果存在，并且不是排除的栏目
        if(newsColumnPO != null && !newsColumnPO.getId().equals(expectedNewsColId)){
            throw new RuntimeException("已经存在【" + title + "】栏目，操作失败！");
        }
    }

    /**
     * 保证存在这个新闻栏目
     * @param newsColId 新闻栏目id
     * @param exceptionMsg 新闻栏目不存在时的异常消息
     * @return
     */
    NewsColumnPO assertNewsColExists(Integer newsColId, String exceptionMsg){
        if(newsColId == null) {
            throw new RuntimeException(exceptionMsg);
        }

        NewsColumnPO newsColumnPO = newsColumnMapper.selectOne(new QueryWrapper<NewsColumnPO>().eq("id", newsColId));
        if(newsColumnPO == null){
            throw new RuntimeException(exceptionMsg);
        }

        return newsColumnPO;
    }

    /**
     * 判断该新闻栏目下是否发布了新闻
     * @param newsColId 新闻栏目id
     * @return false：没有发布新闻，可以删除或关闭栏目；true：发布了新闻，不能删除或关闭
     */
    boolean hasNews(Integer newsColId){
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("column_id", newsColId).eq("news_status", NewsStatus.PUBLISHED.getCode());
        return newsMapper.selectCount(queryWrapper) >= 1;
    }

    /**
     * 判断当前栏目是否包含子栏目
     * @param newsColId
     * @return true：则不能删除或关闭当前栏目； false：可以删除或关闭当前栏目
     */
    Integer hasChildren(Integer newsColId){
        return newsColumnMapper.selectCount(new QueryWrapper<NewsColumnPO>().eq("parent_id", newsColId));
    }

    /**
     * 操作父栏目：要么将parentId设置为父栏目，要么取消将parentId设为父栏目；
     * 设为父栏目，则需要将parentId栏目的is_has_children设为true；
     * 取消设置父栏目，则需要判断parentId栏目是否包含子栏目，没有的话将is_has_children设为false，有就设为true
     * @param parentId
     * @param canceled true，有栏目取消将parentId设置为父栏目；false，将栏目将parentId设为父栏目；
     */
    void operateParentNewsCol(Integer parentId, boolean canceled) {
        //如果是设为父栏目
        if(!canceled){
            //首先，保证父栏目一定存在
            NewsColumnPO newsColumnPO = assertNewsColExists(parentId, "设置的父栏目不存在，操作失败！");
            //保证要设为父栏目的新闻栏目必须是一级栏目
            if(newsColumnPO.getParentId() != null){
                throw new RuntimeException("最多支持二级子栏目，操作失败！");
            }
            newsColumnMapper.update(null, new UpdateWrapper<NewsColumnPO>().eq("id", parentId).set("is_has_children", true));
        }else{
            Integer childrenCount = hasChildren(parentId);
            newsColumnMapper.update(null, new UpdateWrapper<NewsColumnPO>().eq("id", parentId).set("is_has_children", childrenCount > 1));
        }
    }

    /**
     * 信息完整性校验，title没有默认值，所以不能为null
     * @param newsColumnVO
     * @return
     */
    boolean validateInfoIsComplete(NewsColumnVO newsColumnVO){
        boolean validateResult = true;
        String msg = null;
        String title = newsColumnVO.getTitle();
        if(title == null || "".equals(title.trim())){
            validateResult = false;
            msg = "栏目名称不能为空，操作失败！";
        }

        if (!validateResult){
            throw new RuntimeException(msg);
        }
        return true;
    }

    /**
     * 信息合法性校验
     * @param newsColumnVO
     * @return
     */
    boolean validateInfoIsLegality(NewsColumnVO newsColumnVO){
        boolean validateResult = true;
        String msg = null;
        Integer menuOrder = newsColumnVO.getMenuOrder();
        Integer moduleOrder = newsColumnVO.getModuleOrder();
        if(menuOrder != null && (menuOrder < 1 || menuOrder > 127)){
            validateResult = false;
            msg = "菜单序号必须在1~127之间，操作失败！";
        }else if(moduleOrder != null && (moduleOrder < 1 || moduleOrder > 127)){
            validateResult = false;
            msg = "模块序号必须在1~127之间，操作失败！";
        }

        if (!validateResult){
            throw new RuntimeException(msg);
        }
        return true;
    }

    boolean validateInfoAndFormat(NewsColumnVO newsColumnVO){
        validateInfoIsComplete(newsColumnVO);
        validateInfoIsLegality(newsColumnVO);

        //校验成功，再格式化信息，修剪字符串两边空格
        newsColumnVO.setTitle(newsColumnVO.getTitle().trim());
        String externalLink = newsColumnVO.getExternalLink();
        newsColumnVO.setExternalLink(!StringUtils.isNotEmpty(externalLink) ? null : externalLink.trim());
        String description = newsColumnVO.getDescription();
        newsColumnVO.setDescription(!StringUtils.isNotEmpty(description) ? null : description.trim());
        return true;
    }
}
