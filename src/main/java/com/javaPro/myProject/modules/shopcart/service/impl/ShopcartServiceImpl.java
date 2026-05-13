package com.javaPro.myProject.modules.shopcart.service.impl;

import com.javaPro.myProject.modules.shopcart.dao.ShopcartDao;
import com.javaPro.myProject.modules.shopcart.entity.Shopcart;
import com.javaPro.myProject.modules.shopcart.service.ShopcartService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购物车(Shopcart)表服务实现类
 *
 * @author
 * @since 15:35:15
 */
@Service("shopcartService")
public class ShopcartServiceImpl implements ShopcartService {
    @Resource
    private ShopcartDao shopcartDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Shopcart queryById(Integer id) {
        return this.shopcartDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param shopcart 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Shopcart> queryByPage(Shopcart shopcart) {
        List<Shopcart> shopcarts = this.shopcartDao.queryAllByLimit(shopcart);

        return shopcarts;
    }

    /**
     * 新增数据
     *
     * @param shopcart 实例对象
     * @return 实例对象
     */
    @Override
    public Shopcart insert(Shopcart shopcart) {
        Shopcart param = new Shopcart();
        param.setUserid(shopcart.getUserid());
        param.setProductid(shopcart.getProductid());
        param.setSpare1("0");
        List<Shopcart> shopcarts = shopcartDao.queryAllByLimit(param);
        if (!CollectionUtils.isEmpty(shopcarts)){
            Shopcart item = shopcarts.get(0);
            Integer number = item.getNumber();
            item.setNumber(shopcart.getNumber() + number);
            int update = shopcartDao.update(item);
        }else {
            this.shopcartDao.insert(shopcart);

        }
        return shopcart;
    }

    /**
     * 修改数据
     *
     * @param shopcart 实例对象
     * @return 实例对象
     */
    @Override
    public Shopcart update(Shopcart shopcart) {
        this.shopcartDao.update(shopcart);
        return this.queryById(shopcart.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.shopcartDao.deleteById(id) > 0;
    }
}
