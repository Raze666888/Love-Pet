package com.javaPro.myProject.modules.order.entity;

import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.shopcart.entity.Shopcart;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * 订单表(Order)实体类
 *
 * @author
 * @since  21:57:54
 */
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = -21195458741738992L;

    private Integer id;
    /**
     * 购物车ids
     */
    private String carid;
    /**
     * 下单时间
     */
    private Date createtime;
    /**
     * 更新时间
     */
    private Date updatetime;
    /**
     * 用户
     */
    private Integer userid;
    private Integer companyid;

    private String spare1;
    private String remark;

    private String spare2;
//    评价信息
    private String content;
    private String username;
    private String account;
    private List<Shopcart> cartList;

    // 选中的购物车ID列表（前端传递）
    private String selectedCartIds;
}

