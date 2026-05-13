package com.javaPro.myProject.modules.shopcart.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.shopcart.entity.Shopcart;
import com.javaPro.myProject.modules.shopcart.service.ShopcartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 购物车(Shopcart)表控制层
 *
 * @author
 * @since 15:35:15
 */
@RestController
@RequestMapping("shopcart")
public class ShopcartController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private ShopcartService shopcartService;

    /**
     * 分页查询
     *
     * @param shopcart 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Shopcart shopcart) {
        startPage();
        return getList(this.shopcartService.queryByPage(shopcart)) ;
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.shopcartService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param shopcart 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add( @RequestBody Shopcart shopcart) {
        return AjaxResult.ok(this.shopcartService.insert(shopcart));
    }

    /**
     * 编辑数据
     *
     * @param shopcart 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit( @RequestBody Shopcart shopcart) {
        return AjaxResult.ok(this.shopcartService.update(shopcart));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.shopcartService.deleteById(id));
    }

}

