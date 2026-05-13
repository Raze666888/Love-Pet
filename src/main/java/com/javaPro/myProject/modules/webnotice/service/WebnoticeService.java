package com.javaPro.myProject.modules.webnotice.service;

import com.javaPro.myProject.modules.webnotice.entity.Webnotice;

import java.util.List;

/**
 * 公告(Webnotice)表服务接口
 *
 * @author
 * @since 00:36:33
 */
public interface WebnoticeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Webnotice queryById(Integer id);

    /**
     * 分页查询
     *
     * @param webnotice 筛选条件
     * @return 查询结果
     */
    List<Webnotice> queryByPage(Webnotice webnotice);

    /**
     * 新增数据
     *
     * @param webnotice 实例对象
     * @return 实例对象
     */
    Webnotice insert(Webnotice webnotice);

    /**
     * 修改数据
     *
     * @param webnotice 实例对象
     * @return 实例对象
     */
    Webnotice update(Webnotice webnotice);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
