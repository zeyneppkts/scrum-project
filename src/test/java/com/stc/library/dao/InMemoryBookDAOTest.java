package com.stc.library.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.stc.library.model.Book;

public class InMemoryBookDAOTest {

    @Test
    public void testSaveBook() {
        IBookRepository repository = new InMemoryBookDAO();

        Book book = new Book("Clean Code", "Robert C. Martin", 2008, 1, "Prentice Hall", 10, true, "9780132350884");

        repository.save(book);

        assertEquals(1, repository.findAll().size());

        assertEquals("Clean Code", repository.findAll().get(0).getTitle());
    }

    @Test
    void testName() {

    }

}
