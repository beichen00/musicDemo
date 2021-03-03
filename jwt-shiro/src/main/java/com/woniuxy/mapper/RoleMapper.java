package com.woniuxy.mapper;

import com.woniuxy.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 北陈
 * @since 2021-03-02
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT r.rolename  " +
            "FROM t_user AS u  " +
            "JOIN t_user_role AS ur  " +
            "ON u.id=ur.uid  " +
            "JOIN t_role AS r  " +
            "ON ur.rid=r.id  " +
            "WHERE u.id=#{uid}")
    List<String> findRoleByUserId(Integer uid);

    @Delete("DELETE FROM t_user_role WHERE uid=#{uid}")
    void deletRolesByUId(Integer uid);

    @Insert("INSERT t_user_role(uid,rid) VALUES(#{uid},#{rid})")
    void insertRolesByUIdAndRoleId(@RequestParam Integer uid, @RequestParam Integer rid);


}
