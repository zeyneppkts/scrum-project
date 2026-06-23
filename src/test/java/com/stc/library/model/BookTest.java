package com.stc.library.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BookTest {

    @Test
    public void testValidBookCreation() {
        Book book = new Book("Scrum Guide", "Ken Schwaber", 2020, 1, "Scrum.org", 5, true, "123456789");

        assertEquals("Scrum Guide", book.getTitle());
        assertEquals(5, book.getCopyNumber());
    }

    @Test
    public void testInvalidPublicationYear() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Book("Old Book", "Unknown", 1000, 1, "Publisher", 2, true, "111");
        });

        assertTrue(exception.getMessage().contains("Publication year must be between"));
    }

    @Test
    public void testNegativeCopyNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Book("Faulty Book", "Author", 2023, 1, "Pub", -5, true, "222");
        });

        assertEquals("Copy number cannot be negative.", exception.getMessage());
    }

    @Test
    public void testEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Book("", "Author", 2023, 1, "Pub", 5, true, "333");
        });
    }

    @Test
    public void testBorrowDecreasesCopiesAndUpdatesStatistics() {
        Book book = new Book("Refactoring", "Martin Fowler", 2018, 2, "Addison-Wesley", 3, true, "444");

        book.borrowABook(1);

        assertEquals(2, book.getCopyNumber(), "One copy should have been borrowed.");
        assertEquals(1, book.getBorrowCount(), "Borrow statistics should be updated.");
        assertTrue(book.isAvailable(), "Book should still be available with copies left.");
    }

    @Test
    public void testBorrowMultipleCopies() {
        Book book = new Book("The Pragmatic Programmer", "Hunt & Thomas", 1999, 1, "Addison-Wesley", 5, true, "555");

        book.borrowABook(3);

        assertEquals(2, book.getCopyNumber());
        assertEquals(3, book.getBorrowCount());
    }

    @Test
    public void testBorrowLastCopyMarksUnavailable() {
        Book book = new Book("Domain-Driven Design", "Eric Evans", 2003, 1, "Addison-Wesley", 1, true, "666");

        book.borrowABook(1);

        assertEquals(0, book.getCopyNumber());
        assertFalse(book.isAvailable(), "Book should be unavailable when no copies remain.");
    }

    @Test
    public void testBorrowMoreThanAvailableThrows() {
        Book book = new Book("Clean Architecture", "Robert C. Martin", 2017, 1, "Prentice Hall", 2, true, "777");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> book.borrowABook(5));

        assertTrue(exception.getMessage().contains("Not enough copies available"));
        assertEquals(2, book.getCopyNumber(), "Copies must stay unchanged on a failed borrow.");
        assertEquals(0, book.getBorrowCount(), "Statistics must not change on a failed borrow.");
    }

    @Test
    public void testBorrowFromUnavailableBookThrows() {
        Book book = new Book("Out of Stock", "Author", 2020, 1, "Pub", 0, false, "888");

        assertThrows(IllegalArgumentException.class, () -> book.borrowABook(1));
    }

    @Test
    public void testBorrowZeroOrNegativeThrows() {
        Book book = new Book("Edge Cases", "Author", 2020, 1, "Pub", 5, true, "999");

        assertThrows(IllegalArgumentException.class, () -> book.borrowABook(0));
        assertThrows(IllegalArgumentException.class, () -> book.borrowABook(-2));
    }

    @Test
    public void testReturnBookIncreasesCopies() {
        Book book = new Book("Test Title", "Test Author", 2000, 1, "Test Pub", 5, true, "12345");

        book.borrowABook(1);

        book.returnABook(1);

        assertEquals(5, book.getCopyNumber());
    }

    @Test
    public void testReturnBookNegativeCopiesThrowsException() {
        Book book = new Book("Return Guide", "Author", 2023, 1, "Pub", 5, true, "111222");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            book.returnABook(-2);
        });

        assertEquals("Number of copies to return must be positive.", exception.getMessage());
    }

    @Test
    public void testAddRatingCalculatesAverageCorrectly() {
        Book book = new Book("Test Statistics Book", "Author", 2023, 1, "Pub", 5, true, "STAT-123");

        book.addRating(4.0);

        assertEquals(4.0, book.getAverageRating(), 0.01);
        assertEquals(1, book.getRatingCount());

        book.addRating(5.0);

        assertEquals(4.5, book.getAverageRating(), 0.01);
        assertEquals(2, book.getRatingCount());
    }

    @Test
    public void testAddRatingThrowsExceptionForInvalidInput() {
        Book book = new Book("Test Invalid Rating", "Author", 2023, 1, "Pub", 5, true, "STAT-456");

        assertThrows(IllegalArgumentException.class, () -> {
            book.addRating(6.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            book.addRating(-1.0);
        });
    }
}
