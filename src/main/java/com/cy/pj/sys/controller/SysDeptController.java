package com.cy.pj.sys.controller;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysDept;
import com.cy.pj.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dept/")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @RequestMapping("doFindObjects")
    public JsonResult doFindObjects() {
        return new JsonResult(sysDeptService.findObjects());
    }

    @RequestMapping("doDeleteObject")
    public JsonResult doDeleteObject(Integer id) {
        sysDeptService.deleteObject(id);
        return new JsonResult("delete ok");
    }

    @RequestMapping("doFindZTreeNodes")
    public JsonResult doFindZTreeNodes() {
        return new JsonResult(sysDeptService.findZtreeDeptNodes());
    }

    @RequestMapping("doSaveObject")
    public JsonResult doSaveObject(SysDept entity) {
        sysDeptService.saveObject(entity);
        return new JsonResult("save ok");
    }

    @RequestMapping("doUpdateObject")
    public JsonResult doUpdateObject(SysDept entity) {
        sysDeptService.updateObject(entity);
        return new JsonResult("update ok");
    }
}
