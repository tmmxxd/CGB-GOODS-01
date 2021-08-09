package com.cy.pj.sys.service.impl;

import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    //后续追加扩展业务
    /**
     * Cacheable 表示将查询到数据,存储到cache对象中,这个cache对象对应的名字为menuCache
     * 问题:
     * 1)请问menuCache中存储的value为谁?方法的返回值
     * 2)请问menuCache中存储数据时使用的key,在这里默认谁? simpleKey(存储实际参数的值,将这些值得组合作为key)
     * */
    @Cacheable("menuCache")
    @Override
    public List<Map<String, Object>> findObjects() {
        System.out.println("======SysMenuServiceImpl.findObjects==============");
        return sysMenuDao.findObjects();
    }

    @CacheEvict(value = "menuCache", allEntries = true, beforeInvocation = false)
    @Override
//    @RequiredCache
    public int deleteObject(Integer id) {
        //1.参数校验
        Assert.isArgumentValid(id == null || id < 1, "id值无效");
        //2.获取当前菜单对应的子菜单个数并校验
        int childCount = sysMenuDao.getChildCount(id);
        Assert.isServiceValid(childCount > 0, "请先删除子元素");
        //3.删除菜单对应的关系数据
//        sysRoleMenuDao.deleteObjectsByMenuId(id);
        sysRoleMenuDao.deleteById("menu_id", id);
        //4.删除菜单自身信息并校验
        int rows = sysMenuDao.deleteObject(id);
        Assert.isServiceValid(rows == 0, "记录可能以及不存在");
        return rows;
    }

    @Override
    public List<Node> findZtreeMenuNodes() {
        return sysMenuDao.findZtreeMenuNodes();
    }

    @CacheEvict(value = "menuCache", allEntries = true, beforeInvocation = false)
    @Override
    public int saveObject(SysMenu entity) {
        //1.参数校验
        Assert.isArgumentValid(entity == null, "保存对象不能为空");
        Assert.isEmpty(entity.getName(), "菜单名不能为空");
//        if (StringUtils.isEmpty(entity.getName())) {
//            throw new ServiceException("菜单名不能为空");
//        }
        //2.保存菜单信息

        int rows = sysMenuDao.insertObject(entity);//有可能出现网络中单
        //3.返回结果
        return rows;
    }

    /**
     * value的值menuCache为具体cache对象
     * allEntries=true表示要清除cache中所有数据
     * beforeInvocation 表示要在更新之前执行
     */
    @CacheEvict(value = "menuCache", allEntries = true, beforeInvocation = false)
    @Override
    public int updateObject(SysMenu entity) {
        //1.参数校验
        Assert.isArgumentValid(entity == null, "保存对象不能为空");
        Assert.isEmpty(entity.getName(), "菜单名不能为空");
//        if (StringUtils.isEmpty(entity.getName())) {
//            throw new ServiceException("菜单名不能为空");
//        }
        //2.保存菜单信息

        int rows = sysMenuDao.updateObject(entity);//有可能出现网络中单
        //3.返回结果
        return rows;
    }
}
