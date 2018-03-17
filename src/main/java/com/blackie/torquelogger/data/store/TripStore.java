package com.blackie.torquelogger.data.store;

import com.blackie.torquelogger.data.item.LogDetail;
import com.blackie.torquelogger.data.item.LogItem;
import com.blackie.torquelogger.data.item.TripItem;
import com.sun.rowset.internal.Row;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class TripStore extends Store {

    /**
     * Get a fully hydrated trip object
     * @return The trip matching the id or null if not found
     */
    public TripItem getTrip (final int id) {
        Connection con = getDb().getConnection();
        try {
            PreparedStatement p = con.prepareStatement(
                    "SELECT * FROM full_log " +
                            "WHERE tripId = ? " +
                            "ORDER BY logId, detailId ASC;"
            );

            p.setInt(1, id);
            ResultSet rs = p.executeQuery();

            TripItem t = null;
            boolean first = true;
            int lastLog = -1;
            LogItem li = null;

            while (rs.next()) {
                //For the first record, instantiate the trip
                if (first) {
                    t = new TripItem(
                            rs.getInt("tripId"),
                            rs.getDate("start"),
                            rs.getDate("end"),
                            rs.getString("vehicle"),
                            rs.getString("session")
                        );
                    first = false;
                }

                //If we step to a new log, create a new log item
                int logId = rs.getInt("logId");
                if (logId > lastLog) {
                    lastLog = logId;
                    //create a new log
                    //but first add the last log into the trip's logs
                    if (li != null) {
                        t.getLogs().add(li);
                    }

                    li = new LogItem(
                            rs.getInt("logId"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude"),
                            rs.getString("session"),
                            rs.getTimestamp("ts")
                    );

                }

                //Always add the row as a new log detail
                li.getDetails().add(new LogDetail(
                        rs.getInt("detailId"),
                        logId,
                        rs.getString("pid"),
                        rs.getString("value")
                ));

            }

            return t;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get trips which are not hydrated
     * @param start The starting trip to get, so zero for page one
     * @param count The number of trips to retrieve
     * @return A list of non hydrated trips
     */
    public Collection<TripItem> getTrips (final int start, final int count) {
        Connection con = getDb().getConnection();
        try {
            PreparedStatement p = con.prepareStatement(
                    "SELECT * FROM full_log " +
                            "ORDER BY tripId ASC " +
                            "LIMIT ?,?;"
            );

            p.setInt(1, start);
            p.setInt(2, count);

            ResultSet rs = p.executeQuery();

            ArrayList<TripItem> trips = new ArrayList<>();

            while (rs.next()) {
                trips.add(new TripItem(//TODO make these constants please thank you
                        rs.getInt("id"),
                        rs.getDate("start"),
                        rs.getDate("end"),
                        rs.getString("vehicle"),
                        rs.getString("session")
                    )
                );
            }

            return trips;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //No real way to save a trip, but maybe we can annotate trips with some metadata, like description: "I was
    //running from the cops"
}
