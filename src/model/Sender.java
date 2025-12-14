package model;

public class Sender {
    private int userId;        // FK -> users.user_id (also PK)
    private boolean verified;  // tinyint(1)

    public Sender() {}
    public Sender(int userId, boolean verified) { this.userId = userId; this.verified = verified; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    @Override public String toString() {
        return "Sender{userId=" + userId + ", verified=" + verified + "}";
    }
}
