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

    // For now no INSERT/UPDATE since users came from your dump
}
