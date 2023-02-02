package com.example.instagram.Models;

public class Chatting {
    String fromId , message, messageId;
    Long timestamp;
    String toId;

    public Chatting(String fromId, String message) {
        this.fromId = fromId;
        this.message = message;
    }

    public Chatting(String fromId, String message, String toId) {
        this.fromId = fromId;
        this.message = message;
        this.toId = toId;
    }

    public Chatting() {
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
