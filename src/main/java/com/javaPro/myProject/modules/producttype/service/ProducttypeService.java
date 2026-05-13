package com.javaPro.myProject.modules.producttype.service;

import com.javaPro.myProject.modules.producttype.entity.Producttype;

import java.util.List;

/**
 * 宠物服务类型表(Producttype)表服务接口
 *
 * @author AjaxResult
 * @since AjaxResult 08:07:42
 */
public interface ProducttypeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Producttype queryById(Integer id);

    /**
     * 分页查询
     *
     * @param producttype 筛选条件
     * @return 查询结果
     */
    List<Producttype> queryByPage(Producttype producttype);

    /**
     * 新增数据
     *
     * @param producttype 实例对象
     * @return 实例对象
     */
    Producttype insert(Producttype producttype);

    /**
     * 修改数据
     *
     * @param producttype 实例对象
     * @return 实例对象
     */
    Producttype update(Producttype producttype);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
