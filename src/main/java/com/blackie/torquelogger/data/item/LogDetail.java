package com.blackie.torquelogger.data.item;

public class LogDetail {
    public static final String
            //List of Torque pids we care about specifically
            SESSION = "session",
            TIMESTAMP = "time",
            GPS_LAT = "kff1006",
            GPS_LONG = "kff1005",
            GPS_ACC = "kff1239";

    private int id;
    private int logId;
    private String pid;

    public LogDetail(int id, int logId, String pid, String value) {
        this(pid, value);
        this.id = id;
        this.logId = logId;
    }

    private String value;

    public LogDetail (String pid, String value) {
        this.pid = pid;
        this.value = value;
    }



    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
