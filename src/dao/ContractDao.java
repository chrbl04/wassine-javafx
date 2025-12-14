package dao;

import model.Contract;
import util.Database;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContractDao {

    // ---- helpers ----
    private static LocalDateTime getLdt(ResultSet rs, String col) throws SQLException {
        Timestamp t = rs.getTimestamp(col);
        return t != null ? t.toLocalDateTime() : null;
    }
    private static void setTs(PreparedStatement ps, int idx, LocalDateTime ldt) throws SQLException {
        ps.setTimestamp(idx, ldt == null ? null : Timestamp.valueOf(ldt));
    }

    private static Contract map(ResultSet rs) throws SQLException {
        return new Contract(
                rs.getInt("contract_id"),
                rs.getInt("vehicle_id"),
                getLdt(rs, "created_at"),
                rs.getBigDecimal("transport_fee"),
                getLdt(rs, "pickup_date"),
                getLdt(rs, "delivery_date"),
                getLdt(rs, "signed_date"),
                rs.getString("terms_text"),
                Contract.PaymentMethod.valueOf(rs.getString("payment_method")),
                Contract.Payment.valueOf(rs.getString("payment")),
                Contract.Status.valueOf(rs.getString("status"))
        );
    }

    // ---- queries ----
    public List<Contract> findAll(int limit) throws SQLException {
        String sql = "SELECT * FROM contract ORDER BY created_at DESC LIMIT ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Contract> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public Contract findById(int id) throws SQLException {
        String sql = "SELECT * FROM contract WHERE contract_id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Contract> listByVehicle(int vehicleId, int limit) throws SQLException {
        String sql = "SELECT * FROM contract WHERE vehicle_id=? ORDER BY created_at DESC LIMIT ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Contract> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public int insert(Contract ct) throws SQLException {
        String sql = """
            INSERT INTO contract
              (vehicle_id, created_at, transport_fee, pickup_date, delivery_date, signed_date,
               terms_text, payment_method, payment, status)
            VALUES (?,?,?,?,?,?,?,?,?,?)
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ct.getVehicleId());
            setTs(ps, 2, ct.getCreatedAt());                        // you can pass null to use DB default
            ps.setBigDecimal(3, ct.getTransportFee());
            setTs(ps, 4, ct.getPickupDate());
            setTs(ps, 5, ct.getDeliveryDate());
            setTs(ps, 6, ct.getSignedDate());
            ps.setString(7, ct.getTermsText());
            ps.setString(8, ct.getPaymentMethod().name());
            ps.setString(9, ct.getPayment().name());
            ps.setString(10, ct.getStatus().name());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    public boolean update(Contract ct) throws SQLException {
        String sql = """
            UPDATE contract SET
              vehicle_id=?, transport_fee=?, pickup_date=?, delivery_date=?, signed_date=?,
              terms_text=?, payment_method=?, payment=?, status=?
            WHERE contract_id=?
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, ct.getVehicleId());
            ps.setBigDecimal(2, ct.getTransportFee());
            setTs(ps, 3, ct.getPickupDate());
            setTs(ps, 4, ct.getDeliveryDate());
            setTs(ps, 5, ct.getSignedDate());
            ps.setString(6, ct.getTermsText());
            ps.setString(7, ct.getPaymentMethod().name());
            ps.setString(8, ct.getPayment().name());
            ps.setString(9, ct.getStatus().name());
            ps.setInt(10, ct.getContractId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM contract WHERE contract_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}
