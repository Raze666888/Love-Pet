package com.javaPro.myProject.modules.webnotice.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 公告(Webnotice)实体类
 *
 * @author
 * @since  00:36:32
 */
public class Webnotice implements Serializable {
    private static final long serialVersionUID = -91924657176393862L;

    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 类型
     */
    private Long type;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 0删除 1 正常
     */
    private String status;
    /**
     * 封面
     */
    private String img;

    /**
     * 类型名称（关联查询字段）
     */
    private String typeName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}

