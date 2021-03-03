package com.woniuxy.service;

import com.woniuxy.model.Permission;
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
public interface PermissionService extends IService<Permission> {
    List<Permission> findPerByPerId(Integer uId);
    List<Integer> findPermissionByRid(Integer rid);
    List<Permission> findAllPermissions();

}
