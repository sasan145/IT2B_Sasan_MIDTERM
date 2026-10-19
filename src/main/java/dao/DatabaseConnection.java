package com.hotel.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the SQLite connection and one-time table setup.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:scenario.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initialize() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS reservations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "guest_name TEXT NOT NULL, "
                + "room_type TEXT NOT NULL CHECK (room_type IN ('SINGLE','DOUBLE','SUITE')), "
                + "nightly_rate REAL NOT NULL, "
                + "payment TEXT NOT NULL CHECK (payment IN ('PAID','PENDING'))"
                + ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}
