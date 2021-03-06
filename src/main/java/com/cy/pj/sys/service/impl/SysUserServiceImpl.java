package com.cy.pj.sys.service.impl;

import com.cy.pj.common.annotation.RequiredLog;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Transactional(timeout = 30,
        readOnly = false,
        isolation = Isolation.READ_COMMITTED,
        rollbackFor = Throwable.class,
        propagation = Propagation.REQUIRED)
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private PaginationProperties paginationProperties;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Cacheable("userCache")
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

    /**
     * @CacheEvict key:为实际参数的组合,这里的意思是清除和key为 id的值
     * @RequiresPermissions 为shiro框架中的一个注解, 此注解用于描述需要授权访问的方法
     * 注解内部value属性的值(为符合特定规范的字符串[数组也可以]),表示要访问此方法需要的权限
     * 1)系统执行此方法时会判定此方法上是否有@RequiresPermissions,并且获取注解中的内容
     * 2)并基于shiro框架底层机制获取当前登陆用户,然后获取用户的权限信息
     * 3)检测登陆用户的权限中是否包含@RequiresPermissions注解中指定的字符串,假如包含则授权访问
     */
    @RequiresPermissions("sys:user:update")
//    @Transactional //假如在spring中没有控制事务,默认是mybatis框架在控制事务
    @CacheEvict(value = "userCache", key = "#entity.id")
    @RequiredLog(operation = "禁用启用")
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

    /**
     * Propagation.REQUIRED 特性表示参与到一个已有的事务中去,假如没有已有的事务,
     * 则自己开启事务
     * Propagation.REQUIRES_NEW 特性表示此方法永远运行在一个新的事务中
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @RequiredLog(operation = "分页查询")//执行此方法时要进行日志记录
    @Override
    public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
        System.out.println("user.findPage=" + Thread.currentThread().getName());
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
