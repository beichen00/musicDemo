package com.woniuxy.Custom;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.mapper.RoleMapper;
import com.woniuxy.mapper.UserMapper;
import com.woniuxy.model.Permission;
import com.woniuxy.model.Role;
import com.woniuxy.model.User;
import com.woniuxy.service.PermissionService;
import com.woniuxy.service.RoleService;
import com.woniuxy.service.UserService;
import com.woniuxy.utils.JWTUtils;
import com.woniuxy.utils.JwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: 北陈
 * @Date: 2021/1/30 21:07
 * @Description:
 */
public class MyRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleService roleService;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionService permissionService;
    @Resource
    private PermissionMapper permissionMapper;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String token = (String) principalCollection.getPrimaryPrincipal();
        String username = JWTUtils.decodedJWT(token).getClaim("username").asString();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        List<String> roleByUserId = roleService.findRoleByUserId(user.getId());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        roleByUserId.forEach(roleName->{
            simpleAuthorizationInfo.addRole(roleName);
        });
        List<Permission> perByPerId = permissionService.findPerByPerId(user.getId());
        perByPerId.forEach(permission -> {
            simpleAuthorizationInfo.addStringPermission(permission.getElement());
        });
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        String username = JWTUtils.decodedJWT(token).getClaim("username").asString();
        if (!StringUtils.hasLength(token) || !JWTUtils.comparaToAuthentication(token,username)){
            throw new AuthenticationException("token验证失败！！");
        }
        return new SimpleAuthenticationInfo(token,token,this.getName());
    }
}
