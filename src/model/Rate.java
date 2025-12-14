package model;

import java.time.LocalDateTime;

public class Rate {

    private int senderId;
    private int contractId;
    private int score;
    private String comment;
    private LocalDateTime dateGiven;

    public Rate() {}

    public Rate(int senderId, int contractId, int score, String comment, LocalDateTime dateGiven) {
        this.senderId = senderId;
        this.contractId = contractId;
        this.score = score;
        this.comment = comment;
        this.dateGiven = dateGiven;
    }

    // ======== GETTERS ========

    public int getSenderId() {
        return senderId;
    }

    public int getContractId() {
        return contractId;
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getDateGiven() {
        return dateGiven;
    }

    // ======== SETTERS ========

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDateGiven(LocalDateTime dateGiven) {
        this.dateGiven = dateGiven;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "senderId=" + senderId +
                ", contractId=" + contractId +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                ", dateGiven=" + dateGiven +
                '}';
    }
}
