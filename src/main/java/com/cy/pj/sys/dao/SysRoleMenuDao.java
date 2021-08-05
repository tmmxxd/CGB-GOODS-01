package com.cy.pj.sys.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRoleMenuDao {

    @Delete("delete from sys_role_menus where menu_id = #{menuId}")
    int deleteObjectsByMenuId(Integer menuId);
}
