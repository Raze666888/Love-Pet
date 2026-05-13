package com.javaPro.myProject.modules.userlike.dao;

import com.javaPro.myProject.modules.userlike.entity.Userlike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户收藏(Userlike)表数据库访问层
 *
 * @author AjaxResult
 * @since AjaxResult 18:54:41
 */
public interface UserlikeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Userlike queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param userlike 查询条件
     * @return 对象列表
     */
    List<Userlike> queryAllByLimit(Userlike userlike);

    /**
     * 统计总行数
     *
     * @param userlike 查询条件
     * @return 总行数
     */
    long count(Userlike userlike);

    /**
     * 新增数据
     *
     * @param userlike 实例对象
     * @return 影响行数
     */
    int insert(Userlike userlike);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Userlike> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Userlike> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Userlike> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Userlike> entities);

    /**
     * 修改数据
     *
     * @param userlike 实例对象
     * @return 影响行数
     */
    int update(Userlike userlike);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);
    int delete(Userlike userlike);

}

