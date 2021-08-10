package com.cy.pj.sys.dao;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserDao {


    /**
     *基于用户名(此名字来自于客户端用户的输入)查找用户对象
     */
    SysUser findUserByUserName(String username);
    /**
     * 基于指定列进行记录统计
     *
     * @param columnName  列名
     * @param columnValue 列值
     */
    int isExist(@Param("columnName") String columnName, @Param("columnValue") String columnValue);

    /**
     * 禁用或启用用户对象
     */
    int validById(@Param("id") Integer id, @Param("valid") Integer valid, @Param("modifiedUser") String modifiedUser);

    /**
     * 基于查询条件动态拼接sql,获取 用户行为日志.
     * 1)username值为null或者""应查询所有用户的行为日志
     * 2)username值不null或""应按输入的用户进行模糊查询.
     *
     * @return 查询到的记录总数
     */
    int getRowCount(@Param("username") String username);

    /**
     * 基于查询条件,获取当前页要呈现的记录信息
     *
     * @param username   查询条件
     * @param startIndex 起始位置
     * @param pageSize   页面大小(每页最多显示多少条记录)
     * @return 当前页的记录
     */
    List<SysUserDeptVo> findPageObjects(
            @Param("username") String username,
            @Param("startIndex") Integer startIndex,
            @Param("pageSize") Integer pageSize);


    /**
     * 插入用户自身信息
     */
    int insertObject(SysUser entity);

    SysUserDeptVo findObjectById(Integer id);

    int updateObject(SysUser entity);
}
