package com.cy.pj.sys.service.impl;

import com.cy.pj.common.config.PaginationProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDao sysLogDao;

    @Autowired
    private PaginationProperties paginationProperties;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveObject(SysLog entity) {
        System.out.println("log.save=" + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sysLogDao.insertObject(entity);
    }

    @Override
    public int deleteObjects(Integer... ids) {
        // 1.判定参数合法性
       /* if (ids == null || ids.length == 0)
            throw new IllegalArgumentException("请选择一个");*/
        Assert.isArgumentValid(ids == null || ids.length == 0, "请选中记录进行删除");
        // 2.执行删除操作
        int rows;
        try {
            rows = sysLogDao.deleteObjects(ids);
        } catch (Throwable e) {
            e.printStackTrace();
            // 发出报警信息(例如给运维人员发短信)
            throw new ServiceException("系统故障，正在恢复中...");
        }
        // 4.对结果进行验证
       /* if (rows == 0)
            throw new ServiceException("记录可能已经不存在");*/
        Assert.isServiceValid(rows == 0, "记录可能已经不存在");

        // 5.返回结果
        return rows;
    }

    @Override
    public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
        // 1.参数校验
      /*  if (pageCurrent == null || pageCurrent < 1)
            throw new IllegalArgumentException("页码值不正确");*/
        Assert.isArgumentValid(pageCurrent == null || pageCurrent < 1, "页码值不正确");
        // 2.基于用户名查询总记录数并校验
//        int rowCount = sysLogDao.getRowCount(username);
//        Assert.isServiceValid(rowCount == 0, "没有找到对应记录");
       /* if (rowCount == 0)
            throw new ServiceException("没有找到对应记录");*/
        // 3.查询当前页日志记录
        int pageSize = paginationProperties.getPageSize();// 页面大小
        Page<Object> page = PageHelper.startPage(pageCurrent, pageSize);
        int startIndex = paginationProperties.getStartIndex(pageCurrent);
        List<SysLog> records = sysLogDao.findPageObjects(username);
        // 4.封装查询结果并返回
//        return new PageObject<>(rowCount, records, pageSize, pageCurrent);
        return new PageObject<>((int) page.getTotal(), records, pageSize, pageCurrent);
    }


}
