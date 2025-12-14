package dao;

import model.Service;
import util.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceDao {

    // ---------- Mapping helper ----------
    private static Service map(ResultSet rs) throws SQLException {
        return new Service(
                rs.getInt("service_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDouble("parcel_weight_kg"),
                rs.getString("category"),
                rs.getString("pickup_address"),
                rs.getString("dropoff_address"),
                getLdt(rs, "pickup_time_from"),
                getLdt(rs, "pickup_time_until"),
                getLdt(rs, "delivery_deadline"),
                rs.getBoolean("insurance_requested"),
                rs.getDouble("offer_price"),
                rs.getString("status"),
                getLdt(rs, "created_at")
        );
    }

    private static LocalDateTime getLdt(ResultSet rs, String col) throws SQLException {
        Timestamp ts = rs.getTimestamp(col);
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private static void setTs(PreparedStatement ps, int idx, LocalDateTime ldt) throws SQLException {
        if (ldt == null) ps.setTimestamp(idx, null);
        else ps.setTimestamp(idx, Timestamp.valueOf(ldt));
    }

    // ---------- Queries ----------

    public List<Service> findAll(int limit) throws SQLException {
        String sql = "SELECT * FROM service ORDER BY created_at DESC LIMIT ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Service> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public List<Service> search(String q, String status, String category, int limit) throws SQLException {
        String sql = """
            SELECT * FROM service
            WHERE (? IS NULL OR title LIKE CONCAT('%',?,'%') OR description LIKE CONCAT('%',?,'%'))
              AND (? IS NULL OR status = ?)
              AND (? IS NULL OR category = ?)
            ORDER BY created_at DESC
            LIMIT ?
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, q); ps.setString(2, q); ps.setString(3, q);
            ps.setString(4, status); ps.setString(5, status);
            ps.setString(6, category); ps.setString(7, category);
            ps.setInt(8, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Service> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public Service findById(int id) throws SQLException {
        String sql = "SELECT * FROM service WHERE service_id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public int insert(Service s) throws SQLException {
        String sql = """
            INSERT INTO service
                (title, description, parcel_weight_kg, category, pickup_address,
                 dropoff_address, pickup_time_from, pickup_time_until, delivery_deadline,
                 insurance_requested, offer_price, status)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getTitle());
            ps.setString(2, s.getDescription());
            ps.setDouble(3, s.getParcelWeightKg());
            ps.setString(4, s.getCategory());
            ps.setString(5, s.getPickupAddress());
            ps.setString(6, s.getDropoffAddress());
            setTs(ps, 7, s.getPickupTimeFrom());
            setTs(ps, 8, s.getPickupTimeUntil());
            setTs(ps, 9, s.getDeliveryDeadline());
            ps.setBoolean(10, s.isInsuranceRequested());
            ps.setDouble(11, s.getOfferPrice());
            ps.setString(12, s.getStatus());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    public void update(Service s) throws SQLException {
        String sql = """
            UPDATE service SET
                title=?, description=?, parcel_weight_kg=?, category=?, pickup_address=?,
                dropoff_address=?, pickup_time_from=?, pickup_time_until=?, delivery_deadline=?,
                insurance_requested=?, offer_price=?, status=?
            WHERE service_id=?
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getTitle());
            ps.setString(2, s.getDescription());
            ps.setDouble(3, s.getParcelWeightKg());
            ps.setString(4, s.getCategory());
            ps.setString(5, s.getPickupAddress());
            ps.setString(6, s.getDropoffAddress());
            setTs(ps, 7, s.getPickupTimeFrom());
            setTs(ps, 8, s.getPickupTimeUntil());
            setTs(ps, 9, s.getDeliveryDeadline());
            ps.setBoolean(10, s.isInsuranceRequested());
            ps.setDouble(11, s.getOfferPrice());
            ps.setString(12, s.getStatus());
            ps.setInt(13, s.getServiceId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM service WHERE service_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
