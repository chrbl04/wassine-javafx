package model;

public class Traveler {
    private int userId;          // FK -> users.user_id (also PK)
    private boolean verified;    // tinyint(1) in MySQL
    private String bio;
    private String licenceId;    // licence_ID in DB

    public Traveler() {}
    public Traveler(int userId, boolean verified, String bio, String licenceId) {
        this.userId = userId; this.verified = verified; this.bio = bio; this.licenceId = licenceId;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getLicenceId() { return licenceId; }
    public void setLicenceId(String licenceId) { this.licenceId = licenceId; }

    @Override public String toString() {
        return "Traveler{userId=" + userId + ", verified=" + verified + ", licenceId='" + licenceId + "'}";
    }
}
