package com.javaPro.myProject.modules.type.dao;

import com.javaPro.myProject.modules.type.entity.TType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章标签表(TType)表数据库访问层
 *
 * @author
 * @since 14:21:00
 */
public interface TTypeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TType queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tType 查询条件
     * @return 对象列表
     */
    List<TType> queryAllByLimit(TType tType);

    /**
     * 统计总行数
     *
     * @param tType 查询条件
     * @return 总行数
     */
    long count(TType tType);

    /**
     * 新增数据
     *
     * @param tType 实例对象
     * @return 影响行数
     */
    int insert(TType tType);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TType> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TType> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TType> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TType> entities);

    /**
     * 修改数据
     *
     * @param tType 实例对象
     * @return 影响行数
     */
    int update(TType tType);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

