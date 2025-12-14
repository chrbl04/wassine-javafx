package model;

import java.time.LocalDateTime;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String countryCode;
    private String phone;
    private String passwordHash;
    private String country;
    private String city;
    private String street;
    private String building;
    private String profileImageUrl;
    private String idImageUrl;
    private LocalDateTime dateCreated;

    public User() {}

    public User(int userId, String firstName, String lastName, String email, String status,
                String countryCode, String phone, String passwordHash, String country,
                String city, String street, String building, String profileImageUrl,
                String idImageUrl, LocalDateTime dateCreated) {

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.countryCode = countryCode;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.profileImageUrl = profileImageUrl;
        this.idImageUrl = idImageUrl;
        this.dateCreated = dateCreated;
    }

    // ---------- GETTERS AND SETTERS ----------

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getIdImageUrl() { return idImageUrl; }
    public void setIdImageUrl(String idImageUrl) { this.idImageUrl = idImageUrl; }

    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + firstName + " " + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + countryCode + phone + '\'' +
                ", city='" + city + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
