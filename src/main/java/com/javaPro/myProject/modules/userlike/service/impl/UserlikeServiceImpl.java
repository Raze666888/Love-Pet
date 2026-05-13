package com.javaPro.myProject.modules.userlike.service.impl;

import com.javaPro.myProject.modules.userlike.dao.UserlikeDao;
import com.javaPro.myProject.modules.userlike.entity.Userlike;
import com.javaPro.myProject.modules.userlike.service.UserlikeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户收藏(Userlike)表服务实现类
 *
 * @author AjaxResult
 * @since AjaxResult 18:54:42
 */
@Service("userlikeService")
public class UserlikeServiceImpl implements UserlikeService {
    @Resource
    private UserlikeDao userlikeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Userlike queryById(Integer id) {
        return this.userlikeDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param userlike 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Userlike> queryByPage(Userlike userlike) {
        return this.userlikeDao.queryAllByLimit(userlike);
    }

    /**
     * 新增数据
     *
     * @param userlike 实例对象
     * @return 实例对象
     */
    @Override
    public Userlike insert(Userlike userlike) {
        this.userlikeDao.insert(userlike);
        return userlike;
    }

    /**
     * 修改数据
     *
     * @param userlike 实例对象
     * @return 实例对象
     */
    @Override
    public Userlike update(Userlike userlike) {
        this.userlikeDao.update(userlike);
        return this.queryById(userlike.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.userlikeDao.deleteById(id) > 0;
    }
    @Override
    public boolean delete(Userlike userlike) {
        return this.userlikeDao.delete(userlike) > 0;
    }
}
