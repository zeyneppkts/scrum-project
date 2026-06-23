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
                + "isbn TEXT PRIMARY KEY, "
                + "title TEXT NOT NULL, "
                + "author TEXT NOT NULL, "
                + "published_year INTEGER NOT NULL, "
                + "edition INTEGER, "
                + "publisher TEXT, "
                + "copies INTEGER NOT NULL, "
                + "is_available BOOLEAN NOT NULL, "
                + "borrow_count INTEGER DEFAULT 0, "
                + "average_rating REAL DEFAULT 0.0, "
                + "rating_count INTEGER DEFAULT 0"
                + ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            migrateSchema(conn);
            System.out.println("Success: SQLite database and books table are ready.");
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to create table. " + e.getMessage());
        }
    }

    /**
     * Brings databases that were created before the borrow, return and rating
     * features existed up to date by adding any missing columns. New databases
     * already include every column, so each check is a no-op for them.
     *
     * <p>This is required because {@code CREATE TABLE IF NOT EXISTS} never alters
     * an already existing table, so reads/writes against the new columns would
     * otherwise fail with "no such column" and silently lose the change.
     */
    private static void migrateSchema(Connection conn) {
        ensureColumn(conn, "borrow_count", "INTEGER NOT NULL DEFAULT 0");
        ensureColumn(conn, "average_rating", "REAL NOT NULL DEFAULT 0.0");
        ensureColumn(conn, "rating_count", "INTEGER NOT NULL DEFAULT 0");
    }

    /**
     * Adds the given column to the {@code books} table when it is not already
     * present.
     *
     * @param conn       the open database connection
     * @param columnName the column to ensure exists
     * @param definition the SQL type/constraints used when the column is added
     */
    private static void ensureColumn(Connection conn, String columnName, String definition) {
        boolean columnExists = false;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("PRAGMA table_info(books)")) {
            while (rs.next()) {
                if (columnName.equalsIgnoreCase(rs.getString("name"))) {
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
                stmt.execute("ALTER TABLE books ADD COLUMN " + columnName + " " + definition);
                System.out.println("Migration: Added '" + columnName + "' column to books table.");
            } catch (SQLException e) {
                System.err.println("Database Error: Failed to add '" + columnName + "' column. " + e.getMessage());
            }
        }
    }

}
