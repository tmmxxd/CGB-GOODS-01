package com.cy.pj.sys.service.impl;

import com.cy.pj.common.config.PaginationProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private PaginationProperties paginationProperties;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public int updateObject(SysUser entity, Integer[] roleIds) {
        //1.参数有效性验证
        if (entity == null)
            throw new IllegalArgumentException("保存对象不能为空");
        if (StringUtils.isEmpty(entity.getUsername()))
            throw new IllegalArgumentException("用户名不能为空");
        if (roleIds == null || roleIds.length == 0)
            throw new IllegalArgumentException("必须为其指定角色");
        //其它验证自己实现，例如用户名已经存在，密码长度，...
        //2.更新用户自身信息
        int rows = sysUserDao.updateObject(entity);
        //3.保存用户与角色关系数据
//        sysUserRoleDao.deleteObjectsByUserId(entity.getId());
        sysUserRoleDao.deleteById("user_id", entity.getId());
        sysUserRoleDao.insertObjects(entity.getId(),
                roleIds);
        //4.返回结果
        return rows;
    }

    @Override
    public Map<String, Object> findObjectById(Integer userId) {
        //1.合法性验证
        if (userId == null || userId <= 0)
            throw new ServiceException(
                    "参数数据不合法,userId=" + userId);
        //2.业务查询
        SysUserDeptVo user =
                sysUserDao.findObjectById(userId);
        if (user == null)
            throw new ServiceException("此用户已经不存在");
        List<Integer> roleIds =
                sysUserRoleDao.findRoleIdsByUserId(userId);
        //3.数据封装
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roleIds", roleIds);
        return map;
    }

    @Override
    public int saveObject(SysUser entity, Integer[] roleIds) {
        long start = System.currentTimeMillis();
        log.info("start:" + start);
        //1.参数校验
        if (entity == null)
            throw new ServiceException("保存对象不能为空");
        if (StringUtils.isEmpty(entity.getUsername()))
            throw new ServiceException("用户名不能为空");
        if (StringUtils.isEmpty(entity.getPassword()))
            throw new ServiceException("密码不能为空");
        if (roleIds == null || roleIds.length == 0)
            throw new ServiceException("至少要为用户分配角色");
        //2.保存用户自身信息
        //2.1对密码进行加密
        String source = entity.getPassword();
        String salt = UUID.randomUUID().toString();
        SimpleHash sh = new SimpleHash(//Shiro框架
                "MD5",//algorithmName 算法
                source,//原密码
                salt, //盐值
                1);//hashIterations表示加密次数
        entity.setSalt(salt);
        entity.setPassword(sh.toHex());
        int rows = sysUserDao.insertObject(entity);
        //3.保存用户角色关系数据
        sysUserRoleDao.insertObjects(entity.getId(), roleIds);
        long end = System.currentTimeMillis();
        log.info("end:" + end);
        log.info("total time :" + (end - start));
        //4.返回结果
        return rows;
    }

    @Override
    public int validById(Integer id, Integer valid) {
        //1.参数校验
        Assert.isArgumentValid(id == null || id < 1, "id值不正确");
        Assert.isArgumentValid(valid != 1 && valid != 0, "状态值不正确");
        //2.执行更新并校验
        int rows = sysUserDao.validById(id, valid, "admin");
        Assert.isServiceValid(rows == 0, "记录可能已经不存在了");
        //3.返回结果
        return rows;
    }

    @Override
//    public int isExist(String columnName, String columnValue) {
    public boolean isExist(String columnName, String columnValue) {
        int rows = sysUserDao.isExist(columnName, columnValue);
//        Assert.isServiceValid(rows > 0, "记录已经存在");
        return rows > 0;
    }

    @Override
    public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
        //1.参数校验
        Assert.isArgumentValid(pageCurrent == null || pageCurrent < 1, "页码值不正确");
        //2.查询总记录数并校验
        int rowCount = sysUserDao.getRowCount(username);
        Assert.isServiceValid(rowCount == 0, "没有找到对应记录");
        int pageSize = paginationProperties.getPageSize();// 页面大小
        int startIndex = paginationProperties.getStartIndex(pageCurrent);
        //3.查询当前页用户记录信息
        List<SysUserDeptVo> records = sysUserDao.findPageObjects(username, startIndex, pageSize);
        //封装结果返回
        // 4.封装查询结果并返回
        return new PageObject<>(rowCount, records, pageSize, pageCurrent);
    }
}
