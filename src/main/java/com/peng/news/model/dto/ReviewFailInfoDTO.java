package com.peng.news.model.dto;

import com.peng.news.model.po.UserPO;
import lombok.Data;

/**
 * 封装审核失败相关信息
 * @author PENG
 * @version 1.0
 * @date 2021/4/15 14:46
 */
@Data
public class ReviewFailInfoDTO {

    /**
     * 审核轮次
     */
    Integer epoch;

    /**
     * 审核人
     */
    UserPO reviewer;

    /**
     * 审核时间
     */
    String reviewTime;

    /**
     * 审核意见
     */
    String suggestion;

}
