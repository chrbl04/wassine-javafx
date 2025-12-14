// dao/MakeContractDao.java
package dao;

import model.MakeContract;
import util.Database;

import java.sql.*;
import java.util.*;

public class MakeContractDao {
    private static MakeContract map(ResultSet rs) throws SQLException {
        return new MakeContract(
                rs.getInt("service_id"),
                rs.getInt("sender_id"),
                rs.getInt("traveler_id"),
                rs.getInt("admin_id"),
                rs.getInt("contract_id")
        );
    }

    public boolean insert(MakeContract mc) throws SQLException {
        String sql = "INSERT INTO make_contract(service_id,sender_id,traveler_id,admin_id,contract_id) VALUES (?,?,?,?,?)";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setInt(1, mc.getServiceId()); ps.setInt(2, mc.getSenderId());
            ps.setInt(3, mc.getTravelerId()); ps.setInt(4, mc.getAdminId()); ps.setInt(5, mc.getContractId());
            return ps.executeUpdate()==1;
        }
    }

    public List<MakeContract> listByContract(int contractId) throws SQLException {
        try (var c = Database.getConnection();
             var ps = c.prepareStatement("SELECT * FROM make_contract WHERE contract_id=?")) {
            ps.setInt(1, contractId);
            try (var rs = ps.executeQuery()) {
                List<MakeContract> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }
}
