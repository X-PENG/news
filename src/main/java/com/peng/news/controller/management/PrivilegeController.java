package com.peng.news.controller.management;

import com.peng.news.model.CustomizedPage;
import com.peng.news.model.Result;
import com.peng.news.model.po.RolePO;
import com.peng.news.model.vo.RoleVO;
import com.peng.news.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限管理相关接口
 * @author PENG
 * @version 1.0
 * @date 2021/3/24 10:33
 */
@RestController
@RequestMapping("/management/role")
public class PrivilegeController {

    @Autowired
    RoleService roleService;

    @GetMapping("/hello")
    public String hello(){
        return this.getClass().getName() + " hello";
    }

    @GetMapping("/")
    public Result<CustomizedPage<RoleVO>> roleList(int page, int pageSize){
        return Result.success(roleService.roleList(page, pageSize));
    }

    @PostMapping("/")
    public Result addRole(RolePO rolePO){
        roleService.addRole(rolePO);
        return Result.success("添加成功！");
    }

    @DeleteMapping("/{roleId}")
    public Result delRole(@PathVariable Integer roleId){
        roleService.delRole(roleId);
        return Result.success("删除成功！");
    }

    /**
     * 给角色设置可访问的资源
     * @param roleId
     * @param resourceIds
     * @return
     */
    @PutMapping("/{roleId}")
    public Result setResourceForRole(@PathVariable Integer roleId,@RequestBody Integer[] resourceIds){
        roleService.setResourcesForRole(roleId, resourceIds);
        return Result.success("设置成功！");
    }
}
