package com.cy.pj.sys.service;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

import java.util.Map;

public interface SysUserService {

    /**
     * 基于指定列查询记录总数
     */
    boolean isExist(String columnName, String columnValue);


    /**
     * 基于用户id获取用户以及对应的部门信息,角色信息
     */
    PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent);

    /**
     * 禁用启用用户
     */
    int validById(Integer id, Integer valid);

    int saveObject(SysUser entity, Integer[] roleIds);

    Map<String,Object> findObjectById(Integer userId) ;

    int updateObject(SysUser entity,Integer[] roleIds);
}
