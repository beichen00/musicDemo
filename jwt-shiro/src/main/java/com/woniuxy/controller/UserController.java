package com.woniuxy.controller;


import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.woniuxy.dto.Result;
import com.woniuxy.dto.Status;
import com.woniuxy.mapper.UserMapper;
import com.woniuxy.model.Permission;
import com.woniuxy.model.User;
import com.woniuxy.service.RoleService;
import com.woniuxy.service.UserService;
import com.woniuxy.utils.JWTUtils;
import com.woniuxy.utils.JwtToken;
import com.woniuxy.utils.SaltUtils;
import com.woniuxy.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;
    @PostMapping("/register")
    public Result register(@RequestBody UserVo userVo){
        String salt = SaltUtils.getSalt(5);
        Md5Hash md5Hash = new Md5Hash(userVo.getPassword(), salt,1024);
        String password = md5Hash.toHex();

        User user = User.builder().username(userVo.getUsername()).password(password).salt(salt).build();
        userMapper.insert(user);
        return new Result(true, Status.OK,"注册成功");
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserVo userVo){

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userVo.getUsername());
        User uDB = userMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(uDB)){
            Md5Hash md5Hash = new Md5Hash(userVo.getPassword(),uDB.getSalt(),1024);
            String password = md5Hash.toHex();

            if (password.equals(uDB.getPassword())){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username",userVo.getUsername());
                String jwtToken = JWTUtils.getToken(hashMap);
                return new Result(true,Status.OK,"登录成功",jwtToken);
            }else {
                return new Result(false,Status.CREDENTAILERROR,"密码错误");
            }
        }else{
            return new Result(false,Status.PRINCINPALERROR,"用户名不存在");
        }

    }

    @GetMapping("/menus")
    public Result getMenusByUserName(HttpServletRequest request){
        //获取请求头
        String token = request.getHeader("Token");
        String username = JWTUtils.decodedJWT(token).getClaim("username").asString();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        List<Permission> menus = userService.findMenusByUserId(user.getId());
        return new Result(true,Status.OK,"查询菜单成功",menus);
    }

    //当前登录查看所有用户，需要判断当前用户是否用户该类查看权限的去恶色
    @GetMapping("/all")
    @RequiresRoles(value = {"董事长","人事总监"},logical = Logical.OR)
    public Result getUserAll(int current,int size){
        IPage<User> users = userService.findUsers(current, size);
        if (!ObjectUtils.isEmpty(users)){
            return new Result(true,Status.OK,"查询成功",users);
        }
        return new Result(false,Status.ERROR,"查询失败");
    }

    @GetMapping("/userToNull")
    public Result deletUserById(Integer id){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        int delete = userMapper.delete(wrapper);
        //再删除角色信息
        roleService.deletRolesByUserId(id);
        if (delete>0){
            return new Result(true,Status.OK,"删除成功");
        }
        return new Result(true,Status.ERROR,"删除失败");
    }
}

