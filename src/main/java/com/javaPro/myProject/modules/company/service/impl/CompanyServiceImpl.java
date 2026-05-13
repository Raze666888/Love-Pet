package com.javaPro.myProject.modules.company.service.impl;

import com.javaPro.myProject.modules.company.dao.CompanyDao;
import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.company.service.CompanyService;
import com.javaPro.myProject.modules.orderEvalute.service.OrderEvaluteService;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售商表(Company)表服务实现类
 *
 * @author
 * @since 07:49:22
 */
@Service("companyService")
public class CompanyServiceImpl implements CompanyService {
    @Resource
    private CompanyDao companyDao;

    @Resource
    private OrderEvaluteService orderEvaluteService;

    @Resource
    private SysuserService sysuserService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Company queryById(Integer id) {
        Company company = this.companyDao.queryById(id);

        // 填充用户名信息
        if (company != null && company.getCreateid() != null) {
            Sysuser user = sysuserService.queryById(company.getCreateid());
            if (user != null && user.getUsername() != null) {
                company.setUsername(user.getUsername());
            }
        }

        return company;
    }

    /**
     * 分页查询
     *
     * @param company     筛选条件
     * @return 查询结果
     */
    @Override
    public List<Company> queryByPage(Company company) {
        List<Company> companies = this.companyDao.queryAllByLimit(company);

        // 为每个公司填充用户名信息
        for (Company comp : companies) {
            if (comp.getCreateid() != null) {
                Sysuser user = sysuserService.queryById(comp.getCreateid());
                if (user != null && user.getUsername() != null) {
                    comp.setUsername(user.getUsername());
                }
            }
        }

        return companies;
    }

    /**
     * 新增数据
     *
     * @param company 实例对象
     * @return 实例对象
     */
    @Override
    public Company insert(Company company) {
        this.companyDao.insert(company);
        return company;
    }

    @Override
    public Company insertWithId(Company company) {
        this.companyDao.insertWithId(company);
        return company;
    }

    /**
     * 修改数据
     *
     * @param company 实例对象
     * @return 实例对象
     */
    @Override
    public Company update(Company company) {
        this.companyDao.update(company);
        return this.queryById(company.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.companyDao.deleteById(id) > 0;
    }

    @Override
    public Company queryByCreateId(Integer createid) {
        return this.companyDao.queryByCreateId(createid);
    }

    @Override
    public void updateRatingStats(Integer companyid) {
        // 计算平均评分和评价总数
        Double avgRating = orderEvaluteService.getAvgRatingByCompanyId(companyid);
        Integer ratingCount = orderEvaluteService.getRatingCountByCompanyId(companyid);

        // 更新服务商信息
        Company company = this.queryById(companyid);
        if (company != null) {
            company.setAvgRating(avgRating != null ? avgRating : 0.0);
            company.setRatingCount(ratingCount != null ? ratingCount : 0);
            this.update(company);
        }
    }
}
