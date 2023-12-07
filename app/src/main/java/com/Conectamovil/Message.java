package com.Conectamovil;

public class Message {
    private String userId;
    private String content;

    public Message() {
        // Constructor vacío requerido por Firebase
    }

    public Message(String userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }
}
