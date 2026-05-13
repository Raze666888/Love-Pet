package com.javaPro.myProject.modules.aipet.model;

/**
 * 对话消息（用于对话历史）
 */
public class ChatMessage {

    /** 角色：user 或 assistant */
    private String role;

    /** 消息内容 */
    private String content;

    public ChatMessage() {}

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
