package com.javaPro.myProject.modules.type.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.type.entity.TType;
import com.javaPro.myProject.modules.type.service.TTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文章标签表(TType)表控制层
 *
 * @author
 * @since 14:21:00
 */
@RestController
@RequestMapping("tType")
public class TTypeController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private TTypeService tTypeService;

    /**
     * 分页查询
     *
     * @param tType 筛选条件
     * @return 查询结果
     */
    @GetMapping("list")
    public ListByPage queryByPage(TType tType) {
        startPage();
        return getList(this.tTypeService.queryByPage(tType));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.tTypeService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param tType 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public AjaxResult add(@RequestBody TType tType) {
        return AjaxResult.ok(this.tTypeService.insert(tType));
    }

    /**
     * 编辑数据
     *
     * @param tType 实体
     * @return 编辑结果
     */
    @PutMapping("edit")
    public AjaxResult edit(@RequestBody TType tType) {
        return AjaxResult.ok(this.tTypeService.update(tType));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("del")
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.tTypeService.deleteById(id));
    }

}

