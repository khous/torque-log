package com.blackie.torquelogger.data.store;

import com.blackie.torquelogger.data.Database;
import com.blackie.torquelogger.data.item.LogDetail;
import com.blackie.torquelogger.data.item.LogItem;

import java.sql.*;
import java.util.Collection;

public class LogStore extends Store{

    public int save (LogItem log) {
        Connection con = getDb().getConnection();
        try {
            con.setAutoCommit(false);
            //Find or save trip
            PreparedStatement s = con.prepareStatement(
                    "INSERT INTO trips " +
                            "(end, vehicle, session)" +
                            "VALUES (NOW(), '86', ?)" +
                            "ON DUPLICATE KEY UPDATE end = NOW()"
            );
            s.setString(1, log.getSession());
            s.execute();

            //save log
            s = con.prepareStatement(
                    "INSERT INTO logs (latitude, longitude, accuracy, tripId)" +
                    "VALUES (?, ?, ?, (SELECT id FROM trips WHERE session = ?))"
            );
            s.setDouble(1, log.getLat());
            s.setDouble(2, log.getLongitude());
            s.setInt(3, log.getAccuracy());
//            s.setDate(4, log.getTs());
            s.setString(4, log.getSession());
            s.execute();
            ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID() as log_id");
            rs.first();
            int logId = rs.getInt(1);

            //Save its details
            s = con.prepareStatement("INSERT INTO log_details (logId, pid, value) VALUES (?,?,?)");
            for (LogDetail ld : log.getDetails()) {
                s.setInt(1, logId);
                s.setString(2, ld.getPid());
                s.setString(3, ld.getValue());
                s.addBatch();
            }

            s.executeBatch();

            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    public int save (Collection<LogItem> items) {
        if (items == null) return 0;

        for (LogItem i : items) {
            this.save(i);
        }

        return 0;
    }
}
