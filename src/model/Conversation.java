// model/Conversation.java
package model;
import java.time.LocalDateTime;

public class Conversation {
    public enum Status { open, closed, archived }
    private int conversationId, serviceId, senderId, travelerId;
    private LocalDateTime createdAt, lastMessageAt;
    private Status status;

    public Conversation() {}
    public Conversation(int conversationId, int serviceId, int senderId, int travelerId,
                        LocalDateTime createdAt, LocalDateTime lastMessageAt, Status status) {
        this.conversationId = conversationId; this.serviceId = serviceId; this.senderId = senderId;
        this.travelerId = travelerId; this.createdAt = createdAt; this.lastMessageAt = lastMessageAt; this.status = status;
    }
    // getters/setters...
}
