package com.javaPro.myProject.modules.producttype.dao;

import com.javaPro.myProject.modules.producttype.entity.Producttype;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宠物服务类型表(Producttype)表数据库访问层
 *
 * @author AjaxResult
 * @since AjaxResult 08:07:42
 */
public interface ProducttypeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Producttype queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param producttype 查询条件
     * @return 对象列表
     */
    List<Producttype> queryAllByLimit(Producttype producttype);

    /**
     * 统计总行数
     *
     * @param producttype 查询条件
     * @return 总行数
     */
    long count(Producttype producttype);

    /**
     * 新增数据
     *
     * @param producttype 实例对象
     * @return 影响行数
     */
    int insert(Producttype producttype);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Producttype> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Producttype> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Producttype> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Producttype> entities);

    /**
     * 修改数据
     *
     * @param producttype 实例对象
     * @return 影响行数
     */
    int update(Producttype producttype);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

