package org.vvamp.ingenscheveer.database;

import org.vvamp.ingenscheveer.models.Drukte;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDrukteController {
    private static List<Drukte> drukteDataList = new ArrayList<>();
    private boolean isDirty = true;

    public void writeDrukte(Drukte drukte) {
        String sql = "INSERT IGNORE INTO Drukte (severity, time) VALUES (?, ?)";
        drukteDataList.add(drukte);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, drukte.getSeverity());
            pstmt.setTimestamp(2, Timestamp.from(drukte.getTime().toInstant(ZoneOffset.UTC)));
            pstmt.addBatch();

            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Drukte> getAllDrukte(boolean forceUpdate) {
        if (!isDirty && !forceUpdate && !drukteDataList.isEmpty()) {
            return drukteDataList;
        }

        String sql = "SELECT * FROM Drukte ORDER BY time DESC LIMIT 5";
        List<Drukte> signals = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Drukte data = new Drukte(rs.getInt("severity"), rs.getTimestamp("time").toLocalDateTime());
                signals.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        drukteDataList = signals;
        isDirty = false;
        return signals;
    }

    public List<Drukte> getAllDrukte() {
        return getAllDrukte(true);
    }

}
