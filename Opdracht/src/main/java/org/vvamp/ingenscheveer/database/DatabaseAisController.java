package org.vvamp.ingenscheveer.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.vvamp.ingenscheveer.database.models.AisData;
import org.vvamp.ingenscheveer.models.json.AisSignal;
import org.vvamp.ingenscheveer.models.json.PositionReport;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAisController {
    private static List<AisData> aisDataList = new ArrayList<>();
    private boolean isDirty = true;
    private String tableName;
    public DatabaseAisController(String tableName) {
        this.tableName = tableName;
    }
    public AisData convertToAisData(AisSignal signal) {
        String unclean = signal.getMetaData().getTime_utc();
        String clean = unclean.replaceAll("\\s\\+[0-9]+\\sUTC", "").replace(" ", "T");

        LocalDateTime ltd = LocalDateTime.parse(clean);
        Timestamp ts = Timestamp.from(ltd.toInstant(ZoneOffset.UTC));

        ObjectMapper mapper = JsonMapper.builder().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false).build();

        try {
            PositionReport pr = signal.getMessage().getPositionReport();
            AisData aisData = new AisData(ts, pr.getSog(), pr.getLongitude(), pr.getLatitude(), mapper.writeValueAsString(signal));
            return aisData;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AisData> getXMostRecentData(int count){
        if (isDirty || aisDataList.isEmpty()) {
            getAllAisData();
        }
        if(count <= -1){
            count= aisDataList.size();
        }
        return aisDataList.subList(Math.max(aisDataList.size()-count, 0), aisDataList.size());
    }


    public void removeAll(){
        String sql = "DELETE FROM " + tableName;
        aisDataList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeAisData(AisData signal) {
        ObjectMapper mapper = JsonMapper.builder().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false).build();
        try {
            AisSignal s = mapper.readValue(signal.getRaw_json(), AisSignal.class);
            writeAisData(s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void writeAisData(AisSignal signal) {
        String sql = "INSERT IGNORE INTO " + tableName + " (timestamp, sog, longitude, latitude, raw_json) VALUES (?, ?, ?, ?, ?)";
        AisData dataItem = convertToAisData(signal);
        aisDataList.add(dataItem);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            long millis = dataItem.getTimestamp().getTime() / 1000 * 1000 + (dataItem.getTimestamp().getNanos() / 1000000) / 1 * 1;
            dataItem.getTimestamp().setTime(millis);
            dataItem.getTimestamp().setNanos((dataItem.getTimestamp().getNanos() / 1000000) * 1000000);
            pstmt.setTimestamp(1, dataItem.getTimestamp()); // flooring timestamp so it doesn't round it in db
            pstmt.setDouble(2, dataItem.getSog());
            pstmt.setDouble(3, dataItem.getLongitude());
            pstmt.setDouble(4, dataItem.getLatitude());
            pstmt.setString(5, dataItem.getRaw_json());
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

        String sql = "SELECT * FROM " + tableName + " ORDER BY timestamp";
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

}
