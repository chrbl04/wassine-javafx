package dao;

import model.User;
import util.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static LocalDateTime getLdt(ResultSet rs, String col) throws SQLException {
        Timestamp t = rs.getTimestamp(col);
        return t != null ? t.toLocalDateTime() : null;
    }

    private static User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("status"),
                rs.getString("country_code"),
                rs.getString("phone"),
                rs.getString("password_hash"),
                rs.getString("country"),
                rs.getString("city"),
                rs.getString("street"),
                rs.getString("building"),
                rs.getString("profile_image_url"),
                rs.getString("id_image_url"),
                getLdt(rs, "date_created")
        );
    }

    public List<User> findAll(int limit) throws SQLException {
        String sql = "SELECT * FROM users ORDER BY date_created DESC LIMIT ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            List<User> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }
    public boolean existsByEmailAndPasswordHash(String email, String passwordHash) {
        String sql = "SELECT 1 FROM users WHERE email = ? AND password_hash = ? LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException("DB login check failed", e);
        }
    }


        public int insert(model.User u) {
            String sql = """
            INSERT INTO users
            (first_name, last_name, email, status, country_code, phone, password_hash,
             country, city, street, building, profile_image_url, id_image_url)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

            try (Connection c = Database.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, u.getFirstName());
                ps.setString(2, u.getLastName());
                ps.setString(3, u.getEmail());
                ps.setString(4, u.getStatus());
                ps.setString(5, u.getCountryCode());
                ps.setString(6, u.getPhone());
                ps.setString(7, u.getPasswordHash());
                ps.setString(8, u.getCountry());
                ps.setString(9, u.getCity());
                ps.setString(10, u.getStreet());
                ps.setString(11, u.getBuilding());
                ps.setString(12, u.getProfileImageUrl());
                ps.setString(13, u.getIdImageUrl());

                ps.executeUpdate();

                try (var rs = ps.getGeneratedKeys()) {
                    return rs.next() ? rs.getInt(1) : -1;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Insert user failed", e);
            }
        }

        private boolean updateField(String sql, Object value, int userId) {
            try (Connection c = Database.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setObject(1, value);
                ps.setInt(2, userId);
                return ps.executeUpdate() == 1;

            } catch (SQLException e) {
                throw new RuntimeException("User update failed", e);
            }
        }

        public boolean updateFirstName(int userId, String firstName) {
            return updateField("UPDATE users SET first_name=? WHERE user_id=?", firstName, userId);
        }

        public boolean updateLastName(int userId, String lastName) {
            return updateField("UPDATE users SET last_name=? WHERE user_id=?", lastName, userId);
        }

        public boolean updateEmail(int userId, String email) {
            return updateField("UPDATE users SET email=? WHERE user_id=?", email, userId);
        }

        public boolean updateStatus(int userId, String status) {
            // status must be one of: SUSPENDED / ACTIVE / BANNED
            return updateField("UPDATE users SET status=? WHERE user_id=?", status, userId);
        }

        public boolean updateCountryCode(int userId, String countryCode) {
            return updateField("UPDATE users SET country_code=? WHERE user_id=?", countryCode, userId);
        }

        public boolean updatePhone(int userId, String phone) {
            return updateField("UPDATE users SET phone=? WHERE user_id=?", phone, userId);
        }

        public boolean updatePasswordHash(int userId, String passwordHash) {
            return updateField("UPDATE users SET password_hash=? WHERE user_id=?", passwordHash, userId);
        }

        public boolean updateCountry(int userId, String country) {
            return updateField("UPDATE users SET country=? WHERE user_id=?", country, userId);
        }

        public boolean updateCity(int userId, String city) {
            return updateField("UPDATE users SET city=? WHERE user_id=?", city, userId);
        }

        public boolean updateStreet(int userId, String street) {
            return updateField("UPDATE users SET street=? WHERE user_id=?", street, userId);
        }

        public boolean updateBuilding(int userId, String building) {
            return updateField("UPDATE users SET building=? WHERE user_id=?", building, userId);
        }

        public boolean updateProfileImageUrl(int userId, String url) {
            return updateField("UPDATE users SET profile_image_url=? WHERE user_id=?", url, userId);
        }

        public boolean updateIdImageUrl(int userId, String url) {
            return updateField("UPDATE users SET id_image_url=? WHERE user_id=?", url, userId);
        }
    }

