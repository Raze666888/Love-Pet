package com.javaPro.myProject.modules.product.dto;

import lombok.Data;

/**
 * 服务者筛选条件DTO
 * 用于根据时间、价格区间、评分等条件筛选合适的服务者
 *
 * @author AjaxResult
 * @since 2024-09-27
 */
@Data
public class ServiceProviderFilterDTO {
    
    /**
     * 服务开始时间（格式：HH:mm，如 "09:00"）
     */
    private String serviceStartTime;
    
    /**
     * 服务结束时间（格式：HH:mm，如 "18:00"）
     */
    private String serviceEndTime;
    
    /**
     * 最低价格
     */
    private Double minPrice;
    
    /**
     * 最高价格
     */
    private Double maxPrice;
    
    /**
     * 最低评分（1-5分）
     */
    private Double minRating;
    
    /**
     * 服务类型ID
     */
    private Integer productType;
    
    /**
     * 服务区域/地址
     */
    private String serviceArea;
    
    /**
     * 关键词搜索（服务名称）
     */
    private String keyword;
    
    /**
     * 排序方式
     * price_asc: 价格升序
     * price_desc: 价格降序
     * rating_desc: 评分降序
     * distance_asc: 距离升序（暂未实现）
     */
    private String sortBy;
    
    /**
     * 页码（从1开始）
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 10;
    
    /**
     * 是否只显示有库存的服务
     */
    private Boolean onlyAvailable = true;
    
    /**
     * 服务商ID（可选，用于查看特定服务商的服务）
     */
    private Integer companyId;
}
