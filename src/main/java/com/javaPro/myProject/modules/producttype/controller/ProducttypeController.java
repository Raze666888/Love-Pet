package com.javaPro.myProject.modules.producttype.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.producttype.entity.Producttype;
import com.javaPro.myProject.modules.producttype.service.ProducttypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 宠物服务类型表(Producttype)表控制层
 */
@RestController
@RequestMapping("producttype")
public class ProducttypeController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private ProducttypeService producttypeService;

    /**
     * 分页查询
     *
     * @param producttype 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Producttype producttype) {
        startPage();
        return getList(this.producttypeService.queryByPage(producttype));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.producttypeService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param producttype 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Producttype producttype) {
        return AjaxResult.ok(this.producttypeService.insert(producttype));
    }

    /**
     * 编辑数据
     *
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Producttype producttype) {
        return AjaxResult.ok(this.producttypeService.update(producttype));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.producttypeService.deleteById(id));
    }

}

