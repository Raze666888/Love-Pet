package com.javaPro.myProject.modules.order.dao;

import com.javaPro.myProject.modules.order.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单表(Order)表数据库访问层
 *
 * @author
 * @since 21:57:53
 */
public interface OrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Order queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param order 查询条件
     * @return 对象列表
     */
    List<Order> queryAllByLimit(Order order);

    /**
     * 统计总行数
     *
     * @param order 查询条件
     * @return 总行数
     */
    long count(Order order);

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 影响行数
     */
    int insert(Order order);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Order> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Order> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Order> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Order> entities);

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 影响行数
     */
    int update(Order order);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据服务商ID统计订单数量
     *
     * @param companyId 服务商ID
     * @return 订单数量
     */
    Long countByCompanyId(@Param("companyId") Integer companyId);

    /**
     * 根据服务商ID获取总收入
     *
     * @param companyId 服务商ID
     * @return 总收入
     */
    Double getTotalRevenueByCompanyId(@Param("companyId") Integer companyId);

    /**
     * 根据日期和服务商ID获取订单数量
     *
     * @param date 日期
     * @param companyId 服务商ID
     * @return 订单数量
     */
    Long getOrderCountByDateAndCompanyId(@Param("date") Date date, @Param("companyId") Integer companyId);

    /**
     * 获取服务商最近订单
     *
     * @param companyId 服务商ID
     * @param limit 限制数量
     * @return 最近订单列表
     */
    List<Map<String, Object>> getRecentOrdersByCompanyId(@Param("companyId") Integer companyId, @Param("limit") Integer limit);

    /**
     * 根据产品ID统计历史订单数量（包含购买数量）
     *
     * @param productId 产品ID
     * @return 历史订单总数量
     */
    Long getOrderQuantityByProductId(@Param("productId") Integer productId);

    /**
     * 根据用户ID和产品ID统计该用户对该产品的订单数量（包含购买数量）
     *
     * @param userId 用户ID
     * @param productId 产品ID
     * @return 该用户对该产品的订单总数量
     */
    Long getOrderQuantityByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

}

