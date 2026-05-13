package com.javaPro.myProject.modules.company.service;

import com.javaPro.myProject.modules.company.entity.Company;

import java.util.List;

/**
 * 销售商表(Company)表服务接口
 *
 * @author
 * @since 07:49:22
 */
public interface CompanyService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Company queryById(Integer id);

    /**
     * 分页查询
     *
     * @param company 筛选条件
     * @return 查询结果
     */
    List<Company> queryByPage(Company company);

    /**
     * 新增数据
     *
     * @param company 实例对象
     * @return 实例对象
     */
    Company insert(Company company);

    /**
     * 新增指定ID的数据
     *
     * @param company 实例对象
     * @return 实例对象
     */
    Company insertWithId(Company company);

    /**
     * 修改数据
     *
     * @param company 实例对象
     * @return 实例对象
     */
    Company update(Company company);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 根据创建者ID查询服务商信息
     *
     * @param createid 创建者ID（用户ID）
     * @return 服务商信息
     */
    Company queryByCreateId(Integer createid);

    /**
     * 更新服务商评分统计
     *
     * @param companyid 服务商ID
     */
    void updateRatingStats(Integer companyid);

}
