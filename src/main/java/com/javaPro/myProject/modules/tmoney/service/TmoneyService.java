package com.javaPro.myProject.modules.tmoney.service;

import com.javaPro.myProject.modules.tmoney.entity.Tmoney;

import java.util.List;

/**
 * 充值表(Tmoney)表服务接口
 *
 * @author
 * @since 18:40:27
 */
public interface TmoneyService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Tmoney queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tmoney 筛选条件
     * @return 查询结果
     */
    List<Tmoney> queryByPage(Tmoney tmoney);

    /**
     * 新增数据
     *
     * @param tmoney 实例对象
     * @return 实例对象
     */
    Tmoney insert(Tmoney tmoney);

    /**
     * 修改数据
     *
     * @param tmoney 实例对象
     * @return 实例对象
     */
    Tmoney update(Tmoney tmoney);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
