package com.stc.library.dao;

import com.stc.library.model.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import com.stc.library.model.Book;

public class SQLiteBookDAOTest {

    private static SQLiteBookDAO dao;

    @BeforeAll
    public static void setup() {
        DatabaseConnection.URL = "jdbc:sqlite:test_library.db";
        DatabaseConnection.initializeDatabase();
        dao = new SQLiteBookDAO();
    }

    @Test
    public void testSaveBookIntegration() {
        String testIsbn = "INT-TEST-999";
        Book testBook = new Book("Integration Testing Guide", "Scrum Master", 2026, 1, "STC Pub", 3, true, testIsbn);

        dao.save(testBook);

        boolean isBookInDb = false;

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM books WHERE isbn = '" + testIsbn + "'")) {

            if (rs.next()) {
                isBookInDb = true;
                assertEquals("Integration Testing Guide", rs.getString("title"));
            }

        } catch (Exception e) {
            fail("Database connection failed during test: " + e.getMessage());
        }

        assertTrue(isBookInDb, "Book was not found in the database. Integration failed!");
    }

    @AfterAll
    public static void tearDown() {
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM books WHERE isbn = 'INT-TEST-999'");
            System.out.println("Test cleanup: Test data removed from database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
