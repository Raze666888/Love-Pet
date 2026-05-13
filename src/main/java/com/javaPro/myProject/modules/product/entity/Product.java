package com.javaPro.myProject.modules.product.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * 宠物服务表(Product)实体类
 *
 * @author
 * @since  11:27:18
 */
@Data
public class Product implements Serializable {
    private static final long serialVersionUID = -72992493950183581L;

    private Integer id;
    /**
     * 宠物服务名
     */
    private String productname;
    /**
     * 宠物服务描述
     */
    private String productdes;
    /**
     * 图片
     */
    private String img;
    /**
     * 宠物服务成本
     */
    private String chengben;
    /**
     * 服务价格
     */
    private String kedanjia;
    /**
     * 当日可约数量
     */
    private String kucun;
    /**
     * 服务天数
     */
    private String fahuotianshu;
    /**
     * 服务提供地址
     */
    private String chandi;
    /**
     * 服务质量
     */
    private String guige;
    /**
     * 商家id
     */
    private Integer companyid;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 状态
     */
    private String status;
    /**
     * 更新时间
     */
    private Date updatetime;
    /**
     * 宠物服务类型
     */
    private String producttype;
    private String typeName;
    /**
     * 销售量
     */
    private String spare1;
    /**
     * 折扣
     */
    private String spare2;

    private Boolean likeFlag;
    private String userid;
    private String detailimg;
    private List<String> detailImgList;

    /**
     * 服务开始时间
     */
    private String serviceStartTime;

    /**
     * 服务结束时间
     */
    private String serviceEndTime;

    /**
     * 服务商信息（关联查询时使用）
     */
    private String companyName;
    private Double companyRating;
    private Integer companyRatingCount;
    private String companyServiceArea;

    /**
     * 价格区间筛选字段（查询时使用，不存储到数据库）
     */
    private Double minPrice;
    private Double maxPrice;

    /**
     * 服务商经纬度（关联查询时使用）
     */
    private Double companyLongitude;
    private Double companyLatitude;

    /**
     * 距离用户的距离（公里，查询时计算）
     */
    private Double distance;

    /**
     * 格式化的距离显示
     */
    private String distanceText;

    public String getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(String serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getCompanyRating() {
        return companyRating;
    }

    public void setCompanyRating(Double companyRating) {
        this.companyRating = companyRating;
    }

    public Integer getCompanyRatingCount() {
        return companyRatingCount;
    }

    public void setCompanyRatingCount(Integer companyRatingCount) {
        this.companyRatingCount = companyRatingCount;
    }

    public String getCompanyServiceArea() {
        return companyServiceArea;
    }

    public void setCompanyServiceArea(String companyServiceArea) {
        this.companyServiceArea = companyServiceArea;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}

