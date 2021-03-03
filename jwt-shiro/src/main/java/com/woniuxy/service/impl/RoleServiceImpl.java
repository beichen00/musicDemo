package com.woniuxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.model.Role;
import com.woniuxy.mapper.RoleMapper;
import com.woniuxy.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniuxy.vo.UserRoles;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 北陈
 * @since 2021-03-02
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<String> findRoleByUserId(Integer uid) {
        List<String> roles = roleMapper.findRoleByUserId(uid);
        return roles;

    }

    @Override
    public void insertRolesByUserRoles(UserRoles userRoles) {
        //首先时根据选择的用户id删除数据库表中该用户的所有角色
        //获取被修改角色的用户id
        Integer uid=userRoles.getUid();
        roleMapper.deletRolesByUId(uid);
        //然后再查询前台选择的角色的id，需要遍历
        List<String> checkList = userRoles.getCheckList();
        checkList.forEach(check->{
            //先根据角色名字查询该角色的id
            Role roleDB = findRoleByRoleName(check);
            if(!ObjectUtils.isEmpty(roleDB)){
                Integer rid=roleDB.getId();
                //然后往数据库中添加
                roleMapper.insertRolesByUIdAndRoleId(uid,rid);
            }
        });

    }


    //根据角色名字查询该角色进一步获取角色id
    @Override
    public Role findRoleByRoleName(String roleName) {
        QueryWrapper wrapper=new QueryWrapper<>();
        wrapper.eq("rolename",roleName);
        Role role = roleMapper.selectOne(wrapper);
        return role;
    }



    @Override
    public void toChangeNewPermission(Integer rid,List<Integer> pids) {

        //在修改该角色的权限之前，需要将该角色原有权限全部删除
        permissionMapper.deletPermissionByRid(rid);

        //创建存放一级菜单和二级菜单的集合
        HashSet<Integer> permisions1=new HashSet<>();
        HashSet<Integer> permisions2=new HashSet<>();

        //先查询二级菜单
        pids.forEach(pid->{
            //根据三级的id即pid查询当前pid所指向的permission，如果有父级菜单，添加到permissions1，然后再找到一级菜单，并加入相应集合中
            Integer twoMenuId = permissionMapper.query2menu(pid);
            permisions2.add(twoMenuId);
            //查询到后，将三级的权限id添加到角色权限里面
            permissionMapper.insertPermissionByRidAndPid(rid,pid);
        });

        //再查询一级菜单
        pids.forEach(pid->{
            Integer oneMenuId = permissionMapper.query1Menu(pid);
            permisions1.add(oneMenuId);
        });

        //然后再遍历二级菜单和一级菜单，并加入角色权限中
        permisions2.forEach(permission2->{
            permissionMapper.insertPermissionByRidAndPid(rid,permission2);
        });
        permisions1.forEach(permission1->{
            permissionMapper.insertPermissionByRidAndPid(rid,permission1);
        });
    }

    @Override
    public void deletRolesByUserId(Integer id) {
        roleMapper.deletRolesByUId(id);
    }

}
