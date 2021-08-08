package com.cy.pj.sys.dao;

import com.cy.pj.sys.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定义日志数据层接口:SysLogDao
 * 在此接口中定义访问用户行为日志的规范.
 */
@Mapper
public interface SysLogDao {//DAO


    /**
     * 将用户行为日志写入到数据库
     */
    int insertObject(SysLog entity);


    /**
     * 基于查询条件动态拼接sql,获取 用户行为日志.
     * 1)username值为null或者""应查询所有用户的行为日志
     * 2)username值不null或""应按输入的用户进行模糊查询.
     * @return 查询到的记录总数
     */
//	int getRowCount(@Param("username")String username);

    /**
     * 基于查询条件,获取当前页要呈现的记录信息
     *
     * @param username   查询条件
     * @param startIndex 起始位置
     * @param pageSize   页面大小(每页最多显示多少条记录)
     * @return 当前页的记录
     * <p>
     * 底层:sqlSession.selectList(statement,args)
     * 拦截器:interceptor---->select * from sys_logs order by createTime desc
     * 修改sql行为
     * 1)添加sql:select count(0) from sys_logs
     * 2)修改sql:select * from sys_logs order by createTime limit ?,?
     */
//    List<SysLog> findPageObjects(@Param("username") String username, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
    List<SysLog> findPageObjects(@Param("username") String username);

    int deleteObjects(@Param("ids") Integer... ids);

}
