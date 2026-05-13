package com.javaPro.myProject.modules.shopcart.entity;

import com.javaPro.myProject.modules.product.entity.Product;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 购物车(Shopcart)实体类
 *
 * @author
 * @since  15:35:15
 */
@Data
public class Shopcart implements Serializable {
    private static final long serialVersionUID = -68767558503570095L;

    private Integer id;
    /**
     * 宠物服务id
     */
    private Integer productid;
    /**
     * 用户id
     */
    private Integer userid;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 备用字段1
     */
    private String spare1;
    /**
     * 备用字段2
     */
    private String spare2;


 private Product product;

}

