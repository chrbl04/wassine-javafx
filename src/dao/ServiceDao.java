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
            u.first_name, u.last_name, u.email, u.phone,
            s1.service_id, s1.title, s1.description, s1.parcel_weight_kg, s1.category,
            s1.pickup_address, s1.dropoff_address, s1.pickup_time_from, s1.pickup_time_until,
            s1.delivery_deadline, s1.insurance_requested, s1.offer_price, s1.created_at,

            si_primary.image_url AS primary_image_url

        FROM (
            SELECT * FROM service NATURAL JOIN service_approval
        ) AS s1
        JOIN users u ON u.user_id = s1.sender_id

        LEFT JOIN (
            SELECT t.service_id, t.image_url
            FROM service_image t
            JOIN (
                SELECT service_id, MIN(rank_key) AS best_rank
                FROM (
                    SELECT
                        service_id,
                        image_url,
                        CONCAT(
                            IF(is_primary = 1, '0', '1'), '-',
                            LPAD(COALESCE(sort_order, 9999), 4, '0'), '-',
                            DATE_FORMAT(COALESCE(uploaded_at, '2100-01-01 00:00:00'), '%Y%m%d%H%i%s')
                        ) AS rank_key
                    FROM service_image
                ) ranked
                GROUP BY service_id
            ) best ON best.service_id = t.service_id
            WHERE CONCAT(
                IF(t.is_primary = 1, '0', '1'), '-',
                LPAD(COALESCE(t.sort_order, 9999), 4, '0'), '-',
                DATE_FORMAT(COALESCE(t.uploaded_at, '2100-01-01 00:00:00'), '%Y%m%d%H%i%s')
            ) = best.best_rank
        ) si_primary ON si_primary.service_id = s1.service_id

        ORDER BY s1.created_at DESC
    """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Service> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Load service failed", e);
        }
    }


    // If later you want approval enforced:
    // SELECT s.* FROM service s JOIN service_approval sa ON sa.service_id=s.service_id ORDER BY s.created_at DESC;

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
        s.setCreatedAt(tsToLdt(rs.getTimestamp("created_at")));
        return s;
    }



    private LocalDateTime tsToLdt(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
