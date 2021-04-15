package com.peng.news.model.dto;

import com.peng.news.model.po.UserPO;
import lombok.Data;

/**
 * 封装打回修改相关信息
 * @author PENG
 * @version 1.0
 * @date 2021/4/15 14:49
 */
@Data
public class ReModificationInfoDTO {

    /**
     * 操作人
     */
    UserPO operator;


    /**
     * 操作时间
     */
    String operateTime;

    /**
     * 意见
     */
    String suggestion;
}
