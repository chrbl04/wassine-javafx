// dao/RateDao.java
package dao;

import model.Rate;
import util.Database;
import util.DaoUtil;

import java.sql.*;

public class RateDao {
    public boolean insert(Rate r) throws SQLException {
        String sql = "INSERT INTO rate(sender_id,contract_id,score,comment,date_given) VALUES (?,?,?,?,?)";
        try (var c = Database.getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getSenderId()); ps.setInt(2, r.getContractId());
            ps.setInt(3, r.getScore()); ps.setString(4, r.getComment());
            DaoUtil.setTs(ps, 5, r.getDateGiven());
            return ps.executeUpdate()==1;
        }
    }
}
