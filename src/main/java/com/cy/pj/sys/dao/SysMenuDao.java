package com.cy.pj.sys.dao;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface SysMenuDao {

    List<Map<String, Object>> findObjects();

    @Select("select count(*) from sys_menus where parentId = #{id}")
    int getChildCount(@Param("id") Integer id);

    @Delete("delete from sys_menus where id= #{id}")
    int deleteObject(Integer id);

    @Select("select id,name,parentId from sys_menus")
    List<Node> findZtreeMenuNodes();

    int insertObject(SysMenu entity);

    int updateObject(SysMenu entity);
}
