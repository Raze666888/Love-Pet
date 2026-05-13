package com.javaPro.myProject.modules.orderEvalute.service.impl;

import com.javaPro.myProject.modules.orderEvalute.entity.OrderEvalute;
import com.javaPro.myProject.modules.orderEvalute.dao.OrderEvaluteDao;
import com.javaPro.myProject.modules.orderEvalute.service.OrderEvaluteService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

/**
 * 评价表(OrderEvalute)表服务实现类
 */
@Service("orderEvaluteService")
public class OrderEvaluteServiceImpl implements OrderEvaluteService {
    @Resource
    private OrderEvaluteDao orderEvaluteDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public OrderEvalute queryById(Integer id) {
        return this.orderEvaluteDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param orderEvalute 筛选条件
     * @return 查询结果
     */
    @Override
    public List<OrderEvalute> queryByPage(OrderEvalute orderEvalute) {
        return this.orderEvaluteDao.queryAllByLimit(orderEvalute);
    }

    /**
     * 新增数据
     *
     * @param orderEvalute 实例对象
     * @return 实例对象
     */
    @Override
    public OrderEvalute insert(OrderEvalute orderEvalute) {
        this.orderEvaluteDao.insert(orderEvalute);
        return orderEvalute;
    }

    /**
     * 修改数据
     *
     * @param orderEvalute 实例对象
     * @return 实例对象
     */
    @Override
    public OrderEvalute update(OrderEvalute orderEvalute) {
        this.orderEvaluteDao.update(orderEvalute);
        return this.queryById(orderEvalute.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.orderEvaluteDao.deleteById(id) > 0;
    }

    @Override
    public Double getAvgRatingByCompanyId(Integer companyid) {
        return this.orderEvaluteDao.getAvgRatingByCompanyId(companyid);
    }

    @Override
    public Integer getRatingCountByCompanyId(Integer companyid) {
        return this.orderEvaluteDao.getRatingCountByCompanyId(companyid);
    }

    @Override
    public List<OrderEvalute> queryByCompanyId(Integer companyid) {
        return this.orderEvaluteDao.queryByCompanyId(companyid);
    }
}
