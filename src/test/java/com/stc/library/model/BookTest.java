package com.stc.library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
}
