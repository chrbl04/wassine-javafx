package dao;

import model.Service;
import util.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceDao {

    // For now: "approved = all"
    // If you want to enforce approval existence, use the JOIN version below.
    public List<Service> findAllApproved() {
        String sql = """
            SELECT
                service_id, title, description, parcel_weight_kg, category,
                pickup_address, dropoff_address, pickup_time_from, pickup_time_until,
                delivery_deadline, insurance_requested, offer_price, status, created_at
            FROM services
            ORDER BY created_at DESC
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Service> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Load services failed", e);
        }
    }

    // If later you want approval enforced:
    // SELECT s.* FROM services s JOIN service_approval sa ON sa.service_id=s.service_id ORDER BY s.created_at DESC;

    private Service map(ResultSet rs) throws Exception {
        Service s = new Service();
        s.setServiceId(rs.getInt("service_id"));
        s.setTitle(rs.getString("title"));
        s.setDescription(rs.getString("description"));
        s.setParcelWeightKg(rs.getDouble("parcel_weight_kg"));
        s.setCategory(rs.getString("category"));
        s.setPickupAddress(rs.getString("pickup_address"));
        s.setDropoffAddress(rs.getString("dropoff_address"));

        s.setPickupTimeFrom(tsToLdt(rs.getTimestamp("pickup_time_from")));
        s.setPickupTimeUntil(tsToLdt(rs.getTimestamp("pickup_time_until")));
        s.setDeliveryDeadline(tsToLdt(rs.getTimestamp("delivery_deadline")));

        s.setInsuranceRequested(rs.getBoolean("insurance_requested"));
        s.setOfferPrice(rs.getDouble("offer_price"));
        s.setStatus(rs.getString("status"));
        s.setCreatedAt(tsToLdt(rs.getTimestamp("created_at")));
        return s;
    }

    private LocalDateTime tsToLdt(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
