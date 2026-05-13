package com.javaPro.myProject.modules.product.service;

import com.javaPro.myProject.modules.product.entity.Product;

import java.util.List;

/**
 * 宠物服务表(Product)表服务接口
 *
 * @author AjaxResult
 * @since AjaxResult 07:51:55
 */
public interface ProductService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Product queryById(Integer id);

    /**
     * 通过ID查询单条数据（支持用户ID参数用于计算个人销量）
     *
     * @param id 主键
     * @param userid 用户ID（可选）
     * @return 实例对象
     */
    Product queryById(Integer id, Integer userid);

    /**
     * 分页查询
     *
     * @param product 筛选条件
     * @return 查询结果
     */
    List<Product> queryByPage(Product product);

    /**
     * 新增数据
     *
     * @param product 实例对象
     * @return 实例对象
     */
    Product insert(Product product);

    /**
     * 修改数据
     *
     * @param product 实例对象
     * @return 实例对象
     */
    Product update(Product product);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 更新产品服务时间段数据（用于测试）
     *
     * @return 更新的记录数
     */
    int updateServiceTimeForTesting();

}
