package com.woniuxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.woniuxy.model.Permission;
import com.woniuxy.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 北陈
 * @since 2021-03-02
 */
public interface UserService extends IService<User> {

    List<Permission> findMenusByUserId(Integer uId);
    IPage<User> findUsers(int current, int size);
}
