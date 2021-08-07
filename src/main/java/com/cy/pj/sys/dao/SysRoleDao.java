package com.cy.pj.sys.dao;

import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleDao {

    SysRoleMenuVo findObjectById(Integer id);

    /**
     * 写入角色自身信息
     */
    int insertObject(SysRole entity);

    int getRowCount(@Param("name") String name);

    List<SysRole> findPageObjects(@Param("name") String name,
                                  @Param("startIndex") Integer startIndex,
                                  @Param("pageSize") Integer pageSize);

    @Delete("delete from sys_roles where id=#{id}")
    int deleteObject(Integer id);

    int updateObject(SysRole entity);
}
