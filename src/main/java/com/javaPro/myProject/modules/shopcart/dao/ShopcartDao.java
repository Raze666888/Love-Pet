package com.javaPro.myProject.modules.shopcart.dao;

import com.javaPro.myProject.modules.shopcart.entity.Shopcart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车(Shopcart)表数据库访问层
 *
 * @author
 * @since 15:35:15
 */
public interface ShopcartDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Shopcart queryById(Integer id);
    Shopcart queryProductByCartId(String id);

    /**
     * 查询指定行数据
     *
     * @param shopcart 查询条件
     * @return 对象列表
     */
    List<Shopcart> queryAllByLimit(Shopcart shopcart);

    /**
     * 统计总行数
     *
     * @param shopcart 查询条件
     * @return 总行数
     */
    long count(Shopcart shopcart);

    /**
     * 新增数据
     *
     * @param shopcart 实例对象
     * @return 影响行数
     */
    int insert(Shopcart shopcart);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Shopcart> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Shopcart> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Shopcart> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Shopcart> entities);

    /**
     * 修改数据
     *
     * @param shopcart 实例对象
     * @return 影响行数
     */
    int update(Shopcart shopcart);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

