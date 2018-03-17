package com.blackie.torquelogger.data.item;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.Date;
import java.util.List;

public class TripItem {

    private int id;
    private Date start;
    private Date end;
    private String vehicle;
    private String sessionId;
    private List<LogItem> logs;

    public TripItem(int id, Date start, Date end, String vehicle, String sessionId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.vehicle = vehicle;
        this.sessionId = sessionId;
        logs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<LogItem> getLogs() {
        return logs;
    }

    public void setLogs(List<LogItem> logs) {
        this.logs = logs;
    }
}
