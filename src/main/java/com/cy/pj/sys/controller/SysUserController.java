package com.cy.pj.sys.controller;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user/")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("doFindPageObjects")
    public JsonResult doFindPageObjects(String username, Integer pageCurrent) {
        return new JsonResult(sysUserService.findPageObjects(username, pageCurrent));
    }

    @RequestMapping("doValidById")
    public JsonResult doValidById(Integer id, Integer valid) {
        sysUserService.validById(id, valid);
        return new JsonResult("update ok");
    }

    @RequestMapping("isExist")
    public JsonResult isExist(String columnName, String columnValue) {
        boolean flag = sysUserService.isExist(columnName, columnValue);
        return new JsonResult(flag);
    }

    @RequestMapping("doSaveObject")
    public JsonResult doSaveObject(SysUser entity, Integer[] roleIds) {
        sysUserService.saveObject(entity, roleIds);
        return new JsonResult("save ok");
    }

    @RequestMapping("doFindObjectById")
    public JsonResult doFindObjectById(Integer id) {
        Map<String, Object> map = sysUserService.findObjectById(id);
        return new JsonResult(map);
    }

    @RequestMapping("doUpdateObject")
    public JsonResult doUpdateObject(
            SysUser entity, Integer[] roleIds) {
        sysUserService.updateObject(entity, roleIds);
        return new JsonResult("update ok");
    }
}
