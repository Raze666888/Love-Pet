package com.javaPro.myProject.modules.sysuser.service.impl;

import com.javaPro.myProject.modules.sysuser.dao.SysuserDao;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表(Sysuser)表服务实现类
 *
 * @author
 * @since 17:36:57
 */
@Service("sysuserService")
public class SysuserServiceImpl implements SysuserService {
    @Resource
    private SysuserDao sysuserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Sysuser queryById(Integer id) {
        return this.sysuserDao.queryById(id);
    }
    @Override
    public Sysuser queryByAccount(String account) {
        return this.sysuserDao.queryByAccount(account);
    }

    /**
     * 分页查询
     *
     * @param sysuser     筛选条件
     * @return 查询结果
     */
    @Override
    public List<Sysuser> queryByPage(Sysuser sysuser) {

        return this.sysuserDao.queryAllByLimit(sysuser);
    }

    /**
     * 新增数据
     *
     * @param sysuser 实例对象
     * @return 实例对象
     */
    @Override
    public Sysuser insert(Sysuser sysuser) {
        this.sysuserDao.insert(sysuser);
        return sysuser;
    }

    /**
     * 修改数据
     *
     * @param sysuser 实例对象
     * @return 实例对象
     */
    @Override
    public Sysuser update(Sysuser sysuser) {
        this.sysuserDao.update(sysuser);
        return this.queryById(sysuser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.sysuserDao.deleteById(id) > 0;
    }
}
