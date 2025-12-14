package dao;

import model.Traveler;
import util.Database;

import java.sql.*;

public class TravelerDao {

    private static Traveler map(ResultSet rs) throws SQLException {
        return new Traveler(
                rs.getInt("user_id"),
                rs.getBoolean("verified"),
                rs.getString("bio"),
                rs.getString("licence_ID")
        );
    }

    public Traveler findByUserId(int userId) throws SQLException {
        String sql = "SELECT user_id, verified, bio, licence_ID FROM traveler WHERE user_id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** insert; returns true if row created (expect PK=user_id) */
    public boolean insert(Traveler t) throws SQLException {
        String sql = "INSERT INTO traveler(user_id, verified, bio, licence_ID) VALUES (?,?,?,?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getUserId());
            ps.setBoolean(2, t.isVerified());
            ps.setString(3, t.getBio());
            ps.setString(4, t.getLicenceId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean update(Traveler t) throws SQLException {
        String sql = "UPDATE traveler SET verified=?, bio=?, licence_ID=? WHERE user_id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, t.isVerified());
            ps.setString(2, t.getBio());
            ps.setString(3, t.getLicenceId());
            ps.setInt(4, t.getUserId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int userId) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM traveler WHERE user_id=?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        }
    }

    /** convenience for role check */
    public boolean exists(int userId) throws SQLException {
        String sql = "SELECT 1 FROM traveler WHERE user_id=? LIMIT 1";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
