package com.javaPro.myProject.modules.orderEvalute.service;

import com.javaPro.myProject.modules.orderEvalute.entity.OrderEvalute;

import java.util.List;

/**
 * 评价表(OrderEvalute)表服务接口
 */
public interface OrderEvaluteService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OrderEvalute queryById(Integer id);

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    List<OrderEvalute> queryByPage(OrderEvalute orderEvalute);

    /**
     * 新增数据
     *
     * @param orderEvalute 实例对象
     * @return 实例对象
     */
    OrderEvalute insert(OrderEvalute orderEvalute);

    /**
     * 修改数据
     *
     * @param orderEvalute 实例对象
     * @return 实例对象
     */
    OrderEvalute update(OrderEvalute orderEvalute);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 计算服务商的平均评分
     *
     * @param companyid 服务商ID
     * @return 平均评分
     */
    Double getAvgRatingByCompanyId(Integer companyid);

    /**
     * 获取服务商的评价总数
     *
     * @param companyid 服务商ID
     * @return 评价总数
     */
    Integer getRatingCountByCompanyId(Integer companyid);

    /**
     * 根据服务商ID查询评价列表
     *
     * @param companyid 服务商ID
     * @return 评价列表
     */
    List<OrderEvalute> queryByCompanyId(Integer companyid);

}
