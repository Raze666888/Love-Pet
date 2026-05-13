package com.javaPro.myProject.modules.order.service;

import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.modules.order.entity.Order;

import java.util.List;

/**
 * 订单表(Order)表服务接口
 *
 * @author
 * @since 21:57:54
 */
public interface OrderService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Order queryById(Integer id);

    /**
     * 分页查询
     *
     * @param order 筛选条件
     * @return 查询结果
     */
    List<Order> queryByPage(Order order);

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    AjaxResult insert(Order order);

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    Order update(Order order);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);
    AjaxResult statistics();
}
