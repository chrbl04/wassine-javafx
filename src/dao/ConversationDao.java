// dao/ConversationDao.java
package dao;

import model.Conversation;
import util.Database;
import util.DaoUtil;

import java.sql.*;
import java.util.*;

public class ConversationDao {
    private static Conversation map(ResultSet rs) throws SQLException {
        return new Conversation(
                rs.getInt("conversation_id"),
                rs.getInt("service_id"),
                rs.getInt("sender_id"),
                rs.getInt("traveler_id"),
                DaoUtil.getLdt(rs, "created_at"),
                DaoUtil.getLdt(rs, "last_message_at"),
                Conversation.Status.valueOf(rs.getString("status"))
        );
    }

    public Conversation findOrCreate(int serviceId, int senderId, int travelerId) throws SQLException {
        String sel = "SELECT * FROM conversation WHERE service_id=? AND sender_id=? AND traveler_id=?";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sel)) {
            ps.setInt(1, serviceId); ps.setInt(2, senderId); ps.setInt(3, travelerId);
            try (var rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        }
        String ins = "INSERT INTO conversation(service_id,sender_id,traveler_id) VALUES (?,?,?)";
        try (var c = Database.getConnection();
             var ps = c.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, serviceId); ps.setInt(2, senderId); ps.setInt(3, travelerId);
            ps.executeUpdate();
            try (var k = ps.getGeneratedKeys()) { if (k.next()) {
                return findById(k.getInt(1));
            }}
        }
        return null;
    }

    public Conversation findById(int id) throws SQLException {
        try (var c = Database.getConnection(); var ps = c.prepareStatement("SELECT * FROM conversation WHERE conversation_id=?")) {
            ps.setInt(1, id); try (var rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public List<Conversation> listForUser(int userId) throws SQLException {
        String sql = "SELECT * FROM conversation WHERE sender_id=? OR traveler_id=? ORDER BY COALESCE(last_message_at, created_at) DESC";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId); ps.setInt(2, userId);
            try (var rs = ps.executeQuery()) {
                List<Conversation> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }
}
