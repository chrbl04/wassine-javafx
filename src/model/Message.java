package model;

import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private int conversationId;
    private int authorUserId;
    private String body;
    private LocalDateTime sentAt;

    public Message() {}

    public Message(int messageId, int conversationId, int authorUserId,
                   String body, LocalDateTime sentAt) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.authorUserId = authorUserId;
        this.body = body;
        this.sentAt = sentAt;
    }

    // ======== GETTERS ========

    public int getMessageId() {
        return messageId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public int getAuthorUserId() {
        return authorUserId;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    // ======== SETTERS ========

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public void setAuthorUserId(int authorUserId) {
        this.authorUserId = authorUserId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    // Optional toString for debugging
    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", conversationId=" + conversationId +
                ", authorUserId=" + authorUserId +
                ", body='" + body + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
