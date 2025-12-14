// dao/ServiceApprovalDao.java
package dao;

import model.ServiceApproval;
import util.Database;
import util.DaoUtil;

import java.sql.*;
import java.util.*;

public class ServiceApprovalDao {
    private static ServiceApproval map(ResultSet rs) throws SQLException {
        return new ServiceApproval(
                rs.getInt("admin_id"),
                rs.getInt("service_id"),
                rs.getInt("sender_id"),
                DaoUtil.getLdt(rs, "approved_at")
        );
    }

    public boolean insert(ServiceApproval a) throws SQLException {
        String sql = "INSERT INTO service_approval(admin_id,service_id,sender_id,approved_at) VALUES (?,?,?,?)";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getAdminId()); ps.setInt(2, a.getServiceId()); ps.setInt(3, a.getSenderId());
            DaoUtil.setTs(ps, 4, a.getApprovedAt());
            return ps.executeUpdate() == 1;
        }
    }

    public List<ServiceApproval> listByService(int serviceId) throws SQLException {
        try (var c = Database.getConnection();
             var ps = c.prepareStatement("SELECT * FROM service_approval WHERE service_id=?")) {
            ps.setInt(1, serviceId);
            try (var rs = ps.executeQuery()) {
                List<ServiceApproval> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }
}
