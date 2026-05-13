package com.javaPro.myProject.modules.orderEvalute.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.orderEvalute.entity.OrderEvalute;
import com.javaPro.myProject.modules.orderEvalute.service.OrderEvaluteService;
import com.javaPro.myProject.modules.company.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 评价表(OrderEvalute)表控制层
 */
@RestController
@RequestMapping("orderEvalute")
public class OrderEvaluteController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private OrderEvaluteService orderEvaluteService;

    @Resource
    private CompanyService companyService;

    /**
     * 分页查询
     *
     * @param orderEvalute 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(OrderEvalute orderEvalute) {
        return getList(this.orderEvaluteService.queryByPage(orderEvalute));
    }


    /**
     * 新增数据
     *
     * @param orderEvalute 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(OrderEvalute orderEvalute) {
        // 设置创建时间
        orderEvalute.setCreatetime(new Date());

        // 保存评价
        OrderEvalute result = this.orderEvaluteService.insert(orderEvalute);

        // 如果有服务商ID且有评分，更新服务商评分统计
        if (orderEvalute.getCompanyid() != null && orderEvalute.getRating() != null) {
            companyService.updateRatingStats(orderEvalute.getCompanyid());

            // 返回更新后的评分统计信息
            AjaxResult response = AjaxResult.ok(result);
            Double avgRating = this.orderEvaluteService.getAvgRatingByCompanyId(orderEvalute.getCompanyid());
            Integer ratingCount = this.orderEvaluteService.getRatingCountByCompanyId(orderEvalute.getCompanyid());
            response.put("avgRating", avgRating != null ? avgRating : 0.0);
            response.put("ratingCount", ratingCount != null ? ratingCount : 0);
            response.put("companyid", orderEvalute.getCompanyid());
            return response;
        }

        return AjaxResult.ok(result);
    }

    /**
     * 编辑数据
     *
     * @param orderEvalute 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(OrderEvalute orderEvalute) {
        return AjaxResult.ok(this.orderEvaluteService.update(orderEvalute));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.orderEvaluteService.deleteById(id));
    }

    /**
     * 根据服务商ID查询评价列表
     *
     * @param companyid 服务商ID
     * @return 评价列表
     */
    @GetMapping("byCompany")
    public AjaxResult queryByCompanyId(Integer companyid) {
        return AjaxResult.ok(this.orderEvaluteService.queryByCompanyId(companyid));
    }

    /**
     * 获取服务商评分统计
     *
     * @param companyid 服务商ID
     * @return 评分统计
     */
    @GetMapping("ratingStats")
    public AjaxResult getRatingStats(Integer companyid) {
        Double avgRating = this.orderEvaluteService.getAvgRatingByCompanyId(companyid);
        Integer ratingCount = this.orderEvaluteService.getRatingCountByCompanyId(companyid);

        AjaxResult result = AjaxResult.ok();
        result.put("avgRating", avgRating != null ? avgRating : 0.0);
        result.put("ratingCount", ratingCount != null ? ratingCount : 0);
        return result;
    }

}

