package com.woniuxy.mapper;

import com.woniuxy.model.Permission;
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
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT p.element    " +
            "FROM t_user AS u    " +
            "JOIN t_user_role AS ur    " +
            "ON u.id=ur.uid    " +
            "JOIN t_role AS r    " +
            "ON ur.rid=r.id    " +
            "JOIN t_role_permission AS rp    " +
            "ON r.id=rp.rid    " +
            "JOIN t_permission AS p    " +
            "ON rp.pid=p.id    " +
            "WHERE u.id=#{uid}    " +
            "AND p.level=3")
    List<Permission> findPerByPerId(Integer uid);

    //根据角色id查询该角色拥有哪些权限
    @Select("SELECT p.id " +
            "FROM t_permission AS p  " +
            "JOIN t_role_permission AS rp " +
            "ON p.id=rp.pid " +
            "JOIN t_role AS r " +
            "ON rp.rid=r.id " +
            "WHERE r.id=#{rid} " + "AND p.`level`=3 ")
    List<Integer> findPermissionByRid(Integer rid);

    //根据3级菜单id查询2级菜单id
    @Select("SELECT p.pid  " +
            "FROM t_permission AS p  " +
            "WHERE p.`level`=3  " +
            "AND p.id=#{id}")
    Integer query2menu(Integer id);

    //根据3级菜单id一级查出来的2级菜单id查询一级菜单id
    @Select("SELECT pid   " +
            "FROM t_permission AS p   " +
            "WHERE p.`level`=2   " +
            "AND p.id=(SELECT p.pid   " +
            "FROM t_permission AS p   " +
            "WHERE p.`level`=3   " +
            "AND p.id=#{id})")
    Integer query1Menu(Integer id);

    //删除该角色的所有权限
    @Delete("DELETE FROM t_role_permission WHERE rid=#{rid}")
    void deletPermissionByRid(Integer rid);

    //新增角色的权限
    @Insert("INSERT INTO t_role_permission(rid,pid) VALUES(#{rid},#{pid})")
    void insertPermissionByRidAndPid(@RequestParam Integer rid, Integer pid);

}
