package com.javaPro.myProject.modules.tmoney.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 充值表(Tmoney)实体类
 *
 * @author
 * @since  18:46:26
 */
@Data
public class Tmoney implements Serializable {
    private static final long serialVersionUID = 245917199954927815L;

    private Integer id;
    /**
     * 金额
     */
    private String money;
    /**
     * 提交时间
     */
    private Date createtime;
    /**
     * 审核时间
     */
    private Date audittime;
    /**
     * 审核状态，待审核，已审核，驳回
     */
    private String auditstatus;
    /**
     * 驳回原因/备注
     */
    private String cause;
    /**
     * 用户
     */
    private Integer userid;



}

