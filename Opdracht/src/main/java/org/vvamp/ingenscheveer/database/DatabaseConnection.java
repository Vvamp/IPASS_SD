package org.vvamp.ingenscheveer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://ls-7f9ed97743e4c2462a9cba2e281c691d588fc281.c3co6m68qdal.eu-central-1.rds.amazonaws.com:3306/veerdienst";
    private static final String USER = "dbmasteruser";
    private static final String PASSWORD = "simplepw";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
