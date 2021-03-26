package com.peng.news.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 20:16
 */
@Data
@TableName("role_resource")
@NoArgsConstructor
public class RoleResourcePO {

    public RoleResourcePO(Integer roleId, Integer resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }

    @TableId(type = IdType.AUTO)
    Integer id;
    Integer roleId;
    Integer resourceId;
}
