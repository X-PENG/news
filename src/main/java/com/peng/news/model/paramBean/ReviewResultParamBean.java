package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 封装审核结果参数
 * @author PENG
 * @version 1.0
 * @date 2021/4/16 11:30
 */
@Data
public class ReviewResultParamBean {


    /**
     * 是否审核成功
     */
    Boolean reviewSuccess;

    /**
     * 审核失败的意见
     */
    String suggestion;
}
