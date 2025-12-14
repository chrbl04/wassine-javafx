// dao/StarDao.java
package dao;

import model.Star;
import util.Database;
import util.DaoUtil;

import java.sql.*;

public class StarDao {
    public boolean insert(Star s) throws SQLException {
        String sql = "INSERT INTO star(service_id,traveler_id,star_date) VALUES (?,?,?)";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getServiceId()); ps.setInt(2, s.getTravelerId());
            DaoUtil.setTs(ps, 3, s.getStarDate());
            return ps.executeUpdate()==1;
        }
    }
    public boolean delete(int serviceId, int travelerId) throws SQLException {
        try (var c = Database.getConnection();
             var ps = c.prepareStatement("DELETE FROM star WHERE service_id=? AND traveler_id=?")) {
            ps.setInt(1, serviceId); ps.setInt(2, travelerId);
            return ps.executeUpdate()==1;
        }
    }
}
