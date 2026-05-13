package com.javaPro.myProject.modules.order.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.order.entity.Order;
import com.javaPro.myProject.modules.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单表(Order)表控制层
 *
 * @author
 * @since 21:57:53
 */
@RestController
@RequestMapping("order")
public class OrderController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private OrderService orderService;

    /**
     * 分页查询
     *
     * @param order 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Order order) {
        startPage();
        return getList(this.orderService.queryByPage(order));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.orderService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param order 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add( @RequestBody Order order) {
        return this.orderService.insert(order);
    }

    /**
     * 编辑数据
     *
     * @param order 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit( @RequestBody Order order) {
        return AjaxResult.ok(this.orderService.update(order));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.orderService.deleteById(id));
    }
    /**
     * 饼图
     */
    @GetMapping("statistics")
    @ResponseBody
    public AjaxResult statistics() {


        return this.orderService.statistics();
    }
}

