package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.peng.news.mapper.NewsColumnMapper;
import com.peng.news.model.po.NewsColumnPO;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.NewsColumnService;
import com.peng.news.util.StringUtils;
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
@Service
public class NewsColumnServiceImpl implements NewsColumnService {

    static final String NEWS_COL_NOT_EXISTS_MSG_FOR_DEL_OR_UPDATE = "当前栏目不存在，操作失败！";

    @Autowired
    NewsColumnMapper newsColumnMapper;

    @Override
    public List<NewsColumnPO> getAllColumnsByParentId(Integer parentId) {
        QueryWrapper<NewsColumnPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("menu_order", "create_time");
        //按parent_id查询所有开启了的新闻栏目
        if(parentId == null){
            queryWrapper.isNull("parent_id");
        }else{
            queryWrapper.eq("parent_id", parentId);
        }
        return newsColumnMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addNewsColumn(NewsColumnPO newsColumnPO) {
        //信息完整性、合法性校验
        validateInfo(newsColumnPO);
        //保证title不重复
        assertTitleNotExists(newsColumnPO.getTitle(), null);
        String externalLink = newsColumnPO.getExternalLink();
        newsColumnPO.setExternalLink(StringUtils.isNotEmpty(externalLink) ? externalLink : null);
        String description = newsColumnPO.getDescription();
        newsColumnPO.setDescription(StringUtils.isNotEmpty(description) ? description : null);
        newsColumnPO.setId(null);
        newsColumnPO.setModuleOrder(null);
        newsColumnPO.setEnabled(null);
        newsColumnPO.setIsHasChildren(null);
        //如果有parent_id
        if(newsColumnPO.getParentId() != null){
            //把parent_id这个栏目设为父栏目
            operateParentNewsCol(newsColumnPO.getParentId(), false);
        }
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
    public boolean updateNewsColumn(NewsColumnPO newsColumnPO) {
        //信息完整性、合法性校验
        validateInfo(newsColumnPO);

        NewsColumnPO queryResult = assertNewsColExists(newsColumnPO.getId(), NEWS_COL_NOT_EXISTS_MSG_FOR_DEL_OR_UPDATE);

        if(newsColumnPO.getId().equals(newsColumnPO.getParentId())){
            throw new RuntimeException("自己不能作为自己的父栏目，操作失败！");
        }

        UpdateWrapper<NewsColumnPO> updateWrapper = new UpdateWrapper<NewsColumnPO>().eq("id", newsColumnPO.getId());
        //保证title不重复
        assertTitleNotExists(newsColumnPO.getTitle(), newsColumnPO.getId());

        //处理父栏目
        Integer newParentId = newsColumnPO.getParentId();
        Integer oldParentId = queryResult.getParentId();
        if(oldParentId != null && !oldParentId.equals(newParentId)){
            //将原父栏目取消设置为父栏目
            operateParentNewsCol(oldParentId, true);
        }

        if(newParentId != null && !newParentId.equals(oldParentId)){
            //设为父栏目
            operateParentNewsCol(newParentId, false);
        }

        newsColumnPO.setParentId(null);
        updateWrapper.set("parent_id", newParentId);
        updateWrapper.set("module_order", newsColumnPO.getModuleOrder());
        String externalLink = newsColumnPO.getExternalLink();
        newsColumnPO.setExternalLink(null);
        updateWrapper.set("external_link", StringUtils.isNotEmpty(externalLink) ? externalLink : null);
        String description = newsColumnPO.getDescription();
        newsColumnPO.setDescription(null);
        updateWrapper.set("description", StringUtils.isNotEmpty(description) ? description : null);
        //避免更新如下信息
        newsColumnPO.setIsHasChildren(null);
        newsColumnPO.setEnabled(null);
        newsColumnPO.setCreateTime(null);

        newsColumnMapper.update(newsColumnPO, updateWrapper);
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

        newsColumnMapper.update(null, new UpdateWrapper<NewsColumnPO>().eq("id", newsColId).set("enabled", enabled));

        return true;
    }

    @Override
    public List<NewsColumnVO> newsColumnSelectData() {
        //获得所有一级栏目
        List<NewsColumnVO> list = newsColumnMapper.getChildrenNewsColumnListByParentId(null);
        List<NewsColumnVO> returnList = new ArrayList<>();
        for (NewsColumnVO newsColumnVO : list) {
            if(newsColumnVO.getIsHasChildren()) {
                //查询子栏目列表
                newsColumnVO.setChildren(newsColumnMapper.getChildrenNewsColumnListByParentId(newsColumnVO.getId()));
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
     * 判断该新闻栏目下是否有新闻
     * @param newsColId 新闻栏目id
     * @return false：没有新闻，可以删除或关闭栏目；true：有新闻，不能删除或关闭
     */
    boolean hasNews(Integer newsColId){
        /**
         * todo 查询 新闻表
         */
        return false;
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
     * @param newsColumnPO
     * @return
     */
    boolean validateInfoIsComplete(NewsColumnPO newsColumnPO){
        boolean validateResult = true;
        String msg = null;
        if(newsColumnPO.getTitle() == null){
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
     * @param newsColumnPO
     * @return
     */
    boolean validateInfoIsLegality(NewsColumnPO newsColumnPO){
        boolean validateResult = true;
        String msg = null;
        if("".equals(newsColumnPO.getTitle().trim())){
            validateResult = false;
            msg = "栏目名称不能为空，操作失败！";
        }else if(newsColumnPO.getMenuOrder() != null && (newsColumnPO.getMenuOrder() < 1 || newsColumnPO.getMenuOrder() > 127)){
            validateResult = false;
            msg = "菜单序号必须在1~127之间，操作失败！";
        }else if(newsColumnPO.getModuleOrder() != null && (newsColumnPO.getModuleOrder() < 1 && newsColumnPO.getModuleOrder() > 127)){
            validateResult = false;
            msg = "模块序号必须在1~127之间，操作失败！";
        }

        if (!validateResult){
            throw new RuntimeException(msg);
        }
        return true;
    }

    boolean validateInfo(NewsColumnPO newsColumnPO){
        validateInfoIsComplete(newsColumnPO);
        validateInfoIsLegality(newsColumnPO);
        return true;
    }
}
