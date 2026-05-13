package com.javaPro.myProject.modules.webnotice.dao;

import com.javaPro.myProject.modules.webnotice.entity.Webnotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告(Webnotice)表数据库访问层
 *
 * @author
 * @since 00:36:32
 */
public interface WebnoticeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Webnotice queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param webnotice 查询条件
     * @return 对象列表
     */
    List<Webnotice> queryAllByLimit(Webnotice webnotice);

    /**
     * 统计总行数
     *
     * @param webnotice 查询条件
     * @return 总行数
     */
    long count(Webnotice webnotice);

    /**
     * 新增数据
     *
     * @param webnotice 实例对象
     * @return 影响行数
     */
    int insert(Webnotice webnotice);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Webnotice> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Webnotice> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Webnotice> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Webnotice> entities);

    /**
     * 修改数据
     *
     * @param webnotice 实例对象
     * @return 影响行数
     */
    int update(Webnotice webnotice);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

