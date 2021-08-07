package com.cy.pj.sys.service;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

import java.util.List;

public interface SysRoleService {


    /**
     * 更新自身信息和角色对应的关系数据
     */
    int updateObject(SysRole entity, Integer[] menuIds);

    /**
     * 保存自身信息和角色对应的关系数据
     */
    int saveObject(SysRole entity, Integer[] menuIds);

    PageObject<SysRole> findPageObjects(String name, Integer pageCurrent);

    int deleteObject(Integer id);

    SysRoleMenuVo findObjectById(Integer id);

    List<SysRole> findRoles();
}
