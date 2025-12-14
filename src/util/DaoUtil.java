package util;

import java.sql.*;
import java.time.LocalDateTime;

public class DaoUtil {

    public static LocalDateTime getLdt(ResultSet rs, String col) throws SQLException {
        Timestamp t = rs.getTimestamp(col);
        return t != null ? t.toLocalDateTime() : null;
    }

    public static void setTs(PreparedStatement ps, int idx, LocalDateTime ldt) throws SQLException {
        ps.setTimestamp(idx, ldt == null ? null : Timestamp.valueOf(ldt));
    }
}
