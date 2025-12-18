// src/Main.java
import util.Database;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Wassine DB smoke test ===");

        try (Connection con = Database.getConnection()) {
            System.out.println("✅ Connected: " + con.getMetaData().getURL());

            // 1) Table counts
            count(con, "users");
            count(con, "sender");
            count(con, "traveler");
            count(con, "service");
            count(con, "admin");
            count(con, "vehicle");
            count(con, "contract");
            count(con, "conversation");
            count(con, "message");
            count(con, "service_approval");
            count(con, "accept");
            count(con, "star");
            count(con, "rate");
            count(con, "service_image");
            count(con, "vehicle_image");
            count(con, "make_contract");

            // 2) Show a few rows
            sample(con, "users", "user_id, first_name, last_name, email, status", "user_id", 5);
            sample(con, "service", "service_id, title, status, category, offer_price", "service_id", 5);
            sample(con, "vehicle", "vehicle_id, traveler_id, vehicle_type, make, model, status", "vehicle_id", 5);
            sample(con, "contract", "contract_id, vehicle_id, transport_fee, status, payment, payment_method, created_at",
                    "contract_id", 5);

            // 3) CRUD round-trip on a simple independent table: admin
            Integer adminId = createAdmin(con, "TEST Admin", "test_admin_" + System.currentTimeMillis() + "@ex.com");
            if (adminId != null) {
                updateAdminEmail(con, adminId, "test_admin_updated@ex.com");
                deleteAdmin(con, adminId);
            }

            // 4) Insert a Contract using an EXISTING vehicle_id (FK-safe)
            Optional<Integer> anyVehicleId = firstId(con, "vehicle", "vehicle_id");
            if (anyVehicleId.isPresent()) {
                Integer contractId = insertTestContract(con, anyVehicleId.get());
                if (contractId != null) {
                    updateContractStatusToCompleted(con, contractId);
                    deleteContract(con, contractId);
                }
            } else {
                System.out.println("ℹ️ Skipping contract test (no vehicles found).");
            }

            // 5) Conversation + Message: use existing service/sender/traveler if available
            Optional<Integer> serviceId = firstId(con, "service", "service_id");
            Optional<Integer> senderId = firstId(con, "sender", "user_id");
            Optional<Integer> travelerId = firstId(con, "traveler", "user_id");
            if (serviceId.isPresent() && senderId.isPresent() && travelerId.isPresent()) {
                Integer convId = ensureConversation(con, serviceId.get(), senderId.get(), travelerId.get());
                if (convId != null) {
                    Integer msgId = insertMessage(con, convId, senderId.get(), "Hello from test @" + LocalDateTime.now());
                    if (msgId != null) {
                        deleteMessage(con, msgId);
                    }
                    deleteConversation(con, convId); // will fail if other messages exist (caught & reported)
                }
            } else {
                System.out.println("ℹ️ Skipping conversation/message test (need service+sender+traveler).");
            }

            System.out.println("✅ Smoke test finished.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Smoke test failed.");
        }
    }

    // ---------- helpers ----------

    static void count(Connection con, String table) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            System.out.printf("• %-16s : %d%n", table, rs.getInt(1));
        }
    }

    static void sample(Connection con, String table, String cols, String orderBy, int limit) throws SQLException {
        String sql = "SELECT " + cols + " FROM " + table + " ORDER BY " + orderBy + " DESC LIMIT ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("— " + table + " (top " + limit + "):");
                int colCount = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    StringBuilder b = new StringBuilder("  ");
                    for (int i = 1; i <= colCount; i++) {
                        if (i > 1) b.append(" | ");
                        b.append(rs.getMetaData().getColumnLabel(i)).append('=').append(rs.getString(i));
                    }
                    System.out.println(b);
                }
            }
        } catch (SQLSyntaxErrorException e) {
            // Table might not exist in your current schema revision—skip politely
            System.out.println("ℹ️ Skip sample for " + table + " (schema/columns mismatch).");
        }
    }

    static Optional<Integer> firstId(Connection con, String table, String idCol) {
        String sql = "SELECT " + idCol + " FROM " + table + " ORDER BY " + idCol + " ASC LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return Optional.of(rs.getInt(1));
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    // ----- admin CRUD (independent table) -----

    static Integer createAdmin(Connection con, String name, String email) {
        String sql = "INSERT INTO admin(name, email, created_at) VALUES (?, ?, NOW())";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✅ Created admin_id=" + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ createAdmin: " + e.getMessage());
        }
        return null;
    }

    static void updateAdminEmail(Connection con, int adminId, String newEmail) {
        String sql = "UPDATE admin SET email=? WHERE admin_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newEmail);
            ps.setInt(2, adminId);
            int n = ps.executeUpdate();
            System.out.println("🔧 Updated admin " + adminId + " (rows=" + n + ")");
        } catch (SQLException e) {
            System.out.println("⚠️ updateAdminEmail: " + e.getMessage());
        }
    }

    static void deleteAdmin(Connection con, int adminId) {
        String sql = "DELETE FROM admin WHERE admin_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            int n = ps.executeUpdate();
            System.out.println("🧹 Deleted admin " + adminId + " (rows=" + n + ")");
        } catch (SQLException e) {
            System.out.println("⚠️ deleteAdmin: " + e.getMessage());
        }
    }

    // ----- contract CRUD (FK: vehicle_id) -----

    static Integer insertTestContract(Connection con, int vehicleId) {
        String sql = """
                INSERT INTO contract
                    (vehicle_id, created_at, transport_fee, pickup_date, delivery_date, signed_date,
                     terms_text, payment_method, payment, status)
                VALUES
                    (?, NOW(), ?, NOW()+INTERVAL 1 DAY, NOW()+INTERVAL 2 DAY, NULL,
                     'TEST terms', 'Card', 'Pending', 'Active')
                """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, vehicleId);
            ps.setBigDecimal(2, new BigDecimal("12.34"));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✅ Inserted contract_id=" + id + " (vehicle_id=" + vehicleId + ")");
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ insertTestContract: " + e.getMessage());
        }
        return null;
    }

    static void updateContractStatusToCompleted(Connection con, int contractId) {
        String sql = "UPDATE contract SET status='Completed' WHERE contract_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, contractId);
            int n = ps.executeUpdate();
            System.out.println("🔧 Contract " + contractId + " set to Completed (rows=" + n + ")");
        } catch (SQLException e) {
            System.out.println("⚠️ updateContractStatus: " + e.getMessage());
        }
    }

    static void deleteContract(Connection con, int contractId) {
        String sql = "DELETE FROM contract WHERE contract_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, contractId);
            int n = ps.executeUpdate();
            System.out.println("🧹 Deleted contract " + contractId + " (rows=" + n + ")");
        } catch (SQLException e) {
            System.out.println("⚠️ deleteContract: " + e.getMessage());
        }
    }

    // ----- conversation/message (FKs) -----

    static Integer ensureConversation(Connection con, int serviceId, int senderId, int travelerId) {
        // Try to find existing conversation
        String find = """
            SELECT conversation_id FROM conversation
            WHERE service_id=? AND sender_id=? AND traveler_id=? LIMIT 1
            """;
        try (PreparedStatement ps = con.prepareStatement(find)) {
            ps.setInt(1, serviceId);
            ps.setInt(2, senderId);
            ps.setInt(3, travelerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("ℹ️ Using existing conversation_id=" + id);
                    return id;
                }
            }
        } catch (SQLException ignored) {}

        // Create new
        String insert = """
            INSERT INTO conversation(service_id, sender_id, traveler_id, created_at, last_message_at, status)
            VALUES(?, ?, ?, NOW(), NULL, 'open')
            """;
        try (PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, serviceId);
            ps.setInt(2, senderId);
            ps.setInt(3, travelerId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✅ Created conversation_id=" + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ ensureConversation: " + e.getMessage());
        }
        return null;
    }

    static Integer insertMessage(Connection con, int conversationId, int authorUserId, String body) {
        String sql = """
            INSERT INTO message(conversation_id, author_user_id, body, sent_at)
            VALUES (?, ?, ?, NOW())
            """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, conversationId);
            ps.setInt(2, authorUserId);
            ps.setString(3, body);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✅ Inserted message_id=" + id + " (conv=" + conversationId + ")");
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ insertMessage: " + e.getMessage());
        }
        return null;
    }

    static void deleteMessage(Connection con, int messageId) {
        String sql = "DELETE FROM message WHERE message_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            int n = ps.executeUpdate();
            System.out.println("🧹 Deleted message " + messageId + " (rows=" + n + ")");
        } catch (SQLException e) {
            System.out.println("⚠️ deleteMessage: " + e.getMessage());
        }
    }

    static void deleteConversation(Connection con, int conversationId) {
        String sql = "DELETE FROM conversation WHERE conversation_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            int n = ps.executeUpdate();
            System.out.println("🧹 Deleted conversation " + conversationId + " (rows=" + n + ")");
        } catch (SQLException e) {
            System.out.println("⚠️ deleteConversation (probably has existing messages or constraints): " + e.getMessage());
        }
    }
}
