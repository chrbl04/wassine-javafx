// dao/VehicleDao.java
package dao;

import model.Vehicle;
import util.Database;

import java.sql.*;
import java.util.*;

public class VehicleDao {
    private static Vehicle map(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("vehicle_id"),
                rs.getInt("traveler_id"),
                Vehicle.VehicleType.valueOf(rs.getString("vehicle_type")),
                rs.getString("make"),
                rs.getString("model"),
                rs.getString("vehicle_number"),
                (Double) (rs.getObject("capacity_weight") != null ? rs.getDouble("capacity_weight") : null),
                Vehicle.Status.valueOf(rs.getString("status").replace(' ', '_'))
        );
    }

    public Vehicle findById(int id) throws SQLException {
        try (var c = Database.getConnection();
             var ps = c.prepareStatement("SELECT * FROM vehicle WHERE vehicle_id=?")) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public List<Vehicle> listByTraveler(int travelerId) throws SQLException {
        try (var c = Database.getConnection();
             var ps = c.prepareStatement("SELECT * FROM vehicle WHERE traveler_id=? ORDER BY vehicle_id DESC")) {
            ps.setInt(1, travelerId);
            try (var rs = ps.executeQuery()) {
                List<Vehicle> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public int insert(Vehicle v) throws SQLException {
        String sql = """
          INSERT INTO vehicle(traveler_id,vehicle_type,make,model,vehicle_number,capacity_weight,status)
          VALUES (?,?,?,?,?,?,?)
        """;
        try (var c = Database.getConnection();
             var ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, v.getTravelerId());
            ps.setString(2, v.getVehicleType().name());
            ps.setString(3, v.getMake());
            ps.setString(4, v.getModel());
            ps.setString(5, v.getVehicleNumber());
            if (v.getCapacityWeight()==null) ps.setNull(6, Types.DECIMAL); else ps.setDouble(6, v.getCapacityWeight());
            ps.setString(7, v.getStatus().name().replace('_',' '));
            ps.executeUpdate();
            try (var k = ps.getGeneratedKeys()) { return k.next() ? k.getInt(1) : 0; }
        }
    }
}
