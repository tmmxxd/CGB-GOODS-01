package com.cy.pj.sys.service.impl;

import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysDeptDao;
import com.cy.pj.sys.entity.SysDept;
import com.cy.pj.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptDao sysDeptDao;

    @Override
    public List<Map<String, Object>> findObjects() {
        return sysDeptDao.findObjects();
    }

    @Override
    public int deleteObject(Integer id) {
        //1.参数校验
        Assert.isArgumentValid(id == null || id < 1, "id值无效");
        //2.获取当前菜单对应的子菜单个数并校验
        int childCount = sysDeptDao.getChildCount(id);
        Assert.isServiceValid(childCount > 0, "请先删除子元素");
        //3.删除菜单对应的关系数据
//        sysRoleMenuDao.deleteObjectsByMenuId(id);
        //4.删除菜单自身信息并校验
        int rows = sysDeptDao.deleteObject(id);
        Assert.isServiceValid(rows == 0, "记录可能以及不存在");
        return rows;
    }

    @Override
    public List<Node> findZtreeDeptNodes() {
        return sysDeptDao.findZtreeMenuNodes();
    }

    @Override
    public int saveObject(SysDept entity) {
        //1.参数校验
        Assert.isArgumentValid(entity == null, "保存对象不能为空");
        Assert.isEmpty(entity.getName(), "菜单名不能为空");
//        if (StringUtils.isEmpty(entity.getName())) {
//            throw new ServiceException("菜单名不能为空");
//        }
        //2.保存菜单信息
        int rows = sysDeptDao.insertObject(entity);//有可能出现网络中单
        //3.返回结果
        return rows;
    }

    @Override
    public int updateObject(SysDept entity) {
        //1.参数校验
        Assert.isArgumentValid(entity == null, "保存对象不能为空");
        Assert.isEmpty(entity.getName(), "菜单名不能为空");
//        if (StringUtils.isEmpty(entity.getName())) {
//            throw new ServiceException("菜单名不能为空");
//        }
        //2.保存菜单信息
        int rows = sysDeptDao.updateObject(entity);//有可能出现网络中单
        //3.返回结果
        return rows;
    }

}
