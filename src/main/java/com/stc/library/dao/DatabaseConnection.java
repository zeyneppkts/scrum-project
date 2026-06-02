package com.stc.library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
                + "isbn TEXT, "
                + "borrow_count INTEGER NOT NULL DEFAULT 0"
                + ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            ensureBorrowCountColumn(conn);
            System.out.println("Success: SQLite database and books table are ready.");
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to create table. " + e.getMessage());
        }
    }

    /**
     * Adds the {@code borrow_count} column to databases that were created before
     * the borrow feature existed. New databases already include the column, so
     * this is a no-op for them.
     */
    private static void ensureBorrowCountColumn(Connection conn) {
        boolean columnExists = false;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("PRAGMA table_info(books)")) {
            while (rs.next()) {
                if ("borrow_count".equalsIgnoreCase(rs.getString("name"))) {
                    columnExists = true;
                    break;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to inspect books table. " + e.getMessage());
            return;
        }

        if (!columnExists) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("ALTER TABLE books ADD COLUMN borrow_count INTEGER NOT NULL DEFAULT 0");
                System.out.println("Migration: Added 'borrow_count' column to books table.");
            } catch (SQLException e) {
                System.err.println("Database Error: Failed to add 'borrow_count' column. " + e.getMessage());
            }
        }
    }
}
