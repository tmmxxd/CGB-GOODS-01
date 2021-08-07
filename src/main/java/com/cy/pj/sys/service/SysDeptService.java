package com.cy.pj.sys.service;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysDept;

import java.util.List;
import java.util.Map;

public interface SysDeptService {
    /**
     * 查询所有部门以及带单对应的上级部门
     */
    List<Map<String, Object>> findObjects();

    int deleteObject(Integer id);

    List<Node> findZtreeDeptNodes();

    int saveObject(SysDept entity);

    int updateObject(SysDept entity);
}
