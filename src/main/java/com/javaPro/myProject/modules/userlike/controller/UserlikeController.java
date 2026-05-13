package com.javaPro.myProject.modules.userlike.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.userlike.entity.Userlike;
import com.javaPro.myProject.modules.userlike.service.UserlikeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户收藏(Userlike)表控制层
 *
 * @author AjaxResult
 * @since AjaxResult 18:54:41
 */
@RestController
@RequestMapping("userlike")
public class UserlikeController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private UserlikeService userlikeService;

    /**
     * 分页查询
     *
     * @param userlike 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Userlike userlike) {
        return getList(this.userlikeService.queryByPage(userlike));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.userlikeService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param userlike 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Userlike userlike) {
        return AjaxResult.ok("收藏成功",this.userlikeService.insert(userlike));
    }

    /**
     * 编辑数据
     *
     * @param userlike 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Userlike userlike) {
        return AjaxResult.ok(this.userlikeService.update(userlike));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.userlikeService.deleteById(id));
    }
    @DeleteMapping("delete")
    public AjaxResult delete(Userlike id) {
        return AjaxResult.ok("取消成功",this.userlikeService.delete(id));
    }
}

