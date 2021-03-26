package com.peng.news.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 21:06
 */
@Data
@TableName("system_config")
public class SystemConfigPO {
    @TableId(type = IdType.AUTO)
    Integer id;

    Integer reviewLevel;
}
