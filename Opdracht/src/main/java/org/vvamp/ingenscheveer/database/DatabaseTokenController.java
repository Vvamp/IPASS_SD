package org.vvamp.ingenscheveer.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// I didn't know storing JWT's in a db was bad practice and wouldn't work after a restart due to the token signature not matching
// I decided to leave this in since it still works and it was more work to remove it again
// I do clear the database when the app closes to make sure tokens only stay valid when no reboots are done
public class DatabaseTokenController {
    private static List<String> tokenDataList = new ArrayList<>();
    private boolean isDirty = true;

    public void writeToken(String token) {
        String sql = "INSERT IGNORE INTO Tokens (token) VALUES (?)";
        tokenDataList.add(token);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, token);
            pstmt.addBatch();

            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeToken(String token) {
        String sql = "DELETE FROM Tokens WHERE token = ?";
        tokenDataList.remove(token);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, token);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> getAllTokens(boolean forceUpdate) {
        if (!isDirty && !forceUpdate && !tokenDataList.isEmpty()) {
            return tokenDataList;
        }

        String sql = "SELECT * FROM Tokens";
        List<String> tokens = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String token = rs.getString("token");
                tokens.add(token);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tokenDataList = tokens;
        isDirty = false;
        return tokens;
    }

    public void removeAllTokens() {
        String sql = "DELETE FROM Tokens";
        tokenDataList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllTokens() {
        return getAllTokens(false);
    }

}
