package com.javaPro.myProject.modules.tmoney.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.tmoney.entity.Tmoney;
import com.javaPro.myProject.modules.tmoney.service.TmoneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 充值表(Tmoney)表控制层
 *
 * @author
 * @since 18:40:26
 */
@RestController
@RequestMapping("tmoney")
public class TmoneyController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private TmoneyService tmoneyService;

    /**
     * 分页查询
     *
     * @param tmoney 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Tmoney tmoney) {
        startPage();
        return getList(this.tmoneyService.queryByPage(tmoney));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public AjaxResult queryById(@PathVariable("id") Integer id) {
        return AjaxResult.ok(this.tmoneyService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param tmoney 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add( @RequestBody Tmoney tmoney) {
        return AjaxResult.ok(this.tmoneyService.insert(tmoney));
    }

    /**
     * 编辑数据
     *
     * @param tmoney 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Tmoney tmoney) {
        return AjaxResult.ok(this.tmoneyService.update(tmoney));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.tmoneyService.deleteById(id));
    }

}

