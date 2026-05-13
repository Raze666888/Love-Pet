package com.javaPro.myProject.modules.producttype.service.impl;

import com.javaPro.myProject.modules.producttype.dao.ProducttypeDao;
import com.javaPro.myProject.modules.producttype.entity.Producttype;
import com.javaPro.myProject.modules.producttype.service.ProducttypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 宠物服务类型表(Producttype)表服务实现类
 *
 * @author AjaxResult
 * @since AjaxResult 08:07:42
 */
@Service("producttypeService")
public class ProducttypeServiceImpl implements ProducttypeService {
    @Resource
    private ProducttypeDao producttypeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Producttype queryById(Integer id) {
        return this.producttypeDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param producttype 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Producttype> queryByPage(Producttype producttype) {

        return this.producttypeDao.queryAllByLimit(producttype);
    }

    /**
     * 新增数据
     *
     * @param producttype 实例对象
     * @return 实例对象
     */
    @Override
    public Producttype insert(Producttype producttype) {
        this.producttypeDao.insert(producttype);
        return producttype;
    }

    /**
     * 修改数据
     *
     * @param producttype 实例对象
     * @return 实例对象
     */
    @Override
    public Producttype update(Producttype producttype) {
        this.producttypeDao.update(producttype);
        return this.queryById(producttype.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.producttypeDao.deleteById(id) > 0;
    }
}
