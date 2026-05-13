package com.javaPro.myProject.modules.aipet.model;

import java.util.List;

/**
 * AI聊天请求
 */
public class ChatRequest {

    /** 用户问题 */
    private String question;

    /** 对话历史（可选） */
    private List<ChatMessage> conversationHistory;

    public ChatRequest() {}

    public ChatRequest(String question, List<ChatMessage> conversationHistory) {
        this.question = question;
        this.conversationHistory = conversationHistory;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ChatMessage> getConversationHistory() {
        return conversationHistory;
    }

    public void setConversationHistory(List<ChatMessage> conversationHistory) {
        this.conversationHistory = conversationHistory;
    }
}
