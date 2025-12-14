// dao/MessageDao.java
package dao;

import model.Message;
import util.Database;
import util.DaoUtil;

import java.sql.*;
import java.util.*;

public class MessageDao {
    private static Message map(ResultSet rs) throws SQLException {
        return new Message(
                rs.getInt("message_id"),
                rs.getInt("conversation_id"),
                rs.getInt("author_user_id"),
                rs.getString("body"),
                DaoUtil.getLdt(rs, "sent_at")
        );
    }

    public List<Message> listByConversation(int convId, int limit) throws SQLException {
        String sql = "SELECT * FROM message WHERE conversation_id=? ORDER BY sent_at ASC LIMIT ?";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setInt(1, convId); ps.setInt(2, limit);
            try (var rs = ps.executeQuery()) {
                List<Message> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public int insert(Message m) throws SQLException {
        String sql = "INSERT INTO message(conversation_id,author_user_id,body,sent_at) VALUES (?,?,?,?)";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getConversationId());
            ps.setInt(2, m.getAuthorUserId());
            ps.setString(3, m.getBody());
            DaoUtil.setTs(ps, 4, m.getSentAt());
            ps.executeUpdate();
            try (var k = ps.getGeneratedKeys()) { return k.next() ? k.getInt(1) : 0; }
        }
    }
}
