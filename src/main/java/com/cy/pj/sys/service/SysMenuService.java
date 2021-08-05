package com.cy.pj.sys.service;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;

import java.util.List;
import java.util.Map;

/**
 * 菜单模块的业务接口
 */
public interface SysMenuService {

    /**
     * 查询所有菜单以及带单对应的上级菜单
     *
     * @return
     */
    List<Map<String, Object>> findObjects();

    int deleteObject(Integer id);

    List<Node> findZtreeMenuNodes();

    int saveObject(SysMenu entity);

    int updateObject(SysMenu entity);
}
