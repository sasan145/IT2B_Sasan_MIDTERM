package com.hotel.dao;

import com.hotel.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * All SQL lives here. Every query uses PreparedStatement.
 */
public class ScenarioDao {

    private static final String INSERT_SQL =
            "INSERT INTO reservations (guest_name, room_type, nightly_rate, payment) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, guest_name, room_type, nightly_rate, payment FROM reservations ORDER BY id";

    public boolean insert(ScenarioEntity entity) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, entity.guestName());
            ps.setString(2, entity.roomType());
            ps.setDouble(3, entity.nightlyRate());
            ps.setString(4, entity.payment());
            return ps.executeUpdate() > 0;
        }
    }

    public List<ScenarioEntity> findAll() throws SQLException {
        List<ScenarioEntity> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ScenarioEntity(
                        rs.getInt("id"),
                        rs.getString("guest_name"),
                        rs.getString("room_type"),
                        rs.getDouble("nightly_rate"),
                        rs.getString("payment")));
            }
        }
        return list;
    }
}
