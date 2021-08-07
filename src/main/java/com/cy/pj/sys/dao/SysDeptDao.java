package com.cy.pj.sys.dao;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysDept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysDeptDao {
    List<Map<String, Object>> findObjects();

    @Select("select id,name,parentId from sys_depts")
    List<Node> findZtreeMenuNodes();

    int insertObject(SysDept entity);

    int updateObject(SysDept entity);

    @Select("select count(*) from sys_depts where parentId = #{id}")
    int getChildCount(@Param("id") Integer id);

    @Delete("delete from sys_depts where id= #{id}")
    int deleteObject(Integer id);

    SysDept findById(@Param("id") Integer id);
}
