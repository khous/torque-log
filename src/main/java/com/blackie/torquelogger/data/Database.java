package com.blackie.torquelogger.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database ourInstance = new Database();
    private Connection conn;
    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + System.getenv("TQ_DB_HOST") + "/torque?user=" +
                            System.getenv("TQ_DB_UN") + "&password=" + System.getenv("TQ_DB_PW")
            );
        } catch (Exception ex) {
            // handle the error
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public Connection getConnection () {
        return conn;
    }
}
