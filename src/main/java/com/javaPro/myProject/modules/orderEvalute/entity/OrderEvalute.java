package com.javaPro.myProject.modules.orderEvalute.entity;

import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 评价表(OrderEvalute)实体类
 *
 */
@Data
public class OrderEvalute implements Serializable {
    private static final long serialVersionUID = -40635941938958298L;

    private Integer id;
    /**
     * 内容
     */
    private String content;

    private Date createtime;
    /**
     * 用户id
     */
    private Integer userid;
    /**
     * 订单id
     */
    private Integer orderid;

//    企业信息
    private Integer companyid;

    /**
     * 评分（1-5分，支持0.5倍数）
     */
    private Double rating;

    private Sysuser sysuser;

}

