package com.javaPro.myProject.modules.producttype.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 宠物服务类型表(Producttype)实体类
 *
 * @author AjaxResult
 * @since AjaxResult 08:07:42
 */
@Data
public class Producttype implements Serializable {
    private static final long serialVersionUID = 835846513562511676L;

    private Integer id;
    /**
     * 名称
     */
    private String typeName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 状态 0停用 1 启用
     */
    private String status;
    /**
     * 描述
     */
    private String describe;

    private String img;

}

