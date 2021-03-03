package com.woniuxy.mapper;

import com.woniuxy.model.Permission;
import com.woniuxy.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 北陈
 * @since 2021-03-02
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT p.* " +
            "            FROM t_user AS u " +
            "            JOIN t_user_role AS ur " +
            "            ON ur.uid=u.id " +
            "            JOIN t_role AS r " +
            "            ON ur.rid=r.id " +
            "            JOIN t_role_permission as rp " +
            "            ON r.id=rp.rid " +
            "            JOIN t_permission AS p " +
            "            ON rp.pid=p.id " +
            "            WHERE u.id=#{uid}")
    List<Permission> findMenusByUserId(Integer uid);

}
