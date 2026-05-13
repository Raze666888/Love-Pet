package com.javaPro.myProject.modules.webnotice.service.impl;

import com.javaPro.myProject.modules.webnotice.dao.WebnoticeDao;
import com.javaPro.myProject.modules.webnotice.entity.Webnotice;
import com.javaPro.myProject.modules.webnotice.service.WebnoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告(Webnotice)表服务实现类
 *
 * @author
 * @since 00:36:33
 */
@Service("webnoticeService")
public class WebnoticeServiceImpl implements WebnoticeService {
    @Resource
    private WebnoticeDao webnoticeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Webnotice queryById(Integer id) {
        return this.webnoticeDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param webnotice 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Webnotice> queryByPage(Webnotice webnotice) {

        return this.webnoticeDao.queryAllByLimit(webnotice);
    }

    /**
     * 新增数据
     *
     * @param webnotice 实例对象
     * @return 实例对象
     */
    @Override
    public Webnotice insert(Webnotice webnotice) {
        this.webnoticeDao.insert(webnotice);
        return webnotice;
    }

    /**
     * 修改数据
     *
     * @param webnotice 实例对象
     * @return 实例对象
     */
    @Override
    public Webnotice update(Webnotice webnotice) {
        this.webnoticeDao.update(webnotice);
        return this.queryById(webnotice.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.webnoticeDao.deleteById(id) > 0;
    }
}
