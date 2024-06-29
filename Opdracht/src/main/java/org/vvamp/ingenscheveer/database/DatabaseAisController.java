package org.vvamp.ingenscheveer.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.database.models.AisData;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAisController {
    private static List<AisData> aisDataList = new ArrayList<>();
    private boolean isDirty = true;
    private AisData convertToAisData(AisSignal signal) {
        String unclean = signal.metaData.time_utc;
        String clean = unclean.replaceAll("\\s\\+[0-9]+\\sUTC", "").replace(" ", "T");

        LocalDateTime ltd = LocalDateTime.parse(clean);
        Timestamp ts = Timestamp.from(ltd.toInstant(ZoneOffset.UTC));

        ObjectMapper mapper = new ObjectMapper();

        try {
            AisData aisData = new AisData(ts, signal.message.positionReport.sog, signal.message.positionReport.longitude, signal.message.positionReport.latitude, mapper.writeValueAsString(signal));
            return aisData;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private AisSignal convertFromAisData(AisData data) {
        ObjectMapper mapper = new ObjectMapper();
        AisSignal signal = null;
        try {
            signal = mapper.readValue(data.raw_json, AisSignal.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return signal;
    }


    public void SaveAllAisSignals(List<AisSignal> signals) {
        String sql = "INSERT IGNORE INTO AisSignals (timestamp, sog, longitude, latitude, raw_json) VALUES (?, ?, ?, ?, ?)";
        List<AisData> datas = new ArrayList<>();
        for (AisSignal signal : signals) {
            datas.add(convertToAisData(signal));
        }

        System.out.println("Saving " + datas.size() + " AisSignals");

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (AisData data : datas) {
                pstmt.setTimestamp(1, data.timestamp);
                pstmt.setDouble(2, data.sog);
                pstmt.setDouble(3, data.longitude);
                pstmt.setDouble(4, data.latitude);
                pstmt.setString(5, data.raw_json);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void writeAisData(AisSignal signal) {
        String sql = "INSERT IGNORE INTO AisSignals (timestamp, sog, longitude, latitude, raw_json) VALUES (?, ?, ?, ?, ?)";
        AisData dataItem = convertToAisData(signal);
        aisDataList.add(dataItem);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, dataItem.timestamp);
            pstmt.setDouble(2, dataItem.sog);
            pstmt.setDouble(3, dataItem.longitude);
            pstmt.setDouble(4, dataItem.latitude);
            pstmt.setString(5, dataItem.raw_json);
            pstmt.addBatch();

            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AisData> getAllAisData(boolean forceUpdate) {
        if (!isDirty && !forceUpdate && !aisDataList.isEmpty()) {
            return aisDataList;
        }

        String sql = "SELECT * FROM AisSignals";
        List<AisData> signals = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                AisData data = new AisData(rs.getTimestamp("timestamp"), rs.getDouble("sog"), rs.getDouble("longitude"), rs.getDouble("latitude"), rs.getString("raw_json"));
                signals.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        aisDataList = signals;
        isDirty = false;
        return signals;
    }

    public List<AisData> getAllAisData() {
        return getAllAisData(false);
    }

    public List<AisSignal> getAllAisSignals() {
        String sql = "SELECT * FROM AisSignals";
        List<AisSignal> signals = new ArrayList<>();
        List<AisData> data = getAllAisData();
        for (AisData dataSignal : data) {
            signals.add(convertFromAisData(dataSignal));
        }

        return signals;
    }
}
