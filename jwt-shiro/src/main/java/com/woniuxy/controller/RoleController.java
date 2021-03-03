package com.woniuxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.woniuxy.dto.Result;
import com.woniuxy.dto.Status;
import com.woniuxy.mapper.RoleMapper;
import com.woniuxy.model.Role;
import com.woniuxy.service.RoleService;
import com.woniuxy.vo.SetPermissionVo;
import com.woniuxy.vo.UserRoles;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 北陈
 * @since 2021-03-02
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;
    @Resource
    private RoleMapper roleMapper;

    //查询点击授予角色时的该用户的所有角色
    @GetMapping("rolesByUid/{id}")
    public Result getRolesByUid(@PathVariable Integer id){

        List<String> roles = roleService.findRoleByUserId(id);
        if (!CollectionUtils.isEmpty(roles)){
            return new Result(true, Status.OK,"查询该用户所有角色成功",roles);
        }
        return new Result(false,Status.ERROR,"无任何角色");
    }


    //查询该系统下所有角色
    @GetMapping("all")
    public Result getRoleAll(){
        List<Role> roles = roleMapper.selectList(null);
        if (!ObjectUtils.isEmpty(roles)){
            return new Result(true, Status.OK,"查询所有角色成功",roles);
        }
        return new Result(false,Status.ERROR,"无任何角色");
    }

    //管理员点击授予角色修改并点击确定之后
    @PostMapping
    @RequiresPermissions({"授予角色"})
    public Result changeRoles(@RequestBody UserRoles userRoles){
        roleService.insertRolesByUserRoles(userRoles);
        return new Result(true,Status.OK,"修改成功");
    }

    //前端点击角色授予权限之后，获得所选择的所有权限id
    @PostMapping("newPermission")
    @RequiresPermissions({"修改权限"})
    public Result toChangeNewPermission(@RequestBody SetPermissionVo setPermissionVo){
        System.out.println("修改角色权限"+setPermissionVo.getRid());
        roleService.toChangeNewPermission(setPermissionVo.getRid(),setPermissionVo.getPids());
        return new Result(true,Status.OK,"修改权限成功");
    }


}

