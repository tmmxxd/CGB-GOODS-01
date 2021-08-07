package com.cy.pj.sys.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRoleDao {

    /**
     * 插入用户角色对应关系数据
     */
    int insertObjects(Integer userId, Integer[] roleIds);

    List<Integer> findRoleIdsByUserId(Integer id);

    @Delete("delete from sys_user_roles where ${columnName}=#{id}")
    int deleteById(String columnName, Integer id);

//    @Delete("delete from sys_user_roles where role_id=#{roleId}")
//    int deleteObjectsByRoleId(Integer roleId);
//
//    @Delete(" delete from sys_user_roles where user_id=#{userId}")
//    int deleteObjectsByUserId(Integer userId);
}
