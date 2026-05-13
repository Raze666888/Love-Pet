package com.javaPro.myProject.modules.shopcart.service;

import com.javaPro.myProject.modules.shopcart.entity.Shopcart;

import java.util.List;

/**
 * 购物车(Shopcart)表服务接口
 *
 * @author
 * @since 15:35:15
 */
public interface ShopcartService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Shopcart queryById(Integer id);

    /**
     * 分页查询
     *
     * @param shopcart 筛选条件
     * @return 查询结果
     */
    List<Shopcart> queryByPage(Shopcart shopcart);

    /**
     * 新增数据
     *
     * @param shopcart 实例对象
     * @return 实例对象
     */
    Shopcart insert(Shopcart shopcart);

    /**
     * 修改数据
     *
     * @param shopcart 实例对象
     * @return 实例对象
     */
    Shopcart update(Shopcart shopcart);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
