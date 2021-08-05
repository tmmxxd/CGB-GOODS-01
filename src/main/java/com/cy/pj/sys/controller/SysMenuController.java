package com.cy.pj.sys.controller;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu/")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;


    @RequestMapping("doFindObjects")
    public JsonResult doFindObjects() {
        return new JsonResult(sysMenuService.findObjects());
    }

    @RequestMapping("doDeleteObject")
    public JsonResult doDeleteObject(Integer id) {
        sysMenuService.deleteObject(id);
        return new JsonResult("delete ok");
    }

    @RequestMapping("doFindZtreeMenuNodes")
    public JsonResult doFindZtreeMenuNodes() {
        return new JsonResult(sysMenuService.findZtreeMenuNodes());
    }
}
