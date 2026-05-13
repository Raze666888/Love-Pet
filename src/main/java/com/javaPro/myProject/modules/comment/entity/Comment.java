package com.javaPro.myProject.modules.comment.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 公告评论(Comment)实体类
 *
 * @author
 * @since 2025-10-08
 */
@Data
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Integer id;

    /**
     * 公告ID
     */
    private Integer noticeId;

    /**
     * 评论用户ID
     */
    private Integer userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态（1正常，0删除）
     */
    private String status;

    /**
     * 父评论ID（用于回复功能，暂时预留）
     */
    private Integer parentId;

    // 以下字段用于关联查询，不存储在数据库中
    
    /**
     * 评论用户名（关联查询）
     */
    private String username;

    /**
     * 评论用户头像（关联查询）
     */
    private String userAvatar;

    /**
     * 公告标题（关联查询）
     */
    private String noticeTitle;

    /**
     * 格式化的创建时间
     */
    private String createTimeFormatted;

    /**
     * 是否为当前用户的评论
     */
    private Boolean isCurrentUser;

    /**
     * 回复数量（如果支持回复功能）
     */
    private Integer replyCount;

    /**
     * 回复列表（树形结构）
     */
    private List<Comment> replies;

    /**
     * 回复的对象用户名
     */
    private String replyToUser;
}
