package com.blackie.torquelogger.data.item;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LogItem implements Comparable {
    private int id;
    private Timestamp ts;
    private double lat;
    private double longitude;
    private String session;

    public LogItem () {
        details = new ArrayList<>();
    }

    public LogItem(int id, double lat, double longitude, String session, Timestamp ts) {
        this.id = id;
        this.lat = lat;
        this.longitude = longitude;
        this.session = session;
        this.ts = ts;
        details = new ArrayList<>();
    }

    public LogItem (Collection<LogDetail> details) {
        //Do butt ugly copy to maintain encapsulation
        this.details = Arrays.asList(details.toArray(new LogDetail[details.size()]));
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        accuracy *= 100;
        //convert meters to cm;
        this.accuracy = (int) accuracy;
    }

    public void setDetails(Collection<LogDetail> details) {
        this.details = details;
    }

    private int accuracy;//Need to convert this to CM
    //This is the list of PID/Value tuples
    private Collection<LogDetail> details;



    public Collection<LogDetail> getDetails() {
        return details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    @Override
    public int compareTo (Object li) {
        if (!(li instanceof LogItem)) return 0;

        return this.getTs().compareTo(((LogItem)li).getTs());
    }
}
