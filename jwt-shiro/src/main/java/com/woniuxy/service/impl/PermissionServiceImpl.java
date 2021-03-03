package com.woniuxy.service.impl;

import com.woniuxy.model.Permission;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;
    @Override
    public List<Permission> findPerByPerId(Integer uId) {
        List<Permission> permissionList = permissionMapper.findPerByPerId(uId);
        return permissionList;
    }

    //根据某用户id查询该用户的权限id
    @Override
    public List<Integer> findPermissionByRid(Integer rid) {
        List<Integer> permissionByRid = permissionMapper.findPermissionByRid(rid);
        return permissionByRid;
    }

    //查询所有的权限，如某个用户的权限一样也要做分级菜单处理
    @Override
    public List<Permission> findAllPermissions() {
        //先查出所有的菜单
        List<Permission> permissions = permissionMapper.selectList(null);
        //实现一个功能，将菜单进行重新组合，每个二级菜单都对应于它的一级菜单
        ArrayList<Permission> permissions1=new ArrayList<>();//这是一级菜单的集合
        //找出所有的一级菜单，然后将一级菜单添加进去
        permissions.forEach(permission -> {
            if (permission.getLevel()==1){
                //同时增加一个二级菜单
                permission.setChildren(new ArrayList<Permission>());
                permissions1.add(permission);
            }
        });
        //再次循环遍历permission所有权限,找出一级菜单对应的二级菜单，并加入一级菜单的子菜单中
        permissions.forEach(permission -> {
            permissions1.forEach(permission1 -> {
                if (permission.getPid()==permission1.getId()){
                    //再添加一个子菜单集合进去为三级菜单
                    permission.setChildren(new ArrayList<Permission>());
                    permission1.getChildren().add(permission);
                }
            });
        });
        //再次循环遍历permission和二级菜单、三级菜单
        permissions.forEach(permission -> {
            if (permission.getLevel()==3){
                //遍历一级菜单
                permissions1.forEach(permission1 -> {
                    //从一级菜单下拿出二级菜单
                    List<Permission> permissions2 = permission1.getChildren();
                    //遍历二级菜单
                    permissions2.forEach(permission2 -> {
                        if (permission.getPid()==permission2.getId()){
                            permission2.getChildren().add(permission);
                        }
                    });
                });
            }
        });
        return permissions1;
    }




}
