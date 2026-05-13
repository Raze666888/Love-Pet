package com.javaPro.myProject.modules.product.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.product.dto.ServiceProviderFilterDTO;
import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 宠物服务表(Product)表控制层
 *
 * @author AjaxResult
 * @since AjaxResult 07:51:54
 */
@RestController
@RequestMapping("product")
public class ProductController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private ProductService productService;

    /**
     * 分页查询
     *
     * @param product 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Product product) {
        try {
            // 安全地启动分页
            try {
                startPage();
            } catch (Exception e) {
                // 如果分页启动失败，使用默认分页
                com.github.pagehelper.PageHelper.startPage(1, 10).setReasonable(true);
            }
            return getList(this.productService.queryByPage(product));
        } catch (Exception e) {
            e.printStackTrace();
            return getList(500, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @param userid 用户ID（可选，用于计算个人销量）
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id, Integer userid) {
        return AjaxResult.ok(this.productService.queryById(id, userid));
    }

    /**
     * 新增数据
     *
     * @param product 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Product product) {
        return AjaxResult.ok(this.productService.insert(product));
    }

    /**
     * 编辑数据
     *
     * @param product 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Product product) {
        return AjaxResult.ok(this.productService.update(product));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.productService.deleteById(id));
    }

    /**
     * 更新产品服务时间段数据（用于测试）
     *
     * @return 更新结果
     */
    @PostMapping("/updateServiceTime")
    public AjaxResult updateServiceTime() {
        try {
            int updatedCount = this.productService.updateServiceTimeForTesting();
            return AjaxResult.ok("成功更新 " + updatedCount + " 个产品的服务时间段");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("更新服务时间段失败: " + e.getMessage());
        }
    }

    /**
     * 根据时间和价格区间筛选合适的服务者
     *
     * @param filterDTO 筛选条件
     * @return 筛选结果
     */
    @PostMapping("/filterServices")
    public AjaxResult filterServices(@RequestBody ServiceProviderFilterDTO filterDTO) {
        try {
            // 转换DTO为Product查询条件
            Product product = convertFilterDTOToProduct(filterDTO);

            // 设置分页参数（使用PageHelper直接设置）
            if (filterDTO.getPageNum() != null && filterDTO.getPageSize() != null) {
                com.github.pagehelper.PageHelper.startPage(filterDTO.getPageNum(), filterDTO.getPageSize()).setReasonable(true);
            } else {
                try {
                    startPage();
                } catch (Exception e) {
                    com.github.pagehelper.PageHelper.startPage(1, 10).setReasonable(true);
                }
            }

            // 执行查询
            ListByPage result = getList(this.productService.queryByPage(product));

            return AjaxResult.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("筛选服务失败: " + e.getMessage());
        }
    }

    /**
     * GET方式的服务筛选接口（用于URL参数传递）
     *
     * @param serviceStartTime 服务开始时间
     * @param serviceEndTime 服务结束时间
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param minRating 最低评分
     * @param productType 服务类型
     * @param keyword 关键词
     * @param sortBy 排序方式
     * @return 筛选结果
     */
    @GetMapping("/filterServices")
    public AjaxResult filterServicesGet(
            @RequestParam(required = false) String serviceStartTime,
            @RequestParam(required = false) String serviceEndTime,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer productType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        try {
            // 创建筛选DTO
            ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
            filterDTO.setServiceStartTime(serviceStartTime);
            filterDTO.setServiceEndTime(serviceEndTime);
            filterDTO.setMinPrice(minPrice);
            filterDTO.setMaxPrice(maxPrice);
            filterDTO.setMinRating(minRating);
            filterDTO.setProductType(productType);
            filterDTO.setKeyword(keyword);
            filterDTO.setSortBy(sortBy);
            filterDTO.setPageNum(pageNum);
            filterDTO.setPageSize(pageSize);

            // 转换DTO为Product查询条件
            Product product = convertFilterDTOToProduct(filterDTO);

            // 设置分页参数（使用PageHelper直接设置）
            com.github.pagehelper.PageHelper.startPage(pageNum, pageSize).setReasonable(true);

            // 执行查询
            ListByPage result = getList(this.productService.queryByPage(product));

            return AjaxResult.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("筛选服务失败: " + e.getMessage());
        }
    }

    /**
     * 将筛选DTO转换为Product查询条件
     *
     * @param filterDTO 筛选DTO
     * @return Product查询条件
     */
    private Product convertFilterDTOToProduct(ServiceProviderFilterDTO filterDTO) {
        Product product = new Product();

        // 时间筛选
        if (filterDTO.getServiceStartTime() != null && !filterDTO.getServiceStartTime().isEmpty()) {
            product.setServiceStartTime(filterDTO.getServiceStartTime());
        }
        if (filterDTO.getServiceEndTime() != null && !filterDTO.getServiceEndTime().isEmpty()) {
            product.setServiceEndTime(filterDTO.getServiceEndTime());
        }

        // 价格区间筛选
        product.setMinPrice(filterDTO.getMinPrice());
        product.setMaxPrice(filterDTO.getMaxPrice());

        // 评分筛选
        product.setCompanyRating(filterDTO.getMinRating());

        // 服务类型筛选
        if (filterDTO.getProductType() != null) {
            product.setProducttype(filterDTO.getProductType().toString());
        }

        // 关键词搜索
        if (filterDTO.getKeyword() != null && !filterDTO.getKeyword().isEmpty()) {
            product.setProductname(filterDTO.getKeyword());
        }

        // 服务商筛选
        if (filterDTO.getCompanyId() != null) {
            product.setCompanyid(filterDTO.getCompanyId());
        }

        // 只显示有效状态的服务
        product.setStatus("1");

        // 只显示有库存的服务
        if (filterDTO.getOnlyAvailable() != null && filterDTO.getOnlyAvailable()) {
            // 这里可以添加库存筛选逻辑，暂时跳过
        }

        return product;
    }

}

