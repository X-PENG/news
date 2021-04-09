package com.peng.news.model.vo;

import lombok.Data;

/**
 * @author  PENG
 * @date  2021/4/9 10:20
 * @version 1.0
 */
@Data
public class SystemConfigVO {

    /**
     * 当前审核等级
     */
    Integer reviewLevel;

    /**
     * 最大审核等级
     */
    Integer maxReviewLevel;
}
