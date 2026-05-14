package com.javaPro.myProject.modules.aipet.model;

import java.util.List;

/**
 * AI Chat Request
 */
public class ChatRequest {

    /** User's question */
    private String question;

    /** Conversation history (optional) */
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
