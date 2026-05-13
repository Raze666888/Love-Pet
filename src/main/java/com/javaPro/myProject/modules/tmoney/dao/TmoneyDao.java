package com.javaPro.myProject.modules.tmoney.dao;

import com.javaPro.myProject.modules.tmoney.entity.Tmoney;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 充值表(Tmoney)表数据库访问层
 *
 * @author
 * @since 18:40:26
 */
public interface TmoneyDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Tmoney queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tmoney 查询条件
     * @return 对象列表
     */
    List<Tmoney> queryAllByLimit(Tmoney tmoney);

    /**
     * 统计总行数
     *
     * @param tmoney 查询条件
     * @return 总行数
     */
    long count(Tmoney tmoney);

    /**
     * 新增数据
     *
     * @param tmoney 实例对象
     * @return 影响行数
     */
    int insert(Tmoney tmoney);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Tmoney> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Tmoney> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Tmoney> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Tmoney> entities);

    /**
     * 修改数据
     *
     * @param tmoney 实例对象
     * @return 影响行数
     */
    int update(Tmoney tmoney);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

