package org.vvamp.ingenscheveer.database;


import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.vvamp.ingenscheveer.models.schedule.ScheduleTask;
import org.vvamp.ingenscheveer.security.authentication.User;
public class DatabaseUserController {
    private static List<User> userDataList = new ArrayList<>();
    private boolean isDirty = true;
    private String tableName;
    public DatabaseUserController(String tableName) {
        this.tableName = tableName;
    }
    public int writeUser(User user) {
        String sql = "INSERT IGNORE INTO "+tableName+" (username, role, password, salt) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)";
        userDataList.add(user);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getRole());
            pstmt.setString(3, user.getPassword());
            pstmt.setBytes(4, user.getSalt());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new RuntimeException("failed to get id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int findUserId(String username) {
        String sql = "SELECT id FROM "+tableName+" WHERE username = '" + username + "'";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
          while(rs.next()){
              return rs.getInt("id");
          }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
    public User findUserById(int id) {
        String sql = "SELECT * FROM "+tableName+" WHERE id = " + id;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while(rs.next()){
                User data = new User(rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getBytes("salt"), rs.getInt("id"));
                return data;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<User> getAllUsers(boolean forceUpdate) {
        if (!isDirty && !forceUpdate && !userDataList.isEmpty()) {
            return userDataList;
        }

        String sql = "SELECT * FROM "+tableName;
        List<User> signals = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User data = new User(rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getBytes("salt"), rs.getInt("id"));
                signals.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        userDataList = signals;
        isDirty = false;
        return signals;
    }

    public void deleteAllUsers(){
        String sql = "DELETE FROM "+tableName;
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        userDataList.clear();
    }

    public void deleteUserById(int id){
        String sql = "DELETE FROM "+tableName + " WHERE id = " + id;
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getAllUsers(true); // refresh
     }

    public List<User> getAllUsers() {
        return getAllUsers(false);
    }

}
