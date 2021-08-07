package com.cy.pj.sys.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMenuDao {

    /**
     * 写入角色和菜单关系数据
     */
    int insertObjects(@Param("roleId") Integer roleId, @Param("menuIds") Integer[] menuIds);

    @Delete("delete from sys_role_menus where ${columnName} = #{id}")
    int deleteById(String columnName, Integer id);
//    @Delete("delete from sys_role_menus where menu_id = #{menuId}")
//    int deleteObjectsByMenuId(Integer menuId);
//
//    @Delete("delete from sys_role_menus where role_id = #{roleId}")
//    int deleteObjectsByRoleId(Integer roleId);

    @Select("select menu_id from sys_role_menus where role_id=#{roleId}")
    List<Integer> findMenuIdsByRoleId(Integer roleId);
}
