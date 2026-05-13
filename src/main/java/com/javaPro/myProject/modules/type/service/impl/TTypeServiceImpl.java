package com.javaPro.myProject.modules.type.service.impl;

import com.javaPro.myProject.modules.type.dao.TTypeDao;
import com.javaPro.myProject.modules.type.entity.TType;
import com.javaPro.myProject.modules.type.service.TTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章标签表(TType)表服务实现类
 *
 * @author
 * @since 14:21:01
 */
@Service("tTypeService")
public class TTypeServiceImpl implements TTypeService {
    @Resource
    private TTypeDao tTypeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TType queryById(Integer id) {
        return this.tTypeDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tType 筛选条件
     * @return 查询结果
     */
    @Override
    public List<TType> queryByPage(TType tType) {

        return this.tTypeDao.queryAllByLimit(tType);
    }

    /**
     * 新增数据
     *
     * @param tType 实例对象
     * @return 实例对象
     */
    @Override
    public TType insert(TType tType) {
        this.tTypeDao.insert(tType);
        return tType;
    }

    /**
     * 修改数据
     *
     * @param tType 实例对象
     * @return 实例对象
     */
    @Override
    public TType update(TType tType) {
        this.tTypeDao.update(tType);
        return this.queryById(tType.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tTypeDao.deleteById(id) > 0;
    }
}
