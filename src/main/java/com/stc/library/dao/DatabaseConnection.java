package com.stc.library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    public static String URL = "jdbc:sqlite:library.db";

    public static Connection getConnection() throws SQLException {
        try {
            // Explicitly load the SQLite JDBC driver (fixes some VS Code issues)
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Database error: SQLite JDBC driver not found. " + e.getMessage());
        }
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS books ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL, "
                + "author TEXT NOT NULL, "
                + "published_year INTEGER, "
                + "edition INTEGER, "
                + "publisher TEXT, "
                + "copies INTEGER NOT NULL, "
                + "is_available BOOLEAN, "
                + "isbn TEXT"
                + ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Success: SQLite database and books table are ready.");
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to create table. " + e.getMessage());
        }
    }
}
