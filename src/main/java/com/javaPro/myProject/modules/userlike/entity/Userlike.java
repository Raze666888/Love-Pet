package com.javaPro.myProject.modules.userlike.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户收藏(Userlike)实体类
 *
 * @author AjaxResult
 * @since AjaxResult 18:54:42
 */
@Data
public class Userlike implements Serializable {
    private static final long serialVersionUID = 329955207749595185L;

    private Integer id;
    /**
     * 用户id
     */
    private Integer userid;
    /**
     * 宠物服务id
     */
    private Integer productid;
    /**
     * 收藏时间
     */
    private Date createtime;
    /**
     * 备用1
     */
    private String spare1;
    private String productname;
    private String img;



}

