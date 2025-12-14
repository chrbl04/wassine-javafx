// dao/AcceptDao.java
package dao;

import model.Accept;
import util.Database;
import util.DaoUtil;

import java.sql.*;
import java.util.*;

public class AcceptDao {
    private static Accept map(ResultSet rs) throws SQLException {
        return new Accept(
                rs.getInt("service_id"),
                rs.getInt("traveler_id"),
                DaoUtil.getLdt(rs, "acceptance_date")
        );
    }

    public boolean insert(Accept a) throws SQLException {
        String sql = "INSERT INTO accept(service_id,traveler_id,acceptance_date) VALUES (?,?,?)";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getServiceId()); ps.setInt(2, a.getTravelerId());
            DaoUtil.setTs(ps, 3, a.getAcceptanceDate());
            return ps.executeUpdate()==1;
        }
    }
}

