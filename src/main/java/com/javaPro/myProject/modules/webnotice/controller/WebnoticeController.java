package com.javaPro.myProject.modules.webnotice.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.webnotice.entity.Webnotice;
import com.javaPro.myProject.modules.webnotice.service.WebnoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 公告(Webnotice)表控制层
 *
 * @author
 * @since 00:36:31
 */
@RestController
@RequestMapping("webnotice")
public class WebnoticeController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private WebnoticeService webnoticeService;

    /**
     * 分页查询
     *
     * @param webnotice 筛选条件
     * @return 查询结果
     */
    @GetMapping("list")
    public ListByPage queryByPage(Webnotice webnotice) {
        startPage();
        return getList(this.webnoticeService.queryByPage(webnotice));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.webnoticeService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param webnotice 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public AjaxResult add(@RequestBody Webnotice webnotice) {
        return AjaxResult.ok(this.webnoticeService.insert(webnotice));
    }

    /**
     * 编辑数据
     *
     * @param webnotice 实体
     * @return 编辑结果
     */
    @PutMapping("edit")
    public AjaxResult edit( @RequestBody Webnotice webnotice) {
        return AjaxResult.ok(this.webnoticeService.update(webnotice));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("del")
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.webnoticeService.deleteById(id));
    }

}

