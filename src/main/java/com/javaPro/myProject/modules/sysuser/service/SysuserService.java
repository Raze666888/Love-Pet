package com.javaPro.myProject.modules.sysuser.service;

import com.javaPro.myProject.modules.sysuser.entity.Sysuser;

import java.util.List;

/**
 * 用户表(Sysuser)表服务接口
 *
 * @author
 * @since 17:36:57
 */
public interface SysuserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Sysuser queryById(Integer id);
    Sysuser queryByAccount(String account);

    /**
     * 分页查询
     *
     * @param sysuser     筛选条件
     * @return 查询结果
     */
    List<Sysuser> queryByPage(Sysuser sysuser);

    /**
     * 新增数据
     *
     * @param sysuser 实例对象
     * @return 实例对象
     */
    Sysuser insert(Sysuser sysuser);

    /**
     * 修改数据
     *
     * @param sysuser 实例对象
     * @return 实例对象
     */
    Sysuser update(Sysuser sysuser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
