package com.hotel;

import com.hotel.dao.DatabaseConnection;
import com.hotel.ui.ScenarioMenu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.initialize();
        } catch (SQLException e) {
            System.out.println("Could not initialize database: " + e.getMessage());
            return;
        }
        new ScenarioMenu().run();
    }
}

