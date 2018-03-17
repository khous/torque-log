package com.blackie.torquelogger.data.store;

import com.blackie.torquelogger.data.Database;

import java.sql.Connection;

public class Store {
    private final Database db;

    public Store () {
        db = Database.getInstance();
    }

    protected Database getDb() {
        return db;
    }
}
