package com.javaPro.myProject.modules.sysuser.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * 用户表(Sysuser)实体类
 *
 * @author
 * @since  17:36:57
 */
@Data
public class Sysuser implements Serializable {
    private static final long serialVersionUID = 459619795586785767L;
    /**
     * id
     */
    private Integer id;
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 性别
     */
    private String sex;
    /**
     * 联系方式
     */
    private String phonenumber;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 证件号
     */
    private String idcard;
    /**
     * 地址信息
     */
    private String address;
    /**
     * 图片
     */
    private String img;
    /**
     * 角色（1管理员，2用户，3其他）
     */
    private String role;
    /**
     * 注册时间
     */
    private Date createtime;
    /**
     * 年龄
     */
    private String age;
    private String petname;
    private String petage;
    private String petdes;
    private String pettype;
    /**
     * 备注信息
     */
    private String remark;
    private Double money;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;


}

