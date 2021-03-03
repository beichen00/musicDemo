package com.woniuxy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.woniuxy.dto.Result;
import com.woniuxy.dto.Status;
import com.woniuxy.mapper.UserMapper;
import com.woniuxy.model.Permission;
import com.woniuxy.model.User;
import com.woniuxy.service.PermissionService;
import com.woniuxy.utils.GetUserNameFromToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    @Resource
    private UserMapper userMapper;
    @GetMapping("btnAll")
    public Result getAllPermission(HttpServletRequest request){
        //从请求头获取登陆用户的信息
        String username = GetUserNameFromToken.getUsername(request);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);

        List<Permission> permissions = permissionService.findPerByPerId(user.getId());
        if (!CollectionUtils.isEmpty(permissions)){
            return new Result(true, Status.OK,"有三级按钮",permissions);
        }
        return new Result(false,Status.ERROR,"无任何按钮");

    }

    @GetMapping("permissionByRid")
    public Result getPermissionByRid(Integer id){
        System.out.println("用户id"+id);
        List<Integer> permissionByRid = permissionService.findPermissionByRid(id);
        return new Result(true,Status.OK,"查询成功",permissionByRid);
    }

    @GetMapping("allPermissionGrade")
    public Result getAllPermissionGrade(){
        List<Permission> allPermissions = permissionService.findAllPermissions();
        return new Result(true,Status.OK,"查询所有权限并分级成功",allPermissions);
    }
}

