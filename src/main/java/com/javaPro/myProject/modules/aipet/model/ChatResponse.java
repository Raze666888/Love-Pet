package com.javaPro.myProject.modules.aipet.model;

/**
 * AI聊天响应
 */
public class ChatResponse {

    private boolean success;
    private String answer;
    private String timestamp;
    private String error;

    public ChatResponse() {}

    public static ChatResponse success(String answer) {
        ChatResponse resp = new ChatResponse();
        resp.setSuccess(true);
        resp.setAnswer(answer);
        resp.setTimestamp(java.time.LocalDateTime.now().toString());
        return resp;
    }

    public static ChatResponse fail(String error) {
        ChatResponse resp = new ChatResponse();
        resp.setSuccess(false);
        resp.setAnswer("抱歉，我暂时无法回答您的问题，请稍后再试。");
        resp.setError(error);
        resp.setTimestamp(java.time.LocalDateTime.now().toString());
        return resp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
