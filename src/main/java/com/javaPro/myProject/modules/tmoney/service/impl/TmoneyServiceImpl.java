package com.javaPro.myProject.modules.tmoney.service.impl;

import com.javaPro.myProject.modules.sysuser.dao.SysuserDao;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.tmoney.dao.TmoneyDao;
import com.javaPro.myProject.modules.tmoney.entity.Tmoney;
import com.javaPro.myProject.modules.tmoney.service.TmoneyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 充值表(Tmoney)表服务实现类
 *
 * @author
 * @since 18:40:27
 */
@Service("tmoneyService")
public class TmoneyServiceImpl implements TmoneyService {
    @Resource
    private TmoneyDao tmoneyDao;
    @Resource
    private SysuserDao sysuserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Tmoney queryById(Integer id) {
        return this.tmoneyDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tmoney 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Tmoney> queryByPage(Tmoney tmoney) {

        return this.tmoneyDao.queryAllByLimit(tmoney);
    }

    /**
     * 新增数据
     *
     * @param tmoney 实例对象
     * @return 实例对象
     */
    @Override
    public Tmoney insert(Tmoney tmoney) {
        this.tmoneyDao.insert(tmoney);
        return tmoney;
    }

    /**
     * 修改数据
     *
     * @param tmoney 实例对象
     * @return 实例对象
     */
    @Override
    public Tmoney update(Tmoney tmoney) {
        if (tmoney.getAuditstatus().equals("已审核")){
            Sysuser sysuser = sysuserDao.queryById(tmoney.getUserid());
            Double money = sysuser.getMoney();
            money = money + Double.parseDouble(tmoney.getMoney());
            sysuser.setMoney( money);
            sysuserDao.update(sysuser);
        }
        this.tmoneyDao.update(tmoney);
        return this.queryById(tmoney.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tmoneyDao.deleteById(id) > 0;
    }
}
