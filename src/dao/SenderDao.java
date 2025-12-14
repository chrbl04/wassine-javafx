package dao;

import model.Sender;
import util.Database;

import java.sql.*;

public class SenderDao {

    private static Sender map(ResultSet rs) throws SQLException {
        return new Sender(
                rs.getInt("user_id"),
                rs.getBoolean("verified")
        );
    }

    public Sender findByUserId(int userId) throws SQLException {
        String sql = "SELECT user_id, verified FROM sender WHERE user_id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public boolean insert(Sender s) throws SQLException {
        String sql = "INSERT INTO sender(user_id, verified) VALUES (?,?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getUserId());
            ps.setBoolean(2, s.isVerified());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean update(Sender s) throws SQLException {
        String sql = "UPDATE sender SET verified=? WHERE user_id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, s.isVerified());
            ps.setInt(2, s.getUserId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int userId) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM sender WHERE user_id=?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean exists(int userId) throws SQLException {
        String sql = "SELECT 1 FROM sender WHERE user_id=? LIMIT 1";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
