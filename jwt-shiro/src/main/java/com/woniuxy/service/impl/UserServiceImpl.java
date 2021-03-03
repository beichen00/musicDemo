package com.woniuxy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.woniuxy.model.Permission;
import com.woniuxy.model.User;
import com.woniuxy.mapper.UserMapper;
import com.woniuxy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;


    //根据当前登录用户id查询该用户的所有能够展示的菜单信息
    @Override
    public List<Permission> findMenusByUserId(Integer uId) {
        List<Permission> permissions = userMapper.findMenusByUserId(uId);
        //实现一个功能：将菜单进行组合，每个耳机菜单都对应于他自己的一级菜单
        ArrayList<Permission> root=new ArrayList<>();//这是所有的一级菜单
        permissions.forEach(permission -> {

            if (permission.getLevel()==1){
                //每增加一个主菜单，就增加一个二级菜单
                permission.setChildren(new ArrayList<Permission>());
                root.add(permission);


            }
        });

        root.forEach(r->{

        });
        //再次遍历permissions，取二级菜单
        permissions.forEach(permission -> {
            root.forEach(r->{

                //获得二级菜单并加入一级菜单的子菜单中
                if (permission.getPid()==r.getId()){

                    r.getChildren().add(permission);
                }
            });
        });
        return root;
    }

    @Override
    public IPage<User> findUsers(int current, int size) {
        //查询所有用户信息,分页查询
        Page<User> page=new Page(current,size);
        IPage<User> iPage = userMapper.selectPage(page, null);
        return iPage;

    }
}
