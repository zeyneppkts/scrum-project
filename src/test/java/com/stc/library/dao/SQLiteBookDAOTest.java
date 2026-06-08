package com.stc.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.stc.library.model.Book;
import com.stc.library.service.BorrowService;

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

    @Test
    public void testBorrowBookIntegration() {
        String testIsbn = "INT-BORROW-888";
        dao.save(new Book("Borrowable Book", "Test Author", 2024, 1, "STC Pub", 2, true, testIsbn));

        // Simulate borrowing one copy through the service layer.
        BorrowService borrowService = new BorrowService(dao);
        Book updated = borrowService.borrowBook(testIsbn);

        assertEquals(1, updated.getCopyNumber());
        assertEquals(1, updated.getBorrowCount());

        // Verify the new state was actually persisted to the database.
        Book reloaded = dao.findByIsbn(testIsbn);
        assertNotNull(reloaded, "Borrowed book should still exist in the database.");
        assertEquals(1, reloaded.getCopyNumber(), "Available copies should be decreased in the DB.");
        assertEquals(1, reloaded.getBorrowCount(), "Borrow statistics should be persisted in the DB.");
        assertTrue(reloaded.isAvailable(), "Book should remain available with one copy left.");
    }

    @Test
    public void testUpdateBookIntegration() {
        String testIsbn = "RETURN-TEST-777";
        Book testBook = new Book("Update Target", "Tester", 2024, 1, "STC", 2, true, testIsbn);
        dao.save(testBook);

        Book savedBook = dao.findByISBN(testIsbn);
        savedBook.returnABook(3);
        dao.updateBook(savedBook);

        Book updatedBook = dao.findByISBN(testIsbn);
        assertNotNull(updatedBook, "Book should exist in database");
        assertEquals(5, updatedBook.getCopyNumber(), "Database stock was not updated correctly!");

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM books WHERE isbn = '" + testIsbn + "'");
        } catch (Exception e) {
            fail("Cleanup failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM books WHERE isbn IN ('INT-TEST-999', 'INT-BORROW-888')");
            System.out.println("Test cleanup: Test data removed from database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
