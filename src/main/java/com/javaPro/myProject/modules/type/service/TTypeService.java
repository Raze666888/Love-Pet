package com.javaPro.myProject.modules.type.service;

import com.javaPro.myProject.modules.type.entity.TType;

import java.util.List;

/**
 * 文章标签表(TType)表服务接口
 *
 * @author
 * @since 14:21:01
 */
public interface TTypeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TType queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tType       筛选条件
     * @return 查询结果
     */
    List<TType> queryByPage(TType tType);

    /**
     * 新增数据
     *
     * @param tType 实例对象
     * @return 实例对象
     */
    TType insert(TType tType);

    /**
     * 修改数据
     *
     * @param tType 实例对象
     * @return 实例对象
     */
    TType update(TType tType);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
