package com.woniuxy.service;

import com.woniuxy.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.woniuxy.vo.UserRoles;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 北陈
 * @since 2021-03-02
 */
public interface RoleService extends IService<Role> {
    List<String> findRoleByUserId(Integer uid);
    //管理员对用户的角色进行修改
    void insertRolesByUserRoles(UserRoles userRoles);

    Role findRoleByRoleName(String roleName);

    void toChangeNewPermission(Integer rid,List<Integer> pids);

    void deletRolesByUserId(Integer id);
}
