package com.cy.pj.sys.service.impl;

import com.cy.pj.common.config.PaginationProperties;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private PaginationProperties paginationProperties;

    @Override
    public int updateObject(SysRole entity, Integer[] menuIds) {   //1.参数校验
        Assert.isNull(entity, "保存对象不能为空");
        Assert.isEmpty(entity.getName(), "角色名不能为空");
        //2.保存角色自身信息
        int rows = sysRoleDao.updateObject(entity);
        //3.保存角色菜单关系数据
        //3.1先删除原有关系数据
        sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
        //3.2添加新的关系数据
        sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
        //4.返回结果
        return rows;
    }

    @Override
    public SysRoleMenuVo findObjectById(Integer id) {
        //1.参数校验
        Assert.isArgumentValid(id == null || id < 1, "id值无效");
        //2.查询
        SysRoleMenuVo rm = sysRoleDao.findObjectById(id);
        return rm;
    }

    @Override
    public int saveObject(SysRole entity, Integer[] menuIds) {
        //1.参数校验
        Assert.isNull(entity, "保存对象不能为空");
        Assert.isEmpty(entity.getName(), "角色名不能为空");
        //2.保存角色自身信息
        int rows = sysRoleDao.insertObject(entity);
        //3.保存角色菜单关系数据
        sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
        //4.返回结果
        return rows;
    }

    @Override
    public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
        Assert.isArgumentValid(pageCurrent == null || pageCurrent < 1, "当前页码值不正确");
        int rowCount = sysRoleDao.getRowCount(name);
        Assert.isServiceValid(rowCount == 0, "没有对应的记录");
        Integer startIndex = paginationProperties.getStartIndex(pageCurrent);
        Integer pageSize = paginationProperties.getPageSize();
        List<SysRole> records = sysRoleDao.findPageObjects(name, startIndex, pageSize);
        return new PageObject<>(rowCount, records, pageSize, pageCurrent);
    }

    @Override
    public int deleteObject(Integer id) {
        Assert.isArgumentValid(id == null || id < 1, "id值无效");
        sysRoleMenuDao.deleteObjectsByRoleId(id);
        sysUserRoleDao.deleteObjectsByRoleId(id);
        int rows = sysRoleDao.deleteObject(id);
        return rows;
    }
}
