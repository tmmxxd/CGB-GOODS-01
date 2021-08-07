package com.cy.pj.sys.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 基于此对象封装角色修改页面上要呈现的数据
 * 1)角色自身信息:id name note    (来自角色表sys_roles)
 * 2)角色对应的菜单信息:menuId       (来自角色菜单关系表sys_role_menus)
 * 数据应该来自数据库的查询,具体方案
 * 1)单表查询:(在业务层执行两次数据查询操作,最终在业务层进行数据封装)
 * 2)嵌套查询:(数据库端可能要发两次sql)
 * 3)多表查询:(sys_role 关联sys_role_menus
 */
@Data
public class SysRoleMenuVo implements Serializable {

    private static final long serialVersionUID = 1334915952933015337L;
    /**
     * 角色id
     */
    private Integer id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色备注
     */
    private String note;
    /**
     * 角色对应的菜单id
     */
    private List<Integer> menuIds;
}
