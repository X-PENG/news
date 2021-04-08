package com.peng.news.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.peng.news.mapper.ResourceMapper;
import com.peng.news.mapper.RoleResourceMapper;
import com.peng.news.mapper.SystemConfigMapper;
import com.peng.news.model.po.ResourcePO;
import com.peng.news.model.po.RoleResourcePO;
import com.peng.news.model.po.SystemConfigPO;
import com.peng.news.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 21:09
 */
@Service
@Slf4j
public class SystemConfigServiceImpl implements SystemConfigService {

    /**
     * 默认查询的id
     */
    static final Integer DEFAULT_ID = 1;

    /**
     * 审核资源的模板
     */
    static final String REVIEW_RESOURCE_TEMPLATE = "/management/news/review/%d/**";

    /**
     * 最大审核等级
     */
    static Integer MAX_REVIEW_LEVEL = 6;

    @Autowired
    SystemConfigMapper systemConfigMapper;

    @Autowired
    ResourceMapper resourceMapper;

    @Autowired
    RoleResourceMapper roleResourceMapper;

    /**
     * 本地缓存系统配置的review_level
     */
    Integer cacheForReviewLevel;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean setReviewLevel(int reviewLevel) {
        if(reviewLevel < 0 || reviewLevel > MAX_REVIEW_LEVEL){
            throw new RuntimeException("非法设置，审核等级不能小于0或不能大于" + MAX_REVIEW_LEVEL);
        }

        //清空缓存
        cacheForReviewLevel = null;

        SystemConfigPO curSystemConfig = loadCurSystemConfig();
        Integer curReviewLevel = curSystemConfig.getReviewLevel();
        if(reviewLevel != curReviewLevel){
            //状态需要改变的资源
            List<String> resourcesToBeChanged = new ArrayList<>();
            //状态改变的结果
            Boolean status;
            if(reviewLevel < curReviewLevel){
                //小于的话，就要删除/禁用几个有关审核的资源
                status = false;
                for (int i = reviewLevel + 1; i <= curReviewLevel ; i++) {
                    resourcesToBeChanged.add(String.format(REVIEW_RESOURCE_TEMPLATE, i));
                }
            }else{
                //大于的话，就要添加/开启几个有关审核的资源
                status = true;
                for (int i = curReviewLevel + 1; i <= reviewLevel ; i++) {
                    resourcesToBeChanged.add(String.format(REVIEW_RESOURCE_TEMPLATE, i));
                }
            }

            if(resourcesToBeChanged.size() != 0){
                for (String s : resourcesToBeChanged) {
                    UpdateWrapper<ResourcePO> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("enabled", status).eq("url", s);
                    resourceMapper.update(null, updateWrapper);
                }
                if(!status){
                    List<Object> resourceIdList = resourceMapper.selectObjs(new QueryWrapper<ResourcePO>().select("id").eq("url", resourcesToBeChanged));
                    //删除这些资源的分配记录
                    roleResourceMapper.delete(new QueryWrapper<RoleResourcePO>().in("resource_id", resourceIdList));
                }
            }

            UpdateWrapper<SystemConfigPO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("review_level", reviewLevel).eq("id", DEFAULT_ID);
            systemConfigMapper.update(null, updateWrapper);
        }
        return true;
    }

    @Override
    public SystemConfigPO loadCurSystemConfig() {
        return systemConfigMapper.selectById(DEFAULT_ID);
    }

    @Override
    public int getCurReviewLevel() {
        if(cacheForReviewLevel == null){
            //没有缓存就查询
            cacheForReviewLevel = loadCurSystemConfig().getReviewLevel();
        }
        return cacheForReviewLevel;
    }

    @Value("${news.system.max-review-level}")
    public void setMaxReviewLevel(Integer maxReviewLevel) {
        log.info("设置最大审核等级为" + maxReviewLevel);
        MAX_REVIEW_LEVEL = maxReviewLevel;
    }
}
