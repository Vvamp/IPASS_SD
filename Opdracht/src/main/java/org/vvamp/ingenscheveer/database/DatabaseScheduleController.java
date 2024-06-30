package org.vvamp.ingenscheveer.database;


import org.vvamp.ingenscheveer.models.schedule.ScheduleTask;
import org.vvamp.ingenscheveer.models.schedule.TaskType;
import org.vvamp.ingenscheveer.security.authentication.User;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class DatabaseScheduleController {
    private static List<ScheduleTask> scheduleTask = new ArrayList<>();
    private boolean isDirty = true;
    private String tableName;
    public DatabaseScheduleController(String tableName) {
        this.tableName = tableName;
    }
    public int writeScheduleTask(ScheduleTask task) {
        String sql = "INSERT IGNORE INTO "+tableName+" (start, end, userId, taskType) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)";
        scheduleTask.add(task);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, Timestamp.from(task.getStart().toInstant(ZoneOffset.UTC)));
            pstmt.setTimestamp(2, Timestamp.from(task.getEnd().toInstant(ZoneOffset.UTC)));
            int userId = DatabaseStorageController.getDatabaseUserController().findUserId(task.getUsername());
            if (userId == -1) {
                throw new RuntimeException("User not found");
            }
            pstmt.setInt(3, userId);
            pstmt.setString(4, task.getType().toString());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new RuntimeException("failed to get id");
//                    return -1;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ScheduleTask> getTaskForUser(int userId) {
        if (isDirty || scheduleTask.isEmpty()) {
            getAllScheduleTask();
        }

        String sql = "SELECT * FROM "+tableName+" WHERE userId = " + userId;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            List<ScheduleTask> tasks = new ArrayList<>();
            User user = DatabaseStorageController.getDatabaseUserController().findUserById(userId);
            while (rs.next()) {
                int id = rs.getInt("id");
                ScheduleTask data = new ScheduleTask(rs.getTimestamp("start").toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(), rs.getTimestamp("end").toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(), user, TaskType.valueOf(rs.getString("taskType")), id);
                tasks.add(data);
            }
//            System.out.println("get tasks for user " + userId + ", results in " + tasks.size() + " tasks");
            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllScheduleTasks() {
        String sql = "DELETE FROM "+tableName;
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        scheduleTask.clear();

    }

    public void deleteScheduleTask(int id) {
        String sql = "DELETE FROM "+tableName+" WHERE id = " + id;
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(ScheduleTask task : scheduleTask) {
            if(task.getUuid() == id){
                scheduleTask.remove(task);
                break;
            }
        }
    }

    public List<ScheduleTask> getAllScheduleTask(boolean forceUpdate) {
        if (!isDirty && !forceUpdate && !scheduleTask.isEmpty()) {
            return scheduleTask;
        }

        String sql = "SELECT * FROM "+tableName;
        List<ScheduleTask> signals = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userId");
                User user = DatabaseStorageController.getDatabaseUserController().findUserById(userId);
                ScheduleTask data = new ScheduleTask(rs.getTimestamp("start").toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(), rs.getTimestamp("end").toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(), user, TaskType.valueOf(rs.getString("taskType")), id);
                signals.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        scheduleTask = signals;
        isDirty = false;
        return signals;
    }

    public List<ScheduleTask> getAllScheduleTask() {
        return getAllScheduleTask(false);
    }

}
