package com.stc.library.service;

import com.stc.library.dao.InMemoryBookDAO;
import com.stc.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BorrowServiceTest {

    private InMemoryBookDAO repository;
    private BorrowService borrowService;

    @BeforeEach
    public void setup() {
        repository = new InMemoryBookDAO();
        borrowService = new BorrowService(repository);
        repository.save(new Book("Effective Java", "Joshua Bloch", 2018, 3, "Addison-Wesley", 2, true, "ISBN-EJ"));
    }

    @Test
    public void testBorrowBookDecreasesCopiesAndPersists() {
        Book result = borrowService.borrowBook("ISBN-EJ");

        assertEquals(1, result.getCopyNumber());
        assertEquals(1, result.getBorrowCount());

        // Verify the change was persisted through the repository.
        Book persisted = repository.findByIsbn("ISBN-EJ");
        assertEquals(1, persisted.getCopyNumber());
        assertEquals(1, persisted.getBorrowCount());
    }

    @Test
    public void testBorrowLastCopyMarksBookUnavailable() {
        borrowService.borrowBook("ISBN-EJ");
        Book result = borrowService.borrowBook("ISBN-EJ");

        assertEquals(0, result.getCopyNumber());
        assertFalse(result.isAvailable());
    }

    @Test
    public void testBorrowWhenNoCopiesLeftThrows() {
        borrowService.borrowBook("ISBN-EJ");
        borrowService.borrowBook("ISBN-EJ");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> borrowService.borrowBook("ISBN-EJ"));
        assertTrue(exception.getMessage().contains("Not enough copies available"));
    }

    @Test
    public void testBorrowUnknownIsbnThrows() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> borrowService.borrowBook("DOES-NOT-EXIST"));
        assertTrue(exception.getMessage().contains("Book not found"));
    }
}
