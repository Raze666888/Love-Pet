package com.javaPro.myProject.modules.message.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 消息表(Message)实体类
 *
 * @author
 * @since  19:04:32
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 866457829591410752L;

    private Integer id;
    /**
     * 内容
     */
    private String content;
    /**
     * 发送时间
     */
    private Date createtime;
    /**
     * 回复时间
     */
    private Date updatetime;
    /**
     * 发送人id
     */
    private Integer senderid;
    /**
     * 接收人id
     */
    private Integer receiveid;
    /**
     * 状态（0未处理，1已回复）
     */
    private String status;
    private String senderName;
    private String receiverName;
    private String senderRole;
    private String receiverRole;


}

