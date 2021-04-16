package com.peng.news.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.peng.news.mapper.*;
import com.peng.news.model.dto.ReModificationInfoDTO;
import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.po.*;
import com.peng.news.model.vo.SystemConfigVO;
import com.peng.news.service.SystemConfigService;
import com.peng.news.util.Constants;
import com.peng.news.util.DateTimeFormatUtils;
import com.peng.news.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 如果要改变最大审核等级，就需要数据库管理员去数据库中新增没有的审核资源
     */
    static Integer MAX_REVIEW_LEVEL = 6;

    @Autowired
    SystemConfigMapper systemConfigMapper;

    @Autowired
    ResourceMapper resourceMapper;

    @Autowired
    RoleResourceMapper roleResourceMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    NewsMapper newsMapper;

    /**
     * 本地缓存系统配置的review_level
     */
    Integer cacheForReviewLevel;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String setReviewLevel(int reviewLevel) {
        if(reviewLevel < 0 || reviewLevel > MAX_REVIEW_LEVEL){
            throw new RuntimeException("非法设置，审核等级不能小于0或不能大于" + MAX_REVIEW_LEVEL);
        }

        //清空缓存
        cacheForReviewLevel = null;

        Integer curReviewLevel = loadCurSystemConfig().getReviewLevel();
        if(reviewLevel != curReviewLevel) {
            //状态需要改变的资源
            List<String> resourcesToBeChanged = new ArrayList<>();
            //状态改变的结果
            Boolean status;
            if(reviewLevel < curReviewLevel){
                //小于的话，就要禁用几个有关审核的资源
                status = false;
                for (int i = reviewLevel + 1; i <= curReviewLevel ; i++) {
                    resourcesToBeChanged.add(String.format(REVIEW_RESOURCE_TEMPLATE, i));
                }
            }else{
                //大于的话，就要开启几个有关审核的资源
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

                List<Object> resourceIdList = resourceMapper.selectObjs(new QueryWrapper<ResourcePO>().select("id").in("url", resourcesToBeChanged));
                if(!status){
                    //如果是禁用资源，则删除这些资源的分配记录
                    roleResourceMapper.delete(new QueryWrapper<RoleResourcePO>().in("resource_id", resourceIdList));
                }else {
                    //如果是开启资源，自动为系统管理员角色分配这些资源
                    List<Object> adminRoleIdList = roleMapper.selectObjs(new QueryWrapper<RolePO>().select("id").eq("is_system_admin", 1));
                    for(Object adminRoleId : adminRoleIdList){
                        for (Object resourceId : resourceIdList) {
                            RoleResourcePO roleResourcePO = new RoleResourcePO((Integer) adminRoleId, (Integer) resourceId);
                            roleResourceMapper.insert(roleResourcePO);
                        }
                    }
                }
            }

            UpdateWrapper<SystemConfigPO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("review_level", reviewLevel).eq("id", DEFAULT_ID);
            systemConfigMapper.update(null, updateWrapper);


            /**
             * 如果动态修改了审核等级，就把 所有 处于 审核中 的新闻 都 重置为 打回修改 的状态（也就是会回到新闻中转站）
             */
            List<Object> newsIdList = newsMapper.selectObjs(new QueryWrapper<NewsPO>().select("id").eq("news_status", NewsStatus.UNDER_REVIEW.getCode()));
            if(newsIdList.size() != 0) {
                //更新
                UpdateWrapper<NewsPO> newsPOUpdateWrapper = new UpdateWrapper<>();
                //所有审核中的新闻都更新
                newsPOUpdateWrapper.in("id", newsIdList);
                newsPOUpdateWrapper.set("news_status", NewsStatus.RE_MODIFICATION.getCode());
                newsPOUpdateWrapper.set("reviewers", null);
                newsPOUpdateWrapper.set("submitter_id", null);
                newsPOUpdateWrapper.set("submit_time", null);
                newsPOUpdateWrapper.set("current_review_epoch", 0);
                newsPOUpdateWrapper.set("previous_epoch_review_pass_time", null);
                //构造打回修改信息
                ReModificationInfoDTO reModificationInfoDTO = new ReModificationInfoDTO();
                reModificationInfoDTO.setOperateTime(DateTimeFormatUtils.format(LocalDateTime.now()));
                reModificationInfoDTO.setSuggestion("系统温馨提示：系统的审核流程发生变化，所有审核中的新闻全部回到中转站！");
                UserPO operator = new UserPO();
                operator.setId(UserUtils.getUser().getId());
                reModificationInfoDTO.setOperator(operator);
                Map map = new HashMap();
                map.put(Constants.RE_MODIFICATION_KEY, reModificationInfoDTO);
                newsPOUpdateWrapper.set("extra", JSON.toJSONString(map));

                newsMapper.update(null, newsPOUpdateWrapper);
            }

            return "设置成功！共有【" + newsIdList.size() + "】条审核中的新闻被打回到中转站。";
        }
        return "设置成功！";
    }

    @Override
    public SystemConfigVO loadCurSystemConfig() {
        SystemConfigPO systemConfigPO = systemConfigMapper.selectById(DEFAULT_ID);
        SystemConfigVO systemConfigVO = new SystemConfigVO();
        systemConfigVO.setReviewLevel(systemConfigPO.getReviewLevel());
        systemConfigVO.setMaxReviewLevel(MAX_REVIEW_LEVEL);
        return systemConfigVO;
    }

    @Override
    public int getCurReviewLevel() {
        if(cacheForReviewLevel == null){
            //没有缓存就查询，并缓存结果
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
