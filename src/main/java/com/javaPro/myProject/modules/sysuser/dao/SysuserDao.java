package com.javaPro.myProject.modules.sysuser.dao;

import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表(Sysuser)表数据库访问层
 *
 * @author
 * @since 17:36:57
 */
public interface SysuserDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Sysuser queryById(Integer id);
    Sysuser queryByAccount(String account);

    /**
     * 查询指定行数据
     *
     * @param sysuser 查询条件
     * @return 对象列表
     */
    List<Sysuser> queryAllByLimit(Sysuser sysuser);

    /**
     * 统计总行数
     *
     * @param sysuser 查询条件
     * @return 总行数
     */
    long count(Sysuser sysuser);

    /**
     * 新增数据
     *
     * @param sysuser 实例对象
     * @return 影响行数
     */
    int insert(Sysuser sysuser);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Sysuser> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Sysuser> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Sysuser> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Sysuser> entities);

    /**
     * 修改数据
     *
     * @param sysuser 实例对象
     * @return 影响行数
     */
    int update(Sysuser sysuser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

