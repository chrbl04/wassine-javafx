package dao;

import model.ServiceImage;
import util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceImageDao {

    /**
     * Returns the primary image URL for a service.
     * - First tries is_primary = 1
     * - If none, falls back to lowest sort_order
     * - If still none, returns null
     */
    public String findPrimaryUrlByServiceId(int serviceId) {
        String sql = """
            SELECT image_url
            FROM service_image
            WHERE service_id = ?
            ORDER BY is_primary DESC, sort_order ASC, uploaded_at DESC
            LIMIT 1
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, serviceId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("image_url") : null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Load primary service image failed", e);
        }
    }

    public List<ServiceImage> findByServiceId(int serviceId) {
        String sql = """
            SELECT image_id, service_id, image_url, caption, is_primary, sort_order, uploaded_at
            FROM service_image
            WHERE service_id = ?
            ORDER BY is_primary DESC, sort_order ASC, uploaded_at DESC
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, serviceId);

            try (ResultSet rs = ps.executeQuery()) {
                List<ServiceImage> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }

        } catch (Exception e) {
            throw new RuntimeException("Load service images failed", e);
        }
    }

    public ServiceImage findPrimaryByServiceId(int serviceId) {
        String sql = """
            SELECT image_id, service_id, image_url, caption, is_primary, sort_order, uploaded_at
            FROM service_image
            WHERE service_id = ?
            ORDER BY is_primary DESC, sort_order ASC, uploaded_at DESC
            LIMIT 1
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, serviceId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Load primary service image failed", e);
        }
    }

    private ServiceImage map(ResultSet rs) throws Exception {
        ServiceImage si = new ServiceImage();
        si.setImageId(rs.getInt("image_id"));
        si.setServiceId(rs.getInt("service_id"));
        si.setImageUrl(rs.getString("image_url"));
        si.setCaption(rs.getString("caption"));
        si.setPrimary(rs.getBoolean("is_primary"));
        si.setSortOrder(rs.getInt("sort_order"));
        si.setUploadedAt(tsToLdt(rs.getTimestamp("uploaded_at")));
        return si;
    }

    private LocalDateTime tsToLdt(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
