package com.javaPro.myProject.modules.userlike.service;

import com.javaPro.myProject.modules.userlike.entity.Userlike;

import java.util.List;

/**
 * 用户收藏(Userlike)表服务接口
 *
 * @author AjaxResult
 * @since AjaxResult 18:54:42
 */
public interface UserlikeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Userlike queryById(Integer id);

    /**
     * 分页查询
     *
     * @param userlike 筛选条件
     * @return 查询结果
     */
    List<Userlike> queryByPage(Userlike userlike);

    /**
     * 新增数据
     *
     * @param userlike 实例对象
     * @return 实例对象
     */
    Userlike insert(Userlike userlike);

    /**
     * 修改数据
     *
     * @param userlike 实例对象
     * @return 实例对象
     */
    Userlike update(Userlike userlike);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);
    boolean delete(Userlike userlike);

}
