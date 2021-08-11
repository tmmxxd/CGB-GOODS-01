package com.cy.pj.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 基于此对象封装用户菜单信息(用户具备访问权限的菜单)
 */
@Data
public class SysUserMenuVo implements Serializable {

    private static final long serialVersionUID = 6442031841914436442L;
    
    private Integer id;
    private String name;
    private String url;
    private List<SysUserMenuVo> childs;
}
