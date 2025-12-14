// model/Admin.java
package model;
import java.time.LocalDateTime;

public class Admin {
    private int adminId;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public Admin() {}
    public Admin(int adminId, String name, String email, LocalDateTime createdAt) {
        this.adminId = adminId; this.name = name; this.email = email; this.createdAt = createdAt;
    }
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    @Override public String toString() { return "Admin{" + adminId + ", " + name + ", " + email + "}"; }
}
